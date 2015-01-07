package org.lemsml.model.test;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import parser.LemsParser;
import visitors.AddTypeToComponentVisitor;
import extended.Component;

public class AddTypeToComponentVisitorTest extends BaseTest{

	private File schema;
	private File lemsdoc;

	@Before
	public void setUp() {
		schema = getLocalFile("/Schemas/LEMS_v0.8.0.xsd");
		lemsdoc = getLocalFile("/examples/opensourcechaos/standalone_pend.xml");
	}
	
	@Test
	public void testComponentTypeVisitor() throws Throwable {
		
		LemsParser parser = new LemsParser(lemsdoc, schema);
		parser.populateNameComponentTypeHM();
		
		AddTypeToComponentVisitor<Void> ctv = new AddTypeToComponentVisitor<Void>(parser.getLems());
		Component pend = parser.getLems().getComponent().get(0);
	    pend.accept(ctv);	
	}
	

}
