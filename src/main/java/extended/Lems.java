package extended;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import org.lemsml.model.ComponentType;

@XmlTransient
public class Lems extends org.lemsml.model.Lems
{

	private Map<String, ComponentType> componentTypesByNameHM = new HashMap<String, ComponentType>();

	public ComponentType getComponentTypeByName(String name)
	{
		return componentTypesByNameHM.get(name);
	}

	public void registerComponentTypeName(String name, ComponentType ct)
	{
		this.componentTypesByNameHM.put(name, ct);
	}

	public Map<String, ComponentType> getComponentTypesByNameHM()
	{
		return componentTypesByNameHM;
	}

}
