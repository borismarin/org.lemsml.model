package org.lemsml.model.extended;

import org.lemsml.model.Parameter;

public class ParameterValue {
	private PhysicalQuantity value;
	private Parameter definition;

	public ParameterValue(Parameter type, String valueUnit) {
		PhysicalQuantityAdapter adapter = new PhysicalQuantityAdapter();
		this.value = adapter.unmarshal(valueUnit);
		this.definition = type;
	}

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
