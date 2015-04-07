package org.lemsml.model.extended;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import org.lemsml.model.ComponentType;
import org.lemsml.model.LEMSCompilerError;
import org.lemsml.model.Parameter;
import org.lemsml.model.exceptions.LEMSCompilerException;

/**
 * @author borismarin
 *
 */
@XmlTransient
public class Component extends org.lemsml.model.Component {
	@XmlTransient
	ComponentType _ComponentType;

	@XmlTransient
	private Map<String, ParameterInstance> nameToParameterValue = new HashMap<String, ParameterInstance>();

	public ComponentType getComponentType() {
		return _ComponentType;
	}

	public void setComponentType(ComponentType _ComponentType) {
		this._ComponentType = _ComponentType;
	}

	public void registerParameter(Parameter pDef, ParameterInstance pVal) {
		this.nameToParameterValue.put(pDef.getName(), pVal);
	}

	public ParameterInstance getParameterByName(String name)
			throws LEMSCompilerException {
		ParameterInstance pVal = this.nameToParameterValue.get(name);
		if (null != pVal) {
			return pVal;
		} else {
			// TODO: shouldn't this type of error be handled somewhere else?
			throw new LEMSCompilerException("ComponentType" + type
					+ " does not allow parameter " + name,
					LEMSCompilerError.ParameterNotAllowed);
		}
	}
}
