package org.lemsml.model.extended;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import org.lemsml.model.ComponentType;
import org.lemsml.model.Constant;

/**
 * @author borismarin
 *
 */
@XmlTransient
public class Lems extends org.lemsml.model.Lems {

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
	@XmlTransient
	private Map<String, javax.measure.Unit<?>> symbolToUnit = new HashMap<String, javax.measure.Unit<?>>();

	public Component getComponentById(String id) {
		return idToComponent.get(id);
	}

	public ComponentType getComponentTypeByName(String name) {
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

	public Map<String, ComponentType> getNameToCompType() {
		return nameToCompType;
	}

	public void registerComponentId(String id, Component c) {
		this.idToComponent.put(id, c);

	}

	public void registerComponentTypeName(String name, ComponentType ct) {
		this.nameToCompType.put(name, ct);
	}

	public void registerConstantName(String name, Constant ctt) {
		this.nameToConstant.put(name, ctt);
	}

	public void registerDimensionName(String name, javax.measure.Unit<?> dim) {
		this.nameToDimension.put(name, dim);
	}

	public void registerUnitSymbol(String name, javax.measure.Unit<?> unit) {
		this.symbolToUnit.put(name, unit);
	}

}
