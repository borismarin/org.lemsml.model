package org.lemsml.model.compiler.backend;

import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

/**
 * @author borismarin
 *
 */
public class BuildEvaluationContext extends
		TraversingVisitor<Boolean, Throwable> {

	private Lems lems;

	/**
	 * @param lems
	 */
	public BuildEvaluationContext(Lems lems) {
		super(new DepthFirstTraverserImpl<Throwable>(),
				new BaseVisitor<Boolean, Throwable>());
		this.lems = lems;
	}

	@Override
	public Boolean visit(Component comp) throws Throwable {
		// Evaluates derived parameter expressions
		// TODO: should we do dim analysis here?
		DerivedParameterResolver derParVisitor = new DerivedParameterResolver(
				comp);
		derParVisitor.setTraverseFirst(true);
		comp.accept(derParVisitor);

		return true;
	}

}
