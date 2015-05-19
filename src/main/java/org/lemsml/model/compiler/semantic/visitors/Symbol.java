package org.lemsml.model.compiler.semantic.visitors;

import javax.measure.Unit;

import org.lemsml.model.compiler.IDimensionalEvaluable;
import org.lemsml.model.compiler.ISymbol;

public class Symbol<T> implements ISymbol<T> {

	private Double value;
	private String name;
	private Unit<?> unit;
	private T type;

	public Symbol(String name, T instance, IDimensionalEvaluable quant) {
		this.name = name;
		this.type = instance;
		this.value = quant.getValue();
		this.unit = quant.getUnit();
	}

	@Override
	public Double getValue() {
		return this.value;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
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
		// TODO Auto-generated method stub

	}

}
