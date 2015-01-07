package org.lemsml.model.test;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.lemsml.model.ComponentType;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

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

		BuildNameComponentTypeMapVisitor<Lems> nameComptypeVisitor = new BuildNameComponentTypeMapVisitor<Lems>(
				lems);
		TraversingVisitor<Lems, Throwable> tv = new TraversingVisitor<Lems, Throwable>(
				new DepthFirstTraverserImpl<Throwable>(), nameComptypeVisitor);
		tv.setTraverseFirst(true);
		lems.accept(tv);

		Map<String, ComponentType> nameTypeMap = nameComptypeVisitor.getLems().getComponentTypesByName();
		for (Entry<String, ComponentType> entry : nameTypeMap.entrySet()) {
			System.out.println(entry.getKey() + " : "
					+ entry.getValue().getClass().toString());

		}
	}

}
