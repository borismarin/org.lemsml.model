package org.lemsml.model.extended;

import static tec.units.ri.AbstractUnit.ONE;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.measure.Unit;
import javax.xml.bind.annotation.XmlTransient;

import org.lemsml.model.Constant;
import org.lemsml.model.compiler.ISymbol;
import org.lemsml.visitors.Visitor;

/**
 * @author borismarin
 *
 */
@XmlTransient
public class Lems extends org.lemsml.model.Lems implements IScope, INamed {

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

	public Component registerComponentId(String id, Component c) {
		return this.idToComponent.put(id, c);
	}

	public ComponentType registerComponentTypeName(String name, ComponentType ct) {
		return this.nameToCompType.put(name, ct);
	}

	public Constant registerConstantName(String name, Constant ctt) {
		return this.nameToConstant.put(name, ctt);
	}

	public Unit<?> registerDimensionName(String name, javax.measure.Unit<?> dim) {
		return this.nameToDimension.put(name, dim);
	}

	public Unit<?> registerUnitSymbol(String name, javax.measure.Unit<?> unit) {
		return this.symbolToUnit.put(name, unit);
	}

	@Override
	public IScope getEnclosingScope() {
		return null;
	}

	@Override
	public ISymbol<?> define(ISymbol<?> sym) {
		return this.scope.put(sym.getName(), sym);
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

	@Override
	public <R, E extends Throwable> R accept(Visitor<R, E> aVisitor) throws E {
		return aVisitor.visit(this);
	}

	public String getName() {
		return getScopeName();
	}

}
