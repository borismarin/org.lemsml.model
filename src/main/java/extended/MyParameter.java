package extended;

import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.XmlTransient;

import org.lemsml.model.ObjectFactory;
import org.lemsml.model.Parameter;

@XmlTransient
public class MyParameter extends Parameter {
	

}

@XmlRegistry
class ObjectFactoryEx extends ObjectFactory {
  @Override
public
  MyParameter createParameter() {
    return new MyParameter();
  }
}
