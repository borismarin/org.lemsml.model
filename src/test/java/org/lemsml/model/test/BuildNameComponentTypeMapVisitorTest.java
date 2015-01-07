package org.lemsml.model.test;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import parser.LemsParser;
import parser.LemsXmlUtils;
import extended.Lems;

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
		Lems lems = LemsXmlUtils.unmarshall(hr_def, schema);
		LemsParser parser = new LemsParser(schema, hr_def);

		parser.populateNameComponentTypeHM(lems);
	}

	

}
