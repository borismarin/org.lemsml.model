package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lemsml.model.DerivedParameter;
import org.lemsml.model.DerivedVariable;
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

		Double const0 = compiledLems.resolve("const0").evaluate();
		Component comp0 = compiledLems.getComponentById("comp0");
		Double p0 = comp0.resolve("p0").evaluate();
		Double dp0 = comp0.resolve("dp0").evaluate();
		Double dp1 = comp0.resolve("dp1").evaluate();
		Double dp2 = comp0.resolve("dp2").evaluate();
		assertEquals(-0.1, const0, 1e-12);
		assertEquals(p0 * p0, dp0, 1e-12);
		assertEquals((p0 * p0) / dp0, dp1, 1e-12);
		assertEquals(dp0 * dp1 * const0, dp2, 1e-12);
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
									.withValueDefinition("2 * undefined")))
				.withComponents((Component)
								new Component().withType("Foo"));

		LEMSCompilerFrontend.semanticAnalysis(fakeLems);
	}
	
	@Test
	public void testSymbolicExpression() throws Throwable {

		File lemsDoc = getLocalFile("/examples/expression-resolver-test/nested_expressions.xml");

		LEMSCompilerFrontend compiler = new LEMSCompilerFrontend(lemsDoc, schema);
		Lems compiledLems = compiler.generateLEMSDocument();
		Component comp0 = compiledLems.getComponentById("comp0");
		@SuppressWarnings("unchecked")
		SymbolicExpression<DerivedVariable> dv0 = (SymbolicExpression<DerivedVariable>)
			comp0.resolve("dv0");
		Set<String> independentVariables = dv0.getIndependentVariables();
		assertTrue(independentVariables.contains("x0"));
		Double x = dv0.evaluate(new ImmutableMap.Builder<String, Double>()
								.put("x0", 0.)
								.build());
		assertEquals(x, 0, 1e-10);

	}

}
