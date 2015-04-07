package org.lemsml.model.compiler.semantic.visitors;

import javax.xml.namespace.QName;

import org.lemsml.model.ComponentType;
import org.lemsml.model.LEMSCompilerError;
import org.lemsml.model.Parameter;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.ParameterInstance;
import org.lemsml.model.extended.PhysicalQuantity;
import org.lemsml.model.extended.PhysicalQuantityAdapter;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

/**
 * @author borismarin
 *
 */
public class AddParameterValuesToComponent extends
		TraversingVisitor<Void, Throwable> {

	private Lems lems;

	public AddParameterValuesToComponent(Lems lems) {
		super(new DepthFirstTraverserImpl<Throwable>(),
				new BaseVisitor<Void, Throwable>());
		this.lems = lems;
	}

	@Override
	public Void visit(Component comp) throws Throwable {

		ComponentType type = lems.getComponentTypeByName(comp.getType());

		// Reads parameters (defined by the ComponentType) from the attributes
		// TODO: shouldn't comp.registerParameter be responsible for checking
		// the comptype to see if the par is allowed?
		for (Parameter parDef : type.getParameters()) {
			String pName = parDef.getName();
			QName qualiPName = new QName(pName);
			if (comp.getOtherAttributes().keySet().contains(qualiPName)) {
				String valueUnit = comp.getOtherAttributes().get(qualiPName);
				// TODO: review ParameterValue, looking overcomplicated
				PhysicalQuantity quant = new PhysicalQuantityAdapter()
						.unmarshal(valueUnit);
				quant.setUnit(lems.getUnitBySymbol(quant.getUnitSymbol()));
				ParameterInstance parInst = new ParameterInstance();
				parInst.setValue(quant);
				parInst.setDefinition(parDef);
				comp.registerParameter(parDef, parInst);
			} else {
				throw new LEMSCompilerException("Components of type "
						+ comp.getType() + " must define parameter " + pName,
						LEMSCompilerError.RequiredParameterUndefined);
			}
		}
		// TODO: handle spurious attributes ie. those that don't correspond to
		// anything in the ComponentType definition
		return null;
	}

}
