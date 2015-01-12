package parser;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import extended.ExtObjectFactory;
import extended.Lems;

public class LemsXmlUtils {

	public static Lems unmarshall(File document, File schema) {
		Lems lems = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(ExtObjectFactory.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(XmlFileUtils.parseSchema(schema));
			unmarshaller.setProperty("com.sun.xml.bind.ObjectFactory",
					new ExtObjectFactory());
			lems = (Lems) unmarshaller.unmarshal(document);
		} catch (JAXBException e) {
			System.out.println("Problems unmarshalling document "
					+ document.getName());
			e.printStackTrace();
		}

		return lems;
	}

}