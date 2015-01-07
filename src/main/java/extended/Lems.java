package extended;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import org.lemsml.model.ComponentType;


@XmlTransient
public class Lems extends org.lemsml.model.Lems {
	
	private Map<String, ComponentType> componentTypesByName = new HashMap<String, ComponentType>();


	public ComponentType getComponentByName(String name) {
		return componentTypesByName.get(name);
	}

	public void registerComponentType(String name, ComponentType ct) {
		this.componentTypesByName.put(name, ct);
		
	}
	
	public Map<String, ComponentType> getComponentTypesByName() {
		return componentTypesByName;
	}

	public void setComponentTypesByName(
			Map<String, ComponentType> componentTypesByName) {
		this.componentTypesByName = componentTypesByName;
	}

}
