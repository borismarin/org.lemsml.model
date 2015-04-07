package org.lemsml.model.compiler.semantic.visitors;

import javax.xml.namespace.QName;

import org.lemsml.model.ComponentType;
import org.lemsml.model.LEMSCompilerError;
import org.lemsml.model.Parameter;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.ParameterValue;
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

		// Reads parameters (defined by the ComponentType) from the attributes
		for (Parameter p : type.getParameters()) {
			String pName = p.getName();
			QName qualiPName = new QName(pName);
			if (comp.getOtherAttributes().keySet().contains(qualiPName)) {
				String valueUnit = comp.getOtherAttributes().get(qualiPName);
				// TODO: not sure this is the best container
				ParameterValue pVal = new ParameterValue(p, valueUnit);
				pVal.getValue().setUnit(lems.getUnitBySymbol(pVal.getValue().getUnitSymbol()));
				comp.registerParameter(p, pVal);
			} else {
				throw new LEMSCompilerException("Components of type "
						+ comp.getType() + " must define parameter " + pName,
						LEMSCompilerError.RequiredParameterUndefined);
			}
		}
		// TODO: handle spurious attributes ie. those that don't correspond to
		// anything
		// in the ComponentType definition
		return null;
	}

}
