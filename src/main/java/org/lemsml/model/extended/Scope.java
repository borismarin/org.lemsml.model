package org.lemsml.model.extended;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.measure.Quantity;
import javax.measure.Unit;

import org.lemsml.model.DerivedVariable;
import org.lemsml.model.Requirement;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.interfaces.IScope;
import org.lemsml.model.extended.interfaces.IScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import expr_parser.utils.DirectedGraph;
import expr_parser.utils.ExpressionParser;
import expr_parser.utils.UndefinedSymbolException;
import expr_parser.visitors.AntlrExpressionParser;

public class Scope implements IScope{

	private static final Logger logger = LoggerFactory.getLogger(IScope.class);

	private Map<String, Symbol> symbolTable = new HashMap<String, Symbol>();
	private String name;
	private IScope parent;
	private IScoped belongsTo;

	private Map<String, String> expressions = new HashMap<String, String>();
	private Map<String, Unit<?>> unitContext = new HashMap<String, Unit<?>>();

	private DirectedGraph<String> dependencies = new DirectedGraph<String>();

	public Scope(String string) {
		this.setScopeName(string);
	}

	@Override
	public Symbol define(Symbol sym) throws LEMSCompilerException {
		buildDependencies(sym);
		getExpressions().put(sym.getName(), sym.getValueDefinition());
		sym.setParser(new AntlrExpressionParser(sym.getValueDefinition()));
		sym.setInScope(this);
		return this.symbolTable.put(sym.getName(), sym);
	}

	private void buildDependencies(Symbol sym) throws LEMSCompilerException {
		getDependencies().addNode(sym.getName());
		if (null != sym.getValueDefinition()) { // TODO: better logic for
												// select/reduce
			for (String dep : ExpressionParser.listSymbolsInExpression(sym
					.getValueDefinition())) {
				if (!(sym.getValueDefinition().equals(dep))) {
					getDependencies().addNode(dep);
					getDependencies().addEdge(sym.getName(), dep);
				}
			}
		} else { // select/reduce
			DerivedVariable dv = ((DerivedVariable) sym.getType());
			String path = dv.getSelect().replace('/', '.');
			List<String> deps = PathQDParser.expand(path, (Component) this.getBelongsTo());
			for (String dep : deps) {
				getDependencies().addNode(dep);
				getDependencies().addEdge(sym.getName(), dep);
			}
			String expandedValue = PathQDParser.reduceToExpr(deps, Optional.fromNullable(dv.getReduce()));
			if(expandedValue.isEmpty()){
				String w = MessageFormat.format("Evaluation of path ({0}) for [{1}] resulted in empty list", path, getBelongsTo());
				logger.warn(w);
				Lems lemsRoot = (Lems) ((Scope) getLemsRoot(this)).getBelongsTo();
				String firstUnitOfDim = lemsRoot.getAllUnitsForDimension(dv.getDimension()).get(0).getSymbol();
				sym.setValueDefinition("0" + firstUnitOfDim); //TODO: UGLYUGLY
			}
			else{
				sym.setValueDefinition(expandedValue);
			}
		}
	}

	IScope getLemsRoot(IScope scope){
		IScope s = scope;
		while(!s.getScopeName().equals("global")){
			s = s.getEnclosingScope();
		}
		return s;
	}

	@Override
	public Symbol resolve(String name) throws LEMSCompilerException {
		// Scoping rules: if it is not found, look for it in parent (recurse)
		//                paths allow direct access to any symbol in a scope
		Symbol symb = this.symbolTable.get(name);
		if(null != symb){
			//TODO: ugly corner-case logic
			if(symb.getType() instanceof Requirement){
				try{ // try to find actual def upscope
					return getParent().resolve(name);
				} catch(LEMSCompilerException e){
					if (e.getErrorCode().equals(LEMSCompilerError.UndefinedSymbol)){
						return symb; // OK, it is an unbound requirement
					}else{
						throw e;
					}
				}
			}
			return symb;
		}
		if(name.indexOf('.') > 0){ // path notation breaks neat scoping...
			return PathQDParser.resolvePath(name, (Component) this.getBelongsTo());
		}
		if (null != getParent()) {
			return getParent().resolve(name);
		}
		throw new LEMSCompilerException("Undefined symbol: " + name,
				LEMSCompilerError.UndefinedSymbol);
	}

