package org.lemsml.model.extended;

import org.lemsml.model.Parameter;

/**
 * @author borismarin
 *
 */
// TODO: review this class, looking ugly
public class ParameterInstance implements IValued {
	private PhysicalQuantity dimensionalValue;
	private Parameter definition;

	public Parameter getDefinition() {
		return definition;
	}

	public void setDefinition(Parameter definition) {
		this.definition = definition;
	}

	public PhysicalQuantity getDimensionalValue() {
		return dimensionalValue;
	}

	public void setDimensionalValue(PhysicalQuantity value) {
		this.dimensionalValue = value;
	}

	@Override
	public Double getValue() {
		return this.dimensionalValue.getValue();
	}

}
