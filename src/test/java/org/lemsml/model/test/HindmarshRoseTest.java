package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lemsml.model.ComponentType;

import parser.LemsXmlUtils;
import parser.XmlFileUtils;
import extended.Lems;
import extended.Parameter;

/**
 * @author boris
 *
 */
public class HindmarshRoseTest extends BaseTest{

	private File schema;
	private File hr_comptype;
	private File hr_sim;
	private File hr_noncanon_sim;


	@Before
	public void setUp() {
		schema = getLocalFile("/Schemas/LEMS_v0.8.0.xsd");
		hr_comptype = getLocalFile("/examples/HindmarshRose3d.xml");
		hr_sim = getLocalFile("/examples/Run_Chaotic_HindmarshRose.xml");
		hr_noncanon_sim = getLocalFile("/examples/NonCanon_Run_Chaotic_HindmarshRose.xml");
	}

	@Test
	public void validateComponent() {
		assertTrue(XmlFileUtils.validate(hr_comptype, schema));
	}

	@Test
	public void validateSimulation() {
		assertTrue(XmlFileUtils.validate(hr_sim, schema));
	}
	
	@Test
	public void testCanonicalize(){
		File xslt = getLocalFile("/Schemas/canonical.xslt");
		System.out.println("Asserting that a noncanonical file fails to validate...");
		assertFalse(XmlFileUtils.validate(hr_noncanon_sim, schema));

		System.out.println("Asserting that the canonicalized version validates...");
		File transformed = XmlFileUtils.transform(hr_noncanon_sim, xslt);
		assertTrue(XmlFileUtils.validate(transformed, schema));
	}
	
	
	@Test
	public void testUnmarshallingComponent() {

		Lems lems = LemsXmlUtils.unmarshall(hr_comptype, schema);
		ComponentType hrct = lems.getComponentType().get(0);

		String desc = hrct.getDescription();
		assertEquals(
				desc,
				"     The Hindmarsh Rose model is a simplified point cell model which     captures complex firing patterns of single neurons, such as     periodic and chaotic bursting. It in a fast spiking subsystem,     which is a generalization of the Fitzhugh-Nagumo system, coupled     to a slower subsystem which allows the model to fire bursts. The     dynamical variables x,y,z correspond to the membrane potential, a     recovery variable, and a slower adaptation current, respectively.     ");

		List<Parameter> ParameterList = hrct.getParameter();
		assertEquals(ParameterList.get(0).getDescription(),
				"cubic term in x         nullcline");
	}
}
