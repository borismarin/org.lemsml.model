package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static tec.units.ri.AbstractUnit.ONE;
import static tec.units.ri.unit.SI.SECOND;

import java.io.File;
import java.util.Set;

import javax.measure.Quantity;
import javax.measure.quantity.Dimensionless;

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

import tec.units.ri.quantity.Quantities;

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

		Double c0d = -0.1;
		Double p0d = 2.;
		Double p1d = 10.;
		Double p2d = 100.;
		Double dp0d = p0d * p0d;
		Double dp1d = (p0d * p0d) / dp0d;
		Double dp2d = dp0d * dp1d * c0d;
		Double dp1_1d = p0d * p1d;

		Quantity<?> const0 = compiledLems.getScope().evaluate("const0");
		Component comp0 = compiledLems.getComponentById("comp0");
		Quantity<?> dp0 = comp0.getScope().evaluate("dp0");
		Quantity<?> dp1 = comp0.getScope().evaluate("dp1");
		Quantity<?> dp2 = comp0.getScope().evaluate("dp2");

		assertEquals(adim(c0d), const0);
		assertEquals(adim(dp0d), dp0);
		assertEquals(adim(dp1d), dp1);
		assertEquals(adim(dp2d), dp2);

		Component comp1 = compiledLems.getComponentById("comp1");
		Quantity<?> p1 = comp1.getScope().evaluate("p1");
		Quantity<?> dp1_1 = comp1.getScope().evaluate("dp1");
		assertEquals(adim(p1d), p1);
		assertEquals(adim(dp1_1d) , dp1_1);

		Component nested = compiledLems.getComponentById("veryNested");
		Quantity<?> dp0_nested= nested.getScope().evaluate("dp0");
		assertEquals(adim(p2d * Math.pow(c0d, 2)), dp0_nested);


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

	@SuppressWarnings("deprecation")
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
		Quantity<?> x = comp0.getScope().evaluate("dv0", new ImmutableMap.Builder<String, Quantity<?>>()
								.put("x0", adim(1.))
								.build());
		assertEquals(adim(-1.), x);

		Component comp1 = compiledLems.getComponentById("comp1");
		Symbol dy1 = comp1.getScope().resolve("dy1_dt");
		Set<String> independentVariables1 = dy1.getIndependentVariables();
		assertTrue(independentVariables1.contains("y1"));
		//TODO: think about what we expect for symb expre expressions which
		//      depend on top-level symb exprs (i.e. do we expand "dv0" below?)
		Quantity<?> dy = comp1.getScope().evaluate("dy1_dt", new ImmutableMap.Builder<String, Quantity<?>>()
								.put("y1", adim(0.1))
								.put("dv0", adim(10.))
								.build());
		assertEquals(Quantities.getQuantity(-1.0, SECOND.inverse()), dy);

	}

	public Quantity<Dimensionless> adim(Double x) {
		return Quantities.getQuantity(x, ONE);
	}

}
