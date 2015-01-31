package extended;

import java.util.HashMap;
import java.util.Map;

import javax.measure.Unit;
import javax.xml.bind.annotation.XmlTransient;

import org.lemsml.model.ComponentType;

@XmlTransient
public class Lems extends org.lemsml.model.Lems {

	private Map<String, ComponentType> nameToCompType = new HashMap<String, ComponentType>();
	private Map<String, javax.measure.Dimension> nameToDimension = new HashMap<String, javax.measure.Dimension>();
	private Map<String, Unit<?>> nameToUnit = new HashMap<String, Unit<?>>();

	public ComponentType getComponentTypeByName(String name) {
		return nameToCompType.get(name);
	}


	public void registerComponentTypeName(String name, ComponentType ct) {
		this.nameToCompType.put(name, ct);
	}


	public Map<String, ComponentType> getComponentTypesByNameHM() {
		return nameToCompType;
	}


	public Map<String, javax.measure.Dimension> getNameToDimension() {
		return nameToDimension;
	}


	public void setNameToDimension(Map<String, javax.measure.Dimension> nameToDimension) {
		this.nameToDimension = nameToDimension;
	}


	public Map<String, Unit<?>> getNameToUnit() {
		return nameToUnit;
	}


	public void setNameToUnit(Map<String, Unit<?>> nameToUnit) {
		this.nameToUnit = nameToUnit;
	}


}
