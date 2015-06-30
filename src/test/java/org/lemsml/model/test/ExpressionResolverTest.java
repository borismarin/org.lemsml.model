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
import org.lemsml.model.compiler.LEMSCompilerFrontend;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.Symbol;

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

		Double const0 = compiledLems.getScope().evaluate("const0");
		Component comp0 = compiledLems.getComponentById("comp0");
		Double p0 = comp0.getScope().evaluate("p0");
		Double dp0 = comp0.getScope().evaluate("dp0");
		Double dp1 = comp0.getScope().evaluate("dp1");
		Double dp2 = comp0.getScope().evaluate("dp2");
		assertEquals(-0.1, const0, 1e-12);
		assertEquals(p0 * p0, dp0, 1e-12);
		assertEquals((p0 * p0) / dp0, dp1, 1e-12);
		assertEquals(dp0 * dp1 * const0, dp2, 1e-12);

		Component comp1 = compiledLems.getComponentById("comp1");
		Double p1 = comp1.getScope().evaluate("p1");
		Double dp1_1 = comp1.getScope().evaluate("dp1");
		assertEquals(10., p1, 1e-12);
		assertEquals((p0 * p1) , dp1_1, 1e-12);

		Component nested = compiledLems.getComponentById("veryNested");
		Double p2 = nested.getScope().evaluate("p2");
		Double dp0_nested= nested.getScope().evaluate("dp0");
		assertEquals(p2 * Math.pow(const0, 2) , dp0_nested, 1e-12);


		//TODO error if Requirement is not set
		// (we find symbols upscope even if they are not required, set IScope.resolve)

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

		Lems compiledLems = new LEMSCompilerFrontend(lemsDoc, schema).generateLEMSDocument();
		Component comp0 = compiledLems.getComponentById("comp0");
		//SymbolicExpression<DerivedVariable> dv0 = (SymbolicExpression<DerivedVariable>)
			//comp0.resolve("dv0");
		Symbol dv0 = comp0.getScope().resolve("dv0");
		Set<String> independentVariables = dv0.getIndependentVariables();
		assertTrue(independentVariables.contains("x0"));
		Double x = comp0.getScope().evaluate("dv0", new ImmutableMap.Builder<String, Double>()
								.put("x0", 0.)
								.build());
		assertEquals(0, x, 1e-10);

		Component comp1 = compiledLems.getComponentById("comp1");
		Symbol dy1 = comp1.getScope().resolve("dy1_dt");
		Set<String> independentVariables1 = dy1.getIndependentVariables();
		assertTrue(independentVariables1.contains("y1"));
		//TODO: think about what we expect for symb expre expressions which
		//      depend on top-level symb exprs (i.e. do we expand "dv0" below?)
		Double dy = comp1.getScope().evaluate("dy1_dt", new ImmutableMap.Builder<String, Double>()
								.put("y1", 1.)
								.put("dv0", 1.)
								.build());
		assertEquals(-1., dy, 1e-10);

	}

}
