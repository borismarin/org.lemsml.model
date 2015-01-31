package org.lemsml.model.test;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import compiler.parser.LEMSParser;

public class BuildNameComponentTypeMapVisitorTest extends BaseTest {

	private File schema;
	private File lemsdoc;

	@Before
	public void setUp() {
		schema = getLocalFile("/Schemas/LEMS_v0.9.0.xsd");
		lemsdoc = getLocalFile("/examples/nml/HindmarshRose3d.xml");
	}

	@Test
	public void test() throws Throwable {

		LEMSParser parser = new LEMSParser(lemsdoc, schema);

		//TODO parser.populateNameComponentTypeHM();
	}

}
