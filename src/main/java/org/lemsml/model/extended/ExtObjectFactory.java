package org.lemsml.model.extended;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import org.lemsml.model.ObjectFactory;

/**
 * @author borismarin
 *
 */
@XmlRegistry
public class ExtObjectFactory extends ObjectFactory {

	@Override
	public TimeDerivative createTimeDerivative() {
		return new TimeDerivative();
	}

	@Override
	public Component createComponent() {
		return new Component();
	}

	@Override
	public Dimension createDimension() {
		return new Dimension();
	}

	@Override
	public Unit createUnit() {
		return new Unit();
	}

	@Override
	public Lems createLems() {
		return new Lems();
	}

	@XmlElementDecl(namespace="http://www.neuroml.org/lems/0.9.0", name="Lems")
	public JAXBElement<Lems> createBlah(Lems value) {
        return new JAXBElement<Lems>(new QName("http://www.neuroml.org/lems/0.9.0","Lems"), Lems.class, null, value);
    }
}