	@Override
	public Set<String> getDefinedSymbols() {
		return this.symbolTable.keySet();
	}

	//TODO: this method could use some refactoring...
	private Map<String, Quantity<?>> evalDependencies(Symbol symbol,
										Map<String, Quantity<?>> localContext,
										Map<String, Quantity<?>> indepVars)
                        throws LEMSCompilerException, UndefinedSymbolException {
		localContext.putAll(indepVars);
		for (String dep : getDependencies().edgesFrom(symbol.getName())) {
			// don't calculate deps which are already calculated, nor circular deps (e.g. state vars)
			if (!localContext.containsKey(dep) && !(dep.equals(symbol.getName()))){
				// Need to evaluate vars in upper scopes, which can't see this one
				Symbol resolved = resolve(dep);
				if (!resolved.getScope().equals(this)) {
					localContext.put(dep,
							resolved.getScope().evaluate(resolved.getName(), indepVars)); // don't carry local names!!
				} else {
					localContext.putAll(evalDependencies(resolved, localContext, indepVars));
				}
			}
		}
		localContext.put(symbol.getName(),
				ExpressionParser.evaluateQuantityInContext(symbol.getParser(),
						localContext, getUnitContext()));
//				ExpressionParser.evaluateQuantityInContext(symbol.getValueDefinition(),
//						localContext, getUnitContext()));

		return localContext;

	}

	public Map<String, String> buildContext(Symbol symbol, Map<String, String> localContext) throws LEMSCompilerException,
			UndefinedSymbolException {
		for (String dep : getDependencies().edgesFrom(symbol.getName())) {
			if (!localContext.containsKey(dep) && !(dep.equals(symbol.getName()))) {
				Symbol resolved = resolve(dep);
				if (!resolved.getScope().equals(this)) {
					localContext.putAll(resolved.getScope().buildContext(resolved, new HashMap<String, String>()));
				} else {
					localContext.putAll(buildContext(resolved, localContext));
				}
			}
		}
		localContext.put(symbol.getName(), symbol.getValueDefinition());
		return localContext;

	}


	public Quantity<?> evaluate(String symbolName) throws LEMSCompilerException {
		return evaluate(symbolName, new HashMap<String, Quantity<?>>());
	}

	public Quantity<?> evaluate(String toEval,
			Map<String, Quantity<?>> indepVars) throws LEMSCompilerException {
		try {
			HashMap<String, Quantity<?>> context = new HashMap<String, Quantity<?>>();
			Map<String, Quantity<?>> evaluated = evalDependencies(
					resolve(toEval), context, indepVars);
			return evaluated.get(toEval);
		} catch (UndefinedSymbolException e) {
			throw new LEMSCompilerException(e.getMessage(),
					LEMSCompilerError.MissingSymbolValue);
		}
	}

	@Override
	public String getScopeName() {
		return this.name;
	}

	@Override
	public IScope getEnclosingScope() {
		return this.parent;
	}

	public void setScopeName(String name) {
		this.name = name;
	}

	public IScope getParent() {
		return parent;
	}

	public void setParent(IScope parent) {
		this.parent = parent;
	}

	public Map<String, String> getExpressions() {
		return expressions;
	}

	public Map<String, Unit<?>> getUnitContext() {
		return unitContext;
	}

	public DirectedGraph<String> getDependencies() {
		return dependencies;
	}

	public void setUnitContext(Map<String, Unit<?>> unitContext) {
		this.unitContext = unitContext;
	}

	//TODO: consider moving Scope logic back into Component/Lems
	public IScoped getBelongsTo() {
		return belongsTo;
	}

	public void setBelongsTo(IScoped scoped) {
		this.belongsTo = scoped;
	}

	public Symbol get(String name) throws LEMSCompilerException{
		return resolve(name);
	}

}
