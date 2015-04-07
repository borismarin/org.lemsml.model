package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lemsml.model.ComponentType;
import org.lemsml.model.Parameter;
import org.lemsml.model.compiler.LEMSCompilerFrontend;
import org.lemsml.model.compiler.parser.LEMSXMLReader;
import org.lemsml.model.compiler.parser.XMLUtils;
import org.lemsml.model.extended.Lems;

/**
 * @author borismarin
 *
 */
public class HindmarshRoseTest extends BaseTest {

	private File lemsSchemaFile;
	private File hindMarshRoseCompTypeFile;
	private File hindMarshRoseSimFile;
	private File nonCanonHindMarshRoseSimFile;

	@Before
	public void setUp() {
		lemsSchemaFile = getLocalFile("/Schemas/LEMS_v0.9.0.xsd");
		hindMarshRoseCompTypeFile = getLocalFile("/examples/nml/HindmarshRose3d.xml");
		hindMarshRoseSimFile = getLocalFile("/examples/nml/Run_Chaotic_HindmarshRose.xml");
		nonCanonHindMarshRoseSimFile = getLocalFile("/examples/nml/NonCanon_Run_Chaotic_HindmarshRose.xml");
	}

	@Test
	public void validateComponent() {
		assertTrue(XMLUtils.validate(hindMarshRoseCompTypeFile, lemsSchemaFile));
	}

	@Test
	public void validateSimulation() {
		assertTrue(XMLUtils.validate(hindMarshRoseSimFile, lemsSchemaFile));
	}

	@Test
	public void testCanonicalize() {
		File xslt = getLocalFile("/Schemas/canonical.xslt");
		System.out
				.println("Asserting that a noncanonical file fails to validate...");
		assertFalse(XMLUtils.validate(nonCanonHindMarshRoseSimFile,
				lemsSchemaFile));

		System.out
				.println("Asserting that the canonicalized version validates...");
		File transformed = XMLUtils.transform(nonCanonHindMarshRoseSimFile,
				xslt);
		assertTrue(XMLUtils.validate(transformed, lemsSchemaFile));
	}

	private void validateHRComponentType(ComponentType hr_candidate) {

		String desc = hr_candidate.getDescription();
		assertEquals(
				desc,
				"     The Hindmarsh Rose model is a simplified point cell model which     captures complex firing patterns of single neurons, such as     periodic and chaotic bursting. It in a fast spiking subsystem,     which is a generalization of the Fitzhugh-Nagumo system, coupled     to a slower subsystem which allows the model to fire bursts. The     dynamical variables x,y,z correspond to the membrane potential, a     recovery variable, and a slower adaptation current, respectively.     ");

		List<Parameter> parList = hr_candidate.getParameters();
		assertEquals(parList.get(0).getDescription(),
				"cubic term in x         nullcline");
	}

	@Test
	public void testUnmarshallingComponent() {

		Lems lems = LEMSXMLReader.unmarshall(hindMarshRoseCompTypeFile,
				lemsSchemaFile);
		ComponentType hindMarshRoseCompType = lems.getComponentTypes().get(0);
		validateHRComponentType(hindMarshRoseCompType);

	}

	@Test
	public void testParsing() throws Throwable {

		LEMSCompilerFrontend compiler = new LEMSCompilerFrontend(
				hindMarshRoseSimFile, lemsSchemaFile);

		Lems lemsDoc = compiler.generateLEMSDocument();

		ComponentType hindRoseCompType = lemsDoc
				.getComponentTypeByName("hindmarshRoseCell");
		validateHRComponentType(hindRoseCompType);

	}

}
