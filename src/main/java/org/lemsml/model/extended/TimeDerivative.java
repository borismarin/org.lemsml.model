package org.lemsml.model.extended;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.lemsml.model.extended.interfaces.INamedValueDefinition;
import org.lemsml.visitors.Visitor;

/**
 * @author borismarin
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
public class TimeDerivative extends org.lemsml.model.TimeDerivative implements INamedValueDefinition{

	public String getName() {
		return "d" + this.getVariable() + "_dt";
	}


	@Override
	public <R, E extends Throwable> R accept(Visitor<R, E> aVisitor) throws E {
		return aVisitor.visit(this);
	}

}
