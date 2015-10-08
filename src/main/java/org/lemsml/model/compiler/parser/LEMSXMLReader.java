package org.lemsml.model.compiler.parser;

import java.io.File;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.lemsml.model.extended.ExtObjectFactory;
import org.lemsml.model.extended.Lems;

public class LEMSXMLReader {
	private static ExtObjectFactory objFactory = new ExtObjectFactory();

	public static Lems unmarshall(File document, File schema) {
		JAXBElement<Lems> je = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(objFactory.getClass());
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setProperty("com.sun.xml.bind.ObjectFactory",
					objFactory);
			je = (JAXBElement<Lems>) unmarshaller.unmarshal(document);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return je.getValue();
		//return JaxbXMLReader.<Lems> unmarshall(document, schema, objFactory);
	}

	static Lems unmarshall(URL document, File schema) {
		return JaxbXMLReader.<Lems> unmarshall(document, schema, objFactory);
	}

}
