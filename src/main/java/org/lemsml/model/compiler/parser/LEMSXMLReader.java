package org.lemsml.model.compiler.parser;

import java.io.File;
import java.net.URL;

import org.lemsml.model.extended.ExtObjectFactory;
import org.lemsml.model.extended.Lems;

public class LEMSXMLReader
{
	static ExtObjectFactory objFactory = new ExtObjectFactory();
	
	public static Lems unmarshall(File document, File schema) {
		return JaxbXMLReader.<Lems>unmarshall(document, schema, objFactory);
	}

	public static Lems unmarshall(URL document, File schema) {
		return JaxbXMLReader.<Lems>unmarshall(document, schema, objFactory);
	}
	


}
