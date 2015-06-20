package org.lemsml.model.extended;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.measure.Unit;

import org.lemsml.model.compiler.ISymbol;

import tec.units.ri.quantity.NumberQuantity;

import com.google.common.collect.ImmutableMap;

import expr_parser.utils.ExpressionParser;
import expr_parser.utils.UndefinedSymbolException;

public class SymbolicExpression<T extends IValueDefinition> implements ISymbol<T> {

	private Map<String, Double> context = new HashMap<String, Double>();
	private String name;
	private T type;
	private Unit<?> unit;
	private Double value;
	private Map<String, Unit<?>> unitContext = new HashMap<String, Unit<?>>();

	public SymbolicExpression(String name, T instance) {
		this.name = name;
		this.type = instance;
	}

	@Override
	public Double evaluate() throws UndefinedSymbolException {
		if(null != this.value){
			return this.value;
		}
		return ExpressionParser.evaluateInContext(this.getType().getValueDefinition(), this.getContext());
	}
	
	public Double evaluate(Map<String, Double> indepVars)
			throws UndefinedSymbolException {
		Double ret = this.value;
		if (null == ret) {
			ImmutableMap<String, Double> context = new ImmutableMap.Builder<String, Double>()
					.putAll(this.getContext())
					.putAll(indepVars)
					.build();
			ret = ExpressionParser.evaluateInContext(this.getType().getValueDefinition(),
					context);
		}
		return ret;
	}


	public Map<String, Double> getContext() {
		return context;
	}

	public void setContext(Map<String, Double> context) {
		this.value = null; //ugly: marking as invalid due to new context...
		this.context = context;
	}
	
	public Set<String> getIndependentVariables(){
		Set<String> syms = ExpressionParser.listSymbolsInExpression(
				this.getType().getValueDefinition());
		Set<String> inContext = new HashSet<String>(this.context.keySet());
		syms.removeAll(inContext);
		return syms;
	}


	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Unit<?> getUnit() {
		return this.unit;
	}

	public void setUnit(Unit<?> unit) {
		this.unit = unit;
	}

	@Override
	public T getType() {
		return this.type;
	}

	@Override
	public void setType(T type) {
		this.type = type;
	}

	@Override
	public Double evaluateSI() throws UndefinedSymbolException {
		// TODO Auto-generated method stub
		return new Double(NumberQuantity.of(evaluate(), getUnit()).toSI().getValue()
				.doubleValue());
	}

	public Map<String,Unit<?>> getUnitContext() {
		return this.unitContext;
	}

	@Override
	public String getValueDefinition() {
		return this.getType().getValueDefinition();
	}


	@Override
	public Double getValue() {
		return this.value;
	}

	@Override
	public void setValue(Double val) {
		this.value = val;
	}

//	@Override
//	public IDimensionalEvaluable getDimensionalValue() {
//		return new PhysicalQuantity(this.evaluate(), this.getUnit().getSymbol());
//		this.dimensionalValue = quant;
//		this.value = quant.evaluate(null);
//		return this.dimensionalValue;
//	}


}
