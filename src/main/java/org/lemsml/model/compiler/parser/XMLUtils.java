package org.lemsml.model.compiler.parser;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;


/**
 * @author borismarin
 *
 */
public class XMLUtils {
	private static final Logger logger =  LoggerFactory
			.getLogger(XMLUtils.class);

	/**
	 * @param document
	 * @param schema
	 * @return
	 */
	public static boolean validate(File document, File schema) {
		boolean ret = false;
		try {
			logger.info("Validating file {} against schema {} ...",
					document.getName(), schema.getName());
			StreamSource src = new StreamSource(document);
			XMLUtils.parseSchema(schema).newValidator().validate(src);
			ret = true;
			logger.info("\t Valid!!");
		} catch (SAXException e) {
			// e.printStackTrace();
			logger.error("\t Invalid!!, cause:\n\t" + e.getMessage());
		} catch (IOException e) {
			// e.printStackTrace();
			logger.error("Can't open schema file!!!");
		}

		return ret;
	}

	/**
	 * @param document
	 * @param transformation
	 * @return
	 */
	public static File transform(File document, File transformation) {
		logger.info("Applying XSLT " + transformation.getName() + " to file "
				+ document.getName() + "... ");
		String orig_name = document.getPath();
		String transf_name = orig_name.substring(0, orig_name.lastIndexOf('.'))
				+ "_transformed.xml";
		File outputFile = new File(transf_name);
		TransformerFactory factory = TransformerFactory.newInstance();
		Source xslt = new StreamSource(transformation);
		Transformer transformer;
		try {
			transformer = factory.newTransformer(xslt);
			Source text = new StreamSource(document);
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

	/**
	 * @param schema
	 * @return
	 */
	private static Schema parseSchema(File schema) {
		Schema parsedSchema = null;
		SchemaFactory sf = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
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
