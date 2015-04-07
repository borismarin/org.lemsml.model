package org.lemsml.model.extended;

import org.lemsml.model.Parameter;

/**
 * @author borismarin
 *
 */
//TODO: review this class, looking ugly
public class ParameterInstance {
	private PhysicalQuantity value;
	private Parameter definition;

	public Parameter getDefinition() {
		return definition;
	}

	public void setDefinition(Parameter definition) {
		this.definition = definition;
	}

	public PhysicalQuantity getValue() {
		return value;
	}

	public void setValue(PhysicalQuantity value) {
		this.value = value;
	}

}
