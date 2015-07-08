package org.lemsml.model.extended;

import static tec.units.ri.AbstractUnit.ONE;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.measure.quantity.Dimensionless;
import javax.xml.bind.annotation.XmlTransient;

import org.lemsml.model.Constant;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.interfaces.INamed;
import org.lemsml.model.extended.interfaces.IScope;
import org.lemsml.model.extended.interfaces.IScoped;
import org.lemsml.visitors.Visitor;

import tec.units.ri.unit.BaseUnit;

/**
 * @author borismarin
 *
 */
public class Lems extends org.lemsml.model.Lems implements IScoped, INamed {

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
	private final BaseUnit<Dimensionless> anyDimension = new BaseUnit<Dimensionless>(
			"*");
	@XmlTransient
	private Map<String, javax.measure.Unit<?>> nameToDimension = new HashMap<String, javax.measure.Unit<?>>();
	{
		// TODO: ugly.
		nameToDimension.put("none", ONE);
		nameToDimension.put("", ONE);

		// TODO: ugly workaround for '*'
		nameToDimension.put("*", anyDimension);
	}
	@XmlTransient
	private Map<String, javax.measure.Unit<?>> symbolToUnit = new HashMap<String, javax.measure.Unit<?>>();
	{
		// TODO: ugly.
		getSymbolToUnit().put("none", ONE);
		getSymbolToUnit().put("", ONE);

	}

	@XmlTransient
	private Scope scope = new Scope("global");

	public Component getComponentById(String id) throws LEMSCompilerException {
		Component component = idToComponent.get(id);
		if(null == component){
			String msg = MessageFormat.format(
					"Component ID [{0}] undefined in [({1}) {2}].",
					id,
					this.getClass().getSimpleName(),
					this.getName());
			throw new LEMSCompilerException(msg,
					LEMSCompilerError.UndefinedID);
		}
		return component;
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
		return getSymbolToUnit().get(name);
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

	public javax.measure.Unit<?> registerDimensionName(String name, javax.measure.Unit<?> dim) {
		return this.nameToDimension.put(name, dim);
	}

	public javax.measure.Unit<?> registerUnitSymbol(String name, javax.measure.Unit<?> unit) {
		return this.getSymbolToUnit().put(name, unit);
	}

	@Override
	public <R, E extends Throwable> R accept(Visitor<R, E> aVisitor) throws E {
		return aVisitor.visit(this);
	}

	public javax.measure.Unit<?> getAnyDimension() {
		return anyDimension;
	}

	@Override
	public IScope getScope() {
		return scope;
	}

	@Override
	public String getName() {
		return scope.getScopeName();
	}

	public Map<String, javax.measure.Unit<?>> getSymbolToUnit() {
		return symbolToUnit;
	}

	public void setSymbolToUnit(Map<String, javax.measure.Unit<?>> symbolToUnit) {
		this.symbolToUnit = symbolToUnit;
	}

}
