package org.lemsml.model.test;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.lemsml.model.compiler.LEMSCompilerFrontend;
import org.lemsml.model.compiler.parser.LEMSXMLWriter;
import org.lemsml.model.compiler.parser.XMLUtils;
import org.lemsml.model.extended.Lems;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * @author borismarin
 *
 */
public class RoundtripTest extends BaseTest {

	private File schema;
	private File pendLemsFile;
	private Lems compiledLems;

	@Before
	public void setUp() throws Throwable {
		schema = getLocalFile("/Schemas/LEMS_v0.9.0.xsd");
		pendLemsFile = getLocalFile("/examples/opensourcechaos/standalone_pend.xml");
		LEMSCompilerFrontend compiler = new LEMSCompilerFrontend(pendLemsFile,
				schema);
		compiledLems = compiler.generateLEMSDocument();
	}

	@Test
	public void validate() {
		assertTrue(XMLUtils.validate(pendLemsFile, schema));
	}

	@Test
	public void testRoundTrip() throws Throwable {
		LEMSCompilerFrontend compiler = new LEMSCompilerFrontend(pendLemsFile,
				schema);
		compiledLems = compiler.generateLEMSDocument();
		File tmpFile = File.createTempFile("pend", ".xml");
		LEMSXMLWriter.marshall(compiledLems, tmpFile);
		System.out.println(Files.toString(tmpFile, Charsets.UTF_8));

	}

}
