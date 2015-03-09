package org.lemsml.model.compiler.semantic.visitors;

import org.lemsml.model.ComponentType;
import org.lemsml.model.Constant;
import org.lemsml.model.Target;
import org.lemsml.model.Unit;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

/**
 * @author matteocantarelli
 * @author borismarin
 *
 */
public class CopyContentVisitor extends TraversingVisitor<Boolean, Throwable>
{
	
	private Lems resolvedLems;

	/**
	 * 
	 */
	public CopyContentVisitor(org.lemsml.model.extended.Lems lems)
	{
		super(new DepthFirstTraverserImpl<Throwable>(), new BaseVisitor<Boolean, Throwable>());
		resolvedLems=lems;
	}

	
	@Override
	public Boolean visit(Constant constant)
	{
		resolvedLems.getConstant().add(constant);
		return true;

	}

	@Override
	public Boolean visit(ComponentType componentType)
	{
		resolvedLems.getComponentType().add(componentType);
		return true;
	}

	@Override
	public Boolean visit(org.lemsml.model.extended.Component component)
	{
		resolvedLems.getComponent().add(component);
		return true;
	}

	@Override
	public Boolean visit(Target target)
	{
		resolvedLems.getTarget().add(target);
		return true;
	}

	@Override
	public Boolean visit(org.lemsml.model.extended.Dimension dimension)
	{
		resolvedLems.getDimension().add(dimension);
		return true;
	}

	@Override
	public Boolean visit(Unit unit)
	{
		resolvedLems.getUnit().add(unit);
		return true;
	}
}
