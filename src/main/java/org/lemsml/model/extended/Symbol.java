package org.lemsml.model.extended;

import javax.measure.Unit;

import org.lemsml.model.compiler.ISymbol;

import tec.units.ri.quantity.NumberQuantity;
import expr_parser.utils.UndefinedParameterException;

public class Symbol<T> implements ISymbol<T> {

	private Double value;
	private String name;
	private T type;
	private Unit<?> unit;

	public Symbol(String name, T instance) {
		this.name = name;
		this.type = instance;
	}

	@Override
	public Double evaluate() throws UndefinedParameterException {
		return this.value;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Unit<?> getUnit() {
		return this.unit;
	}

	@Override
	public T getType() {
		return this.type;
	}

	@Override
	public void setType(T type) {
		this.type = type;
	}


	@Override
	public Double evaluateSI() {
		return new Double(NumberQuantity.of(value, getUnit()).toSI().getValue().doubleValue());
	}

	public void setUnit(Unit <?> unit) {
		this.unit = unit;
	}

	public Double getValue() {
		return this.value;
	}

	@Override
	public void setValue(Double val) {
		this.value = val;
		
	}

	@Override
	public String getValueDefinition() {
		return ((IValueDefinition) this.getType()).getValueDefinition();
	}

}
