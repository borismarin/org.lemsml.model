package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lemsml.model.DerivedParameter;
import org.lemsml.model.Parameter;
import org.lemsml.model.compiler.LEMSCompilerFrontend;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.Symbol;

import com.google.common.collect.ImmutableMap;

public class SyncTest extends BaseTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testParams() throws Throwable {

		//type Foo defined below, inline with Lems
		Component foo = ((Component) new  Component()
							.withType("Foo"))
							.withParameterValue("p", "1");

		Lems lems = (Lems) new Lems().withComponentTypes(
				(ComponentType) new ComponentType().withName("Foo")
						.withParameters(
								new Parameter().withName("p"))
						.withDerivedParameters(
								new DerivedParameter().withName("twoP")
										.withValueDefinition("2 * p")))
				.withComponents(foo);

		LEMSCompilerFrontend.semanticAnalysis(lems);
		assertEquals(1.0, foo.getScope().evaluate("p"), 1e-12);
	}

	@Test
	public void testSymbolicExpression() throws Throwable {

		File schema = getLocalFile("/Schemas/LEMS_v0.9.0.xsd");
		File lemsDoc = getLocalFile("/examples/expression-resolver-test/nested_expressions.xml");

		Lems compiledLems = new LEMSCompilerFrontend(lemsDoc, schema)
				.generateLEMSDocument();
		Component comp0 = compiledLems.getComponentById("comp0");
		Symbol dv0 = comp0.getScope().resolve("dv0");
		Set<String> independentVariables = dv0.getIndependentVariables();
		assertTrue(independentVariables.contains("x0"));
		Double x = comp0.getScope().evaluate("dv0", new ImmutableMap.Builder<String, Double>().put(
				"x0", 0.).build());
		assertEquals(0, x, 1e-10);

		Component comp1 = compiledLems.getComponentById("comp1");
		Symbol dy1 = comp1.getScope().resolve("dy1_dt");
		Set<String> independentVariables1 = dy1.getIndependentVariables();
		assertTrue(independentVariables1.contains("y1"));
		// TODO: think about what we expect for symb expre expressions which
		// depend on top-level symb exprs (i.e. do we expand "dv0" below?)
		Double dy = comp1.getScope().evaluate("dy1_dt", new ImmutableMap.Builder<String, Double>()
				.put("y1", 1.).put("dv0", 1.).build());
		assertEquals(-1., dy, 1e-10);

	}

}
