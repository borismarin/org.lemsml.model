package org.lemsml.model.extended;

import java.util.List;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.namespace.QName;

import org.lemsml.model.ComponentType;
import org.lemsml.model.NamedDimensionalType;
import org.lemsml.model.exceptions.LEMSParserException;

/**
 * @author borismarin
 *
 */
@XmlTransient
public class Component extends org.lemsml.model.Component {
	@XmlTransient
	ComponentType _ComponentType;

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

	public PhysicalQuantity getParameterValue(String parName)
			throws LEMSParserException {
		if (hasName(this.getComponentType().getParameters(), parName)) {
			String parVal = this.getOtherAttributes().get(new QName(parName));
			return (new PhysicalQuantityAdapter()).unmarshal(parVal);
		} else {
			throw new LEMSParserException("Parameter " + parName
					+ " not allowed in ComponentType "
					+ this.getComponentType());
		}
	}
}
