package org.lemsml.model.compiler.semantic.visitors;

import javax.measure.Unit;

import org.lemsml.model.compiler.utils.UOMUtils;
import org.lemsml.model.extended.Dimension;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

/**
 * @author borismarin
 *
 */
public class AddUOMToDimension extends TraversingVisitor<Boolean, Throwable> {

	private Lems lems;

	public AddUOMToDimension(Lems lems) {
		super(new DepthFirstTraverserImpl<Throwable>(),
				new BaseVisitor<Boolean, Throwable>());
		this.lems = lems;
	}

	@Override
	public Boolean visit(Dimension dimension) throws Throwable {
		dimension
				.setDimension((Unit<?>) UOMUtils.LemsDimensionToUOM(dimension));
		lems.registerDimensionName(dimension.getName(),
				dimension.getDimension());
		return true;
	}

}
