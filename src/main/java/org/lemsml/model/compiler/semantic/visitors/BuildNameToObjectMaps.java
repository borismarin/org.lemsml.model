package org.lemsml.model.compiler.semantic.visitors;

import org.lemsml.model.ComponentType;
import org.lemsml.model.Constant;
import org.lemsml.model.compiler.utils.UOMUtils;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.Dimension;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.LemsNode;
import org.lemsml.model.extended.Unit;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author borismarin
 *
 */
public class BuildNameToObjectMaps extends
		TraversingVisitor<Boolean, Throwable> {

	private Lems lems;
	private static final Logger logger = LoggerFactory
			.getLogger(BuildNameToObjectMaps.class);


	private void logRegistration(String identifier, LemsNode n){
		logger.debug(String.format("Registering '%s', defined in %s", n.getClass(), identifier, n.getDefinedIn()));
	}
	
	/**
	 * @param lems
	 */
	public BuildNameToObjectMaps(Lems lems) {
		super(new DepthFirstTraverserImpl<Throwable>(),
				new BaseVisitor<Boolean, Throwable>());
		this.lems = lems;
	}

	@Override
	public Boolean visit(Component c) throws Throwable {
		//logger.debug(String.format("Registering Component ID '%s', defined in %s", c.getId(), c.getDefinedIn()));
		logRegistration(c.getId(), c);
		this.lems.registerComponentId(c.getId(), c);
		return true;
	}

	@Override
	public Boolean visit(ComponentType ct) throws Throwable {
//		logger.debug(String.format("Registering ComponentType '%s', defined in %s", ct.getName(), ct.getDefinedIn()));
		logRegistration(ct.getName(), ct);
		this.lems.registerComponentTypeName(ct.getName(), ct);
		return true;
	}

	@Override
	public Boolean visit(Constant ctt) throws Throwable {
//		logger.debug(String.format("Registering Constant '%s', defined in %s", ctt.getName()));
		logRegistration(ctt.getName(), ctt);
		this.lems.registerConstantName(ctt.getName(), ctt);
		return true;
	}

	@Override
	public Boolean visit(Dimension dimension) throws Throwable {
		dimension.setDimension(UOMUtils.LemsDimensionToUOM(dimension));
//		logger.debug(String.format("Registering Dimension '%s', defined in %s", dimension.getName()));
		logRegistration(dimension.getName(), dimension);
		lems.registerDimensionName(dimension.getName(),
				dimension.getDimension());
		return true;
	}

	@Override
	public Boolean visit(Unit unit) throws Throwable {
		javax.measure.Unit<?> dim = lems
				.getDimensionByName(unit.getDimension());

		// That's why we don't store a javax.measure.Dimension in nameToDim:
		// dimensions can't be operated with
		javax.measure.Unit<?> uomUnit = dim.multiply(Math.pow(10, unit
				.getPower().intValue()));
		uomUnit = uomUnit.shift(unit.getOffset());

		unit.setUnit(uomUnit);
//		logger.debug(String.format("Registering UnitSymbol '%s', defined in %s", unit.getSymbol()));
		logRegistration(unit.getSymbol(), unit);
		lems.registerUnitSymbol(unit.getSymbol(), uomUnit);
		return true;
	}

}
