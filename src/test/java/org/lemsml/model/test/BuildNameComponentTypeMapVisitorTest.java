package org.lemsml.model.test;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import parser.LemsParser;

public class BuildNameComponentTypeMapVisitorTest extends BaseTest {

	private File schema;
	private File hr_def;

	@Before
	public void setUp() {
		schema = getLocalFile("/Schemas/LEMS_v0.8.0.xsd");
		hr_def = getLocalFile("/examples/HindmarshRose3d.xml");
	}

	@Test
	public void test() throws Throwable {

		LemsParser parser = new LemsParser(hr_def, schema);

		parser.populateNameComponentTypeHM();
	}

}
