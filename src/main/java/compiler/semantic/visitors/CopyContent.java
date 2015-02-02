package compiler.semantic.visitors;

import org.lemsml.model.ComponentType;
import org.lemsml.model.Constant;
import org.lemsml.model.Target;
import org.lemsml.model.Unit;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

import extended.Component;
import extended.Dimension;
import extended.Lems;

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
	public CopyContent(Lems lems)
	{
		super(new DepthFirstTraverserImpl<Throwable>(), new BaseVisitor<Boolean, Throwable>());
		resolvedLems = lems;
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
	public Boolean visit(Component component)
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
	public Boolean visit(Dimension dimension)
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
