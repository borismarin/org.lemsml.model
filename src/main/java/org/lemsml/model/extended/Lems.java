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
	private Map<String, ComponentType> nameToCompType = new HashMap<String, ComponentType>();
	// TODO: notice that there is a discrepancy between what LEMS calls
	// dimensions
	// and what UOM calls dimensions. We'll thus confusingly use Unit<?> here
	// to store a dimension...
	@XmlTransient
	private Map<String, javax.measure.Unit<?>> nameToDimension = new HashMap<String, javax.measure.Unit<?>>();
	@XmlTransient
	private Map<String, javax.measure.Unit<?>> symbolToUnit = new HashMap<String, javax.measure.Unit<?>>();
	@XmlTransient
	private Map<String, Constant> nameToConstant = new HashMap<String, Constant>();

	public ComponentType getComponentTypeByName(String name) {
		return nameToCompType.get(name);
	}

	public void registerComponentTypeName(String name, ComponentType ct) {
		this.nameToCompType.put(name, ct);
	}

	public void registerDimensionName(String name, javax.measure.Unit<?> dim) {
		this.nameToDimension.put(name, dim);
	}

	public void registerUnitName(String name, javax.measure.Unit<?> unit) {
		this.symbolToUnit.put(name, unit);
	}

	public javax.measure.Unit<?> getDimensionByName(String name) {
		return nameToDimension.get(name);
	}

	public void registerConstantName(String name, Constant ctt) {
		this.nameToConstant.put(name, ctt);
	}

	public Constant getConstantByName(String name) {
		return nameToConstant.get(name);
	}

	public Map<String, ComponentType> getNameToCompType() {
		return nameToCompType;
	}

	public Map<String, javax.measure.Unit<?>> getNameToDimension() {
		return nameToDimension;
	}

	public Map<String, javax.measure.Unit<?>> getSymbolToUnit() {
		return symbolToUnit;
	}

}
