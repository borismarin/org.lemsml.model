package org.lemsml.model.compiler.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.lemsml.model.Lems;

public class LEMSXMLWriter {
	private static Marshaller marshaller;

	public static void marshall(Lems document, File outFile)
			throws FileNotFoundException, JAXBException {
		OutputStream os = new FileOutputStream(outFile);

		JAXBContext jc = JAXBContext.newInstance(Lems.class);

		JAXBElement<Lems> element = new JAXBElement<Lems>(new QName(
				"http://www.neuroml.org/lems/0.9.0", "Lems"), Lems.class,
				document);
		marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(element, os);

	}

}
