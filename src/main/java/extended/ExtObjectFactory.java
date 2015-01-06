package extended;

import javax.xml.bind.annotation.XmlRegistry;

import org.lemsml.model.ObjectFactory;

@XmlRegistry
public class ExtObjectFactory extends ObjectFactory {
	@Override
	public Parameter createParameter() {
		return new Parameter();
	}

	@Override
	public Component createComponent() {
		return new Component();
	}
}