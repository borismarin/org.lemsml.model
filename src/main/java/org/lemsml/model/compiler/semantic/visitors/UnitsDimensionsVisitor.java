package org.lemsml.model.compiler.semantic.visitors;

import org.lemsml.model.compiler.utils.UOMUtils;
import org.lemsml.model.extended.Dimension;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.LemsNode;
import org.lemsml.model.extended.Unit;
import org.lemsml.visitors.BaseVisitor;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * @author borismarin
 *
 */
public class UnitsDimensionsVisitor extends BaseVisitor<Boolean, Throwable> {

	private Lems lems;
	private static final Logger logger = (Logger) LoggerFactory
			.getLogger(UnitsDimensionsVisitor.class);

	private void logRegistration(String identifier, LemsNode n) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Registering [%s '%s'] ", n.getClass()
				.getSimpleName(), identifier));
		sb.append(String.format("defined in " + n.getDefinedIn().getName()));
		logger.debug(sb.toString());
	}

	private <K, V extends javax.measure.Unit<?>> void warnMapOverwrite(K key, V oldval, V newval) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Overwriting unit/dimension '%s'!\n", key));
		sb.append(String.format("\t -> old:  %s\n", oldval));
		sb.append(String.format("\t -> new:  %s\n", newval));
		logger.warn(sb.toString());
	}

	/**
	 * @param lems
	 * @throws Throwable
	 */
	public UnitsDimensionsVisitor(Lems lems) throws Throwable {
		this.lems = lems;
	}

	@Override
	public Boolean visit(Dimension dimension) throws Throwable {
		dimension.setUOMDimension(UOMUtils.LemsDimensionToUOM(dimension));
		logRegistration(dimension.getName(), dimension);
		javax.measure.Unit<?> old = this.lems.registerDimensionName(dimension.getName(), dimension.getUOMDimension());
		if(null != old){
			warnMapOverwrite(dimension.getName(), old, dimension.getUOMDimension());
		};
		return true;
	}

	@Override
	public Boolean visit(Unit unit) throws Throwable {
		javax.measure.Unit<?> dim = lems.getDimensionByName(unit.getDimension());

		// That's why we don't store a javax.measure.Dimension in nameToDim:
		// dimensions can't be operated with
		javax.measure.Unit<?> uomUnit = dim.multiply(Math.pow(10, unit.getPower().intValue()));
		uomUnit = uomUnit.shift(unit.getOffset());

		unit.setUOMUnit(uomUnit);
		logRegistration(unit.getSymbol(), unit);
		javax.measure.Unit<?> old = lems.registerUnitSymbol(unit.getSymbol(), uomUnit);
		if(null != old){
			warnMapOverwrite(unit.getSymbol(), old, uomUnit);
		}

		return true;
	}

}
