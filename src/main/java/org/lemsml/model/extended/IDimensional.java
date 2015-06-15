package org.lemsml.model.extended;

import javax.measure.Unit;

interface IDimensional {
	Unit<?> getUnit();
	void setUnit(Unit<?> u);
}
