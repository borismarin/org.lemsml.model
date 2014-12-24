package extended;

import javax.xml.bind.annotation.XmlRegistry;

import org.lemsml.model.ObjectFactory;

@XmlRegistry
class ExtObjectFactory extends ObjectFactory {
	@Override
	public ExtParameter createParameter() {
		return new ExtParameter();
	}

	@Override
	public ExtComponent createComponent() {
		return new ExtComponent();
	}
}