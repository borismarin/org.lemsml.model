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
public class CopyContent extends TraversingVisitor<Boolean, Throwable>
{
	
	private Lems resolvedLems;

	/**
	 * 
	 */
	public CopyContent(org.lemsml.model.extended.Lems lems)
	{
		super(new DepthFirstTraverserImpl<Throwable>(), new BaseVisitor<Boolean, Throwable>());
		resolvedLems=lems;
	}

	
	@Override
	public Boolean visit(Constant constant)
	{
		resolvedLems.getConstants().add(constant);
		return true;

	}

	@Override
	public Boolean visit(ComponentType componentType)
	{
		resolvedLems.getComponentTypes().add(componentType);
		return true;
	}

	@Override
	public Boolean visit(org.lemsml.model.extended.Component component)
	{
		resolvedLems.getComponents().add(component);
		return true;
	}

	@Override
	public Boolean visit(Target target)
	{
		resolvedLems.getTargets().add(target);
		return true;
	}

	@Override
	public Boolean visit(org.lemsml.model.extended.Dimension dimension)
	{
		resolvedLems.getDimensions().add(dimension);
		return true;
	}

	@Override
	public Boolean visit(Unit unit)
	{
		resolvedLems.getUnits().add((org.lemsml.model.extended.Unit) unit);
		return true;
	}
}
