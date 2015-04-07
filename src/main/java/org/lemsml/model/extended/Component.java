package org.lemsml.model.extended;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import org.lemsml.model.ComponentType;
import org.lemsml.model.NamedDimensionalType;
import org.lemsml.model.Parameter;

/**
 * @author borismarin
 *
 */
@XmlTransient
public class Component extends org.lemsml.model.Component {
	@XmlTransient
	ComponentType _ComponentType;
	
	@XmlTransient
	private Map<String, ParameterValue> nameToParameterValue = new HashMap<String, ParameterValue>();

	public ComponentType getComponentType() {
		return _ComponentType;
	}

	public void setComponentType(ComponentType _ComponentType) {
		this._ComponentType = _ComponentType;
	}
	

	public Boolean hasName(List<? extends NamedDimensionalType> nameables,
			String name) {
		Boolean ret = false;
		for (NamedDimensionalType t : nameables) {
			if (t.getName().equals(name)) {
				ret = true;
				break;
			}
		}
		return ret;
	}

//	public PhysicalQuantity getParameterValue(String parName)
//			throws LEMSCompilerException {
//		ComponentType type = this.getComponentType();
//		if (hasName(type.getParameters(), parName)) {
//			String parVal = this.getOtherAttributes().get(new QName(parName));
//			return (new PhysicalQuantityAdapter()).unmarshal(parVal);
//		} else {
//			//TODO: shouldn't this type of error be handled somewhere else? 
//			throw new LEMSCompilerException("ComponentType" + type
//					+ " does not allow parameter " + parName,
//					LEMSCompilerError.ParameterNotAllowed);
//		}
//	}

	public void registerParameter(Parameter pDef, ParameterValue pVal) {
		this.nameToParameterValue.put(pDef.getName(), pVal);
	}

	public ParameterValue getParameterByName(String name) {
		return this.nameToParameterValue.get(name);
	}
}
