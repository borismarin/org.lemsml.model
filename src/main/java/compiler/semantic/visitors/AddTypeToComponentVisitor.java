package compiler.semantic.visitors;

import org.lemsml.model.ComponentType;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

import extended.Component;
import extended.Lems;

/**
 * @author borismarin
 *
 */
public class AddTypeToComponentVisitor extends TraversingVisitor<Boolean, Throwable>
{

	private Lems lems;

	/**
	 * @param lems
	 */
	public AddTypeToComponentVisitor(Lems lems)
	{
		super(new DepthFirstTraverserImpl<Throwable>(), new BaseVisitor<Boolean, Throwable>());
		this.lems = lems;
	}

	/* (non-Javadoc)
	 * @see org.lemsml.visitors.TraversingVisitor#visit(org.lemsml.model.Component)
	 */
	@Override
	public Boolean visit(Component comp) throws Throwable
	{
		ComponentType ctToSet = this.lems.getComponentTypeByName(comp.getType());
		comp.setComponentType(ctToSet);
		return true;
	}

	/**
	 * @return
	 */
	public Lems getLems()
	{
		return lems;
	}

	/**
	 * @param lems
	 */
	public void setLems(Lems lems)
	{
		this.lems = lems;
	}

}
