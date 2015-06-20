package org.lemsml.model.compiler.parser;

import java.io.File;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * @author borismarin
 *
 */
public class JaxbXMLReader {

	/**
	 * @param document
	 * @param schema
	 * @return
	 * @return
	 */
	@SuppressWarnings("unchecked")
	static <T> T unmarshall(File document, File schema, Object objFactory) {

		T unmarshalledDoc = null;
		Unmarshaller unmarshaller = getUnmarshaller(objFactory);
		try {
			unmarshalledDoc = (T) unmarshaller.unmarshal(document);
		} catch (JAXBException e) {
			System.out.println("Problems unmarshalling document "
					+ document.getName());
			e.printStackTrace();
		}

		return unmarshalledDoc;
	}

	/**
	 * @param document
	 * @param schema
	 * @return
	 * @return
	 */
	@SuppressWarnings("unchecked")
	static <T> T unmarshall(URL document, File schema, Object objFactory) {

		T unmarshalledDoc = null;
		Unmarshaller unmarshaller = getUnmarshaller(objFactory);
		try {
			unmarshalledDoc = (T) unmarshaller.unmarshal(document);
		} catch (JAXBException e) {
			System.out.println("Problems unmarshalling url " + document);
			e.printStackTrace();
		}

		return unmarshalledDoc;
	}

	public static Unmarshaller getUnmarshaller(Object objFactory) {
		Unmarshaller unmarshaller = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(objFactory.getClass());
			unmarshaller = jc.createUnmarshaller();
			unmarshaller.setProperty("com.sun.xml.bind.ObjectFactory",
					objFactory);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return unmarshaller;
	}

}