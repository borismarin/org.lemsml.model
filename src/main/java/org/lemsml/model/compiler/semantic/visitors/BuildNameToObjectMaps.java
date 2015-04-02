package org.lemsml.model.compiler.semantic.visitors;

import org.lemsml.model.ComponentType;
import org.lemsml.model.Constant;
import org.lemsml.model.compiler.utils.UOMUtils;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.Dimension;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.Unit;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

/**
 * @author borismarin
 *
 */
public class BuildNameToObjectMaps extends
		TraversingVisitor<Boolean, Throwable> {

	private Lems lems;

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
		this.lems.registerComponentId(c.getId(), c);
		return true;
	}

	@Override
	public Boolean visit(ComponentType ct) throws Throwable {
		this.lems.registerComponentTypeName(ct.getName(), ct);
		return true;
	}

	@Override
	public Boolean visit(Constant ctt) throws Throwable {
		this.lems.registerConstantName(ctt.getName(), ctt);
		return true;
	}

	@Override
	public Boolean visit(Dimension dimension) throws Throwable {
		dimension.setDimension(UOMUtils.LemsDimensionToUOM(dimension));
		lems.getNameToDimension().put(dimension.getName(),
				dimension.getDimension());
		return true;
	}
	
	@Override
	public Boolean visit(Unit unit) throws Throwable {
		javax.measure.Unit<?> dim = lems.getNameToDimension().get(
				unit.getDimension());

		// That's why we don't store a javax.measure.Dimension in nameToDim:
		// dimensions can't be operated with
		javax.measure.Unit<?> uomUnit = dim.multiply(Math.pow(10, unit
				.getPower().intValue()));
		uomUnit = uomUnit.shift(unit.getOffset());

		unit.setUnit(uomUnit);
		lems.getSymbolToUnit().put(unit.getSymbol(), uomUnit);
		return true;
	}


	public Lems getLems() {
		return lems;
	}

	public void setLems(Lems lems) {
		this.lems = lems;
	}

}
