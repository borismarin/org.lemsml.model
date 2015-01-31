package parser;

import java.io.File;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import extended.ExtObjectFactory;
import extended.Lems;

public class LemsXmlUtils {

	public static Lems unmarshall(File document, File schema) {
		
		Lems lems = null;
		Unmarshaller unmarshaller = getUnmarshaller(schema);
		try {
			lems = (Lems) unmarshaller.unmarshal(document);
		} catch (JAXBException e) {
			System.out.println("Problems unmarshalling document "
					+ document.getName());
			e.printStackTrace();
		}

		return lems;
	}
	
	public static Lems unmarshall(URL document, File schema) {
		
		Lems lems = null;
		Unmarshaller unmarshaller = getUnmarshaller(schema);
		try {
			lems = (Lems) unmarshaller.unmarshal(document);
		} catch (JAXBException e) {
			System.out.println("Problems unmarshalling url "
					+ document);
			e.printStackTrace();
		}

		return lems;
	}

	private static Unmarshaller getUnmarshaller(File schema) {
		Unmarshaller unmarshaller = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(ExtObjectFactory.class);
			unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(XmlFileUtils.parseSchema(schema));
			unmarshaller.setProperty("com.sun.xml.bind.ObjectFactory",
					new ExtObjectFactory());
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return unmarshaller;
	}
	
	

}