package org.lemsml.model.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lemsml.model.DerivedParameter;
import org.lemsml.model.DerivedVariable;
import org.lemsml.model.compiler.ISymbol;
import org.lemsml.model.compiler.LEMSCompilerFrontend;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.SymbolicExpression;

import com.google.common.collect.ImmutableMap;

public class ExpressionResolverTest extends BaseTest {
	@Rule public ExpectedException exception = ExpectedException.none();
	private File schema;

	@Before
	public void setUp() {
		schema = getLocalFile("/Schemas/LEMS_v0.9.0.xsd");
	}

	@Test
	public void testNested() throws Throwable {

		File lemsDoc = getLocalFile("/examples/expression-resolver-test/nested_expressions.xml");

		LEMSCompilerFrontend compiler = new LEMSCompilerFrontend(lemsDoc,
				schema);
		Lems compiledLems = compiler.generateLEMSDocument();

		assertEquals("-0.1", compiledLems.getConstantByName("const0").getValue());
		Double p0 = new Double(2.0);
		Double dp0 = compiledLems.getComponentById("comp0").resolve("dp0").evaluate(null);
		Double dp1 = compiledLems.getComponentById("comp0").resolve("dp1").evaluate(null);
		assertEquals(p0 * p0, dp0, 1e-12);
		assertEquals(p0 * p0 * dp0, dp1, 1e-12);
	}
	
	@Test
	public void testUndefinedSymbol() throws Throwable {
		exception.expect(LEMSCompilerException.class);
		exception.expectMessage(LEMSCompilerError.UndefinedSymbol.toString());

		Lems fakeLems = (Lems) new Lems()
				.withComponentTypes(
						(ComponentType)
						new ComponentType()
							.withName("Foo")
							.withDerivedParameters(
								new DerivedParameter()
									.withName("fake")
									.withValue("2 * undefined")))
				.withComponents((Component)
								new Component().withType("Foo"));

		LEMSCompilerFrontend.semanticAnalysis(fakeLems);
	}
	
	@Test
	public void testSymbolicExpression() throws Throwable {

		File lemsDoc = getLocalFile("/examples/expression-resolver-test/nested_expressions.xml");

		LEMSCompilerFrontend compiler = new LEMSCompilerFrontend(lemsDoc, schema);
		Lems compiledLems = compiler.generateLEMSDocument();
		@SuppressWarnings("unchecked")
		SymbolicExpression<DerivedVariable> dv0 = (SymbolicExpression<DerivedVariable>)
				compiledLems
				.getComponentById("comp0")
				.resolve("dv0");
		Set<String> independentVariables = dv0.getIndependentVariables();
		assertTrue(independentVariables.contains("x0"));
		assertTrue(independentVariables.contains("const0"));
		Double x = dv0.evaluate(new ImmutableMap.Builder<String, Double>()
								.put("x0", 0.)
								.put("const0", 0.)
								.build());
		assertEquals(x, 0, 1e-10);

	}

}
