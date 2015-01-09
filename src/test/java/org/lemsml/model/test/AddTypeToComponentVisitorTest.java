package org.lemsml.model.test;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.lemsml.model.ComponentType;

import parser.LemsParser;
import extended.Component;

public class AddTypeToComponentVisitorTest extends BaseTest {

	private File schema;
	private File lemsdoc;

	@Before
	public void setUp() {
		schema = getLocalFile("/Schemas/LEMS_v0.9.0.xsd");
		lemsdoc = getLocalFile("/examples/opensourcechaos/standalone_pend.xml");
	}

	@Test
	public void testComponentTypeVisitor() throws Throwable {

		LemsParser parser = new LemsParser(lemsdoc, schema);

		// Creates the {String name : ComponentType type} HM used during parsing
		parser.populateNameComponentTypeHM();
		System.out.println("##### Generated name:ComponentType HM");
		Map<String, ComponentType> nameTypeMap = parser.getLems().getComponentTypesByNameHM();
		for (Entry<String, ComponentType> entry : nameTypeMap.entrySet()) {
			System.out.println(entry.getKey() + " : "
					+ entry.getValue().getClass().toString() + " " + entry.getValue().getName());
		}

		// Adds the corresponding ComponentType to each Component
		parser.decorateComponentsWithType();
		System.out.println("##### Decorated Components with type");
		for (Component comp : parser.getLems().getComponent()) {
			System.out.println(comp + " : " + comp.get_ComponentType());
		}

	}

}
