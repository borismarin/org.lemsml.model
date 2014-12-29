package org.lemsml.model.test;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.lemsml.model.Lems;
import org.xml.sax.SAXException;

import extended.ExtObjectFactory;

public class LemsTestUtils {
	public File schema;
	public File document;
	
	public LemsTestUtils(File lems_doc, File lems_schema) {
		this.schema = lems_schema;
		this.document = lems_doc;
	}

	boolean validate() {
		boolean ret = false;
		try {
			System.out.print("Validating file " + document.getName()
					+ " against schema " + schema.getName() + "... ");
			StreamSource src = new StreamSource(document);
			parseSchema().newValidator().validate(src);
			ret = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Valid!!");
		return ret;
	}

	Lems unmarshall() {
		Lems lems = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(Lems.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(parseSchema());
			unmarshaller.setProperty("com.sun.xml.internal.bind.ObjectFactory",
					new ExtObjectFactory());
			lems = (Lems) unmarshaller.unmarshal(document);
		} catch (JAXBException e) {
			System.out.println("Problems unmarshalling document " + document.getName());
			e.printStackTrace();
		}

		return lems;
	}
	
	File applyXSLT(File canonical_xslt) {
		System.out.print("Applying XSLT " + canonical_xslt.getName()
				+ " to file " + this.document.getName() + "... ");
		//TODO: Change output path		
		File outputFile = new File("output.xml");
		TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(canonical_xslt);
        Transformer transformer;
		try {
			transformer = factory.newTransformer(xslt);
			Source text = new StreamSource(this.document);
			transformer.transform(text, new StreamResult(outputFile));
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
		
		return outputFile;
	}

	private Schema parseSchema() {
		Schema parsedSchema = null;
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			parsedSchema = sf.newSchema(schema);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			System.out.println("Problems parsing schema " + schema.getName());
			e.printStackTrace();
		}
		return parsedSchema;
	}

}