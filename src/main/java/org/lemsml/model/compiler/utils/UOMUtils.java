package org.lemsml.model.compiler.utils;

import static tec.units.ri.AbstractUnit.ONE;
import static tec.units.ri.unit.SI.AMPERE;
import static tec.units.ri.unit.SI.CANDELA;
import static tec.units.ri.unit.SI.KELVIN;
import static tec.units.ri.unit.SI.KILOGRAM;
import static tec.units.ri.unit.SI.METRE;
import static tec.units.ri.unit.SI.MOLE;
import static tec.units.ri.unit.SI.SECOND;

import javax.measure.Unit;

import org.lemsml.model.extended.Dimension;

public abstract class UOMUtils {

	@SuppressWarnings("deprecation")
	public static Unit<?> LemsDimensionToUOM(Dimension lemsDim) {
		Unit<?> dim = ONE;
		dim = dim.multiply(AMPERE.pow(lemsDim.getI().intValue()));
		dim = dim.multiply(CANDELA.pow(lemsDim.getJ().intValue()));
		dim = dim.multiply(KELVIN.pow(lemsDim.getK().intValue()));
		dim = dim.multiply(METRE.pow(lemsDim.getL().intValue()));
		dim = dim.multiply(KILOGRAM.pow(lemsDim.getM().intValue()));
		dim = dim.multiply(MOLE.pow(lemsDim.getN().intValue()));
		dim = dim.multiply(SECOND.pow(lemsDim.getT().intValue()));
		// TODO: notice that there is a discrepancy between what LEMS calls
		// dimensions and what UOM calls dimensions. We'll thus confusingly
		// return an Unit<?> here instead of a javax.measure.dimension
		// dim.getDimension()
		return dim;
	}

}
