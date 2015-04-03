package org.lemsml.model.compiler.semantic;

import javax.xml.namespace.QName;

import org.lemsml.model.ComponentType;
import org.lemsml.model.Parameter;
import org.lemsml.model.exceptions.LEMSParserException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

public class ComponentDefinitionChecker extends
		TraversingVisitor<Void, Throwable> {

	private Lems lems;

	/**
	 * @param lems
	 */
	public ComponentDefinitionChecker(Lems lems) {
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
		if (null == type) {
			throw new LEMSParserException(
					"Trying to build Component of unknow type "
							+ comp.getType());
		}

		for (Parameter p : type.getParameters()) {
			if (!comp.getOtherAttributes().keySet().contains(new QName(p.getName()))) {
				throw new LEMSParserException("Components of type "
						+ comp.getType() + " must define parameter "
						+ p.getName());
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	public Lems getLems() {
		return lems;
	}

	/**
	 * @param lems
	 */
	public void setLems(Lems lems) {
		this.lems = lems;
	}

}
