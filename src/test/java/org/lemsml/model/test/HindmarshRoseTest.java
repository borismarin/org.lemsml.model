package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lemsml.model.ComponentType;
import org.lemsml.model.Lems;

import extended.ExtParameter;

/**
 * @author boris
 *
 */
public class HindmarshRoseTest{

	private LemsTestUtils hr_compdef;
	private LemsTestUtils hr_sim;
	private File canonical_xslt;

	private File getLocalFile(String fname) {
		return new File(getClass().getResource(fname).getFile());
	}

	@Before
	public void setUp() {
		File schema = getLocalFile("/Schemas/LEMS_v0.8.0.xsd");
		canonical_xslt = getLocalFile("/Schemas/canonical.xslt");
		hr_compdef = new LemsTestUtils(
				getLocalFile("/examples/HindmarshRose3d.xml"), schema);
		hr_sim = new LemsTestUtils(
				getLocalFile("/examples/Run_Chaotic_HindmarshRose.xml"), schema);
	}

	@Test
	public void validateComponent() {
		assertTrue(hr_compdef.validate());
	}

	@Test
	public void validateSimulation() {
		assertTrue(hr_sim.validate());
	}
	
	@Test
	public void applyXSLT(){
		//TODO: Add assert
		hr_compdef.applyXSLT(canonical_xslt);
	}
	
	
	@Test
	public void testUnmarshallingComponent() {

		Lems lems = hr_compdef.unmarshall();
		ComponentType hrct = lems.getComponentType().get(0);

		String desc = hrct.getDescription();
		assertEquals(
				desc,
				"     The Hindmarsh Rose model is a simplified point cell model which     captures complex firing patterns of single neurons, such as     periodic and chaotic bursting. It in a fast spiking subsystem,     which is a generalization of the Fitzhugh-Nagumo system, coupled     to a slower subsystem which allows the model to fire bursts. The     dynamical variables x,y,z correspond to the membrane potential, a     recovery variable, and a slower adaptation current, respectively.     ");
		List<ExtParameter> extParameterList = hrct.getParameter();

		assertEquals(extParameterList.get(0).getDescription(),
				"cubic term in x         nullcline");
	}
}
