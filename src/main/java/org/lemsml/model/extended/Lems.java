package org.lemsml.model.extended;

import static tec.units.ri.AbstractUnit.ONE;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlTransient;

import org.lemsml.model.Constant;
import org.lemsml.model.compiler.IHasParentFile;
import org.lemsml.model.compiler.IScope;
import org.lemsml.model.compiler.ISymbol;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * @author borismarin
 *
 */
@XmlTransient
public class Lems extends org.lemsml.model.Lems implements IScope {

	@XmlTransient
	private Map<String, Component> idToComponent = new HashMap<String, Component>();
	@XmlTransient
	private Map<String, ComponentType> nameToCompType = new HashMap<String, ComponentType>();
	@XmlTransient
	private Map<String, Constant> nameToConstant = new HashMap<String, Constant>();
	/*
	 * TODO: notice that there is a discrepancy between what LEMS calls
	 * dimensions and what UOM calls dimensions. We'll thus confusingly use
	 * Unit<?> here to store a dimension...
	 */
	@XmlTransient
	private Map<String, javax.measure.Unit<?>> nameToDimension = new HashMap<String, javax.measure.Unit<?>>();
	{
		//TODO: ugly.
	    nameToDimension.put("none", ONE);
	}
	@XmlTransient
	private Map<String, javax.measure.Unit<?>> symbolToUnit = new HashMap<String, javax.measure.Unit<?>>();
	{
		//TODO: ugly.
	    symbolToUnit.put("none", ONE);
	    symbolToUnit.put("", ONE);
	}
	@XmlTransient
	private Map<String, ISymbol<?>> scope = new HashMap<String, ISymbol<?>>();

	@XmlTransient
	private static final Logger logger = (Logger) LoggerFactory.getLogger(Lems.class);


	public Map<String, ISymbol<?>> getScope() {
		return scope;
	}

	public Component getComponentById(String id) {
		return idToComponent.get(id);
	}

	public ComponentType getComponentTypeByName(String name) {
		// TODO: error handling here?
		return nameToCompType.get(name);
	}

	public Constant getConstantByName(String name) {
		return nameToConstant.get(name);
	}

	public javax.measure.Unit<?> getDimensionByName(String name) {
		return nameToDimension.get(name);
	}

	public javax.measure.Unit<?> getUnitBySymbol(String name) {
		return symbolToUnit.get(name);
	}

	public Map<String, ComponentType> getNameToCompTypeMap() {
		return nameToCompType;
	}

	public void registerComponentId(String id, Component c) {
		Component old = this.idToComponent.put(id, c);
		if (old != null){
			this.warnMapOverwrite(id, old, c);
		}

	}

	public void registerComponentTypeName(String name, ComponentType ct) {
		ComponentType old = this.nameToCompType.put(name, ct);
		if (old != null){
			this.warnMapOverwrite(name, old, ct);
		}
	}

	public void registerConstantName(String name, Constant ctt) {
		Constant old = this.nameToConstant.put(name, ctt);
		if (old != null){
			this.warnMapOverwrite(name, old, ctt);
		}
	}

	public void registerDimensionName(String name, javax.measure.Unit<?> dim) {
		this.nameToDimension.put(name, dim);
	}

	public void registerUnitSymbol(String name, javax.measure.Unit<?> unit) {
		this.symbolToUnit.put(name, unit);
	}

	@Override
	public IScope getEnclosingScope() {
		return null;
	}

	@Override
	public void define(ISymbol<?> sym) {
		ISymbol<?> old = this.scope.put(sym.getName(), sym);
		if (old != null){
			this.warnMapOverwrite(sym.getName(), old, sym);
		}
	}

	@Override
	public ISymbol<?> resolve(String name) {
		return this.scope.get(name);
	}

	@Override
	public String getScopeName() {
		return "global";
	}

	@Override
	public Set<String> getDefinedSymbols() {
		return this.scope.keySet();
	}
	
	private <K, V extends IHasParentFile> void warnMapOverwrite(K key, V oldval, V newval) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Overwriting symbol '%s'!\n", key));
		sb.append(String.format("\t -> old: [defined in %s]: %s\n", oldval.getDefinedIn().getName(), oldval));
		sb.append(String.format("\t -> new: [defined in %s]: %s\n", newval.getDefinedIn().getName(), newval));
		logger.warn(sb.toString());
	}

}
