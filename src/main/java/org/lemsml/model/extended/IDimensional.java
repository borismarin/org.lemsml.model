package org.lemsml.model.extended;

import javax.measure.Unit;

public interface IDimensional {
	Unit<?> getUnit();
	void setUnit(Unit<?> u);
}
