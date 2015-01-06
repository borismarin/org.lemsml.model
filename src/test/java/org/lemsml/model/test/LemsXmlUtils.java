package org.lemsml.model.test;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.lemsml.model.Lems;

import extended.ExtObjectFactory;

public class LemsXmlUtils {

	static Lems unmarshall(File document, File schema) {
		Lems lems = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(Lems.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(XmlFileUtils.parseSchema(schema));
			unmarshaller.setProperty("com.sun.xml.internal.bind.ObjectFactory",
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