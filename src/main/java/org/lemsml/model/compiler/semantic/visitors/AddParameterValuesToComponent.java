package org.lemsml.model.compiler.semantic.visitors;

import javax.xml.namespace.QName;

import org.lemsml.model.ComponentType;
import org.lemsml.model.LEMSCompilerError;
import org.lemsml.model.Parameter;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

public class AddParameterValuesToComponent extends
		TraversingVisitor<Void, Throwable> {

	private Lems lems;

	public AddParameterValuesToComponent(Lems lems) {
		super(new DepthFirstTraverserImpl<Throwable>(),
				new BaseVisitor<Void, Throwable>());
		this.lems = lems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lemsml.visitors.TraversingVisitor#visit(org.lemsml.model.Component)
	 */
	@Override
	public Void visit(Component comp) throws Throwable {

		ComponentType type = lems.getComponentTypeByName(comp.getType());

		for (Parameter p : type.getParameters()) {
			if (!comp.getOtherAttributes().keySet()
					.contains(new QName(p.getName()))) {
				throw new LEMSCompilerException("Components of type "
						+ comp.getType() + " must define parameter "
						+ p.getName(),
						LEMSCompilerError.RequiredParameterUndefined);
			}
		}
		return null;
	}

}
