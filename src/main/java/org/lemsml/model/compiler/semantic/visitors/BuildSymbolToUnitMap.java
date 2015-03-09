package org.lemsml.model.compiler.semantic.visitors;

import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.Unit;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

/**
 * @author borismarin
 *
 */
public class BuildSymbolToUnitMap extends TraversingVisitor<Boolean, Throwable>
{

	private Lems lems;

	public BuildSymbolToUnitMap(Lems lems)
	{
		super(new DepthFirstTraverserImpl<Throwable>(), new BaseVisitor<Boolean, Throwable>());
		this.lems = lems;
	}

	@Override
	public Boolean visit(Unit unit) throws Throwable

	{
		javax.measure.Unit<?> dim = lems.getNameToDimension().get(unit.getDimension());

		// That's why we don't store a javax.measure.Dimension in nameToDim: dimensions can't be
		// operated with
		javax.measure.Unit<?> uomUnit = dim.multiply(Math.pow(10, unit.getPower().intValue()));
		uomUnit = uomUnit.shift(unit.getOffset());

		unit.setUnit(uomUnit);
		lems.getSymbolToUnit().put(unit.getSymbol(), uomUnit);
		return true;
	}

	public Lems getLems()
	{
		return lems;
	}

}
