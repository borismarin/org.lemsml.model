package compiler.parser;

import java.io.File;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import extended.ExtObjectFactory;

/**
 * @author borismarin
 *
 */
public class JaxbXMLReader 
{

	/**
	 * @param document
	 * @param schema
	 * @return 
	 * @return
	 */
	public static <T> T unmarshall(File document, File schema)
	{

		T unmarshalledDoc = null;
		Unmarshaller unmarshaller = getUnmarshaller(schema);
		try
		{
			unmarshalledDoc = (T) unmarshaller.unmarshal(document);
		}
		catch(JAXBException e)
		{
			System.out.println("Problems unmarshalling document " + document.getName());
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
	public static <T> T unmarshall(URL document, File schema)
	{

		T lems = null;
		Unmarshaller unmarshaller = getUnmarshaller(schema);
		try
		{
			lems = (T) unmarshaller.unmarshal(document);
		}
		catch(JAXBException e)
		{
			System.out.println("Problems unmarshalling url " + document);
			e.printStackTrace();
		}

		return lems;
	}

	/**
	 * @param schema
	 * @return
	 */
	private static Unmarshaller getUnmarshaller(File schema)
	{
		Unmarshaller unmarshaller = null;
		try
		{
			JAXBContext jc = JAXBContext.newInstance(ExtObjectFactory.class);
			unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(XMLUtils.parseSchema(schema));
			unmarshaller.setProperty("com.sun.xml.bind.ObjectFactory", new ExtObjectFactory());
		}
		catch(JAXBException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return unmarshaller;
	}

}