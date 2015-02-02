package compiler.semantic.visitors;

import org.lemsml.model.ComponentType;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

import extended.Lems;

/**
 * @author borismarin
 *
 */
public class BuildNameComponentTypeMapVisitor extends TraversingVisitor<Boolean, Throwable>
{

	private Lems lems;

	/**
	 * @param lems
	 */
	public BuildNameComponentTypeMapVisitor(Lems lems)
	{
		super(new DepthFirstTraverserImpl<Throwable>(), new BaseVisitor<Boolean, Throwable>());
		this.lems = lems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lemsml.visitors.TraversingVisitor#visit(org.lemsml.model.ComponentType)
	 */
	@Override
	public Boolean visit(ComponentType ct) throws Throwable
	{
		this.lems.registerComponentTypeName(ct.getName(), ct);
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
