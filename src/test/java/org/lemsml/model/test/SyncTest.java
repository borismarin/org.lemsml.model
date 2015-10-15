package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static tec.units.ri.AbstractUnit.ONE;
import static tec.units.ri.unit.SI.SECOND;

import java.io.File;
import java.util.Set;

import javax.measure.Quantity;
import javax.measure.quantity.Dimensionless;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lemsml.model.Child;
import org.lemsml.model.DerivedParameter;
import org.lemsml.model.Parameter;
import org.lemsml.model.compiler.LEMSCompilerFrontend;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.Symbol;

import tec.units.ri.quantity.Quantities;

import com.google.common.collect.ImmutableMap;

public class SyncTest extends BaseTest {
	LEMSCompilerFrontend compiler = new LEMSCompilerFrontend(null);

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testParams() throws Throwable {

		// type Foo defined below, inline with Lems
		Component foo = ((Component) new Component().withType("Foo").withId(
				"foo")).withParameterValue("p", "1");

		Lems lems = (Lems) new Lems().withComponentTypes(
				(ComponentType) new ComponentType()
						.withName("Foo")
						.withParameters(new Parameter().withName("p"))
						.withDerivedParameters(
								new DerivedParameter().withName("twoP")
										.withValueDefinition("2 * p")))
				.withComponents(foo);

		compiler.semanticAnalysis(lems);
		assertEquals(adim(1.0), foo.getScope().evaluate("p"));
		assertEquals(adim(2.0), foo.getScope().evaluate("twoP"));

		lems.getComponentById("foo").withParameterValue("p", "2");
		assertEquals(adim(2.0), foo.getScope().evaluate("p"));
		assertEquals(adim(4.0), foo.getScope().evaluate("twoP"));
	}

	@SuppressWarnings("deprecation")
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
		Quantity<?> x = comp0.getScope().evaluate(
				"dv0",
				new ImmutableMap.Builder<String, Quantity<?>>().put("x0",
						adim(10.)).build());
		assertEquals(adim(-10.), x);

		Component comp1 = comp0.getSubComponentsWithName("comp1").get(0);
		Symbol dy1 = comp1.getScope().resolve("dy1_dt");
		Set<String> independentVariables1 = dy1.getIndependentVariables();
		assertTrue(independentVariables1.contains("y1"));
		Quantity<?> dy = comp1.getScope().evaluate(
				"dy1_dt",
				new ImmutableMap.Builder<String, Quantity<?>>()
						.put("y1", adim(1.)).put("dv0", adim(1.)).build());
		assertEquals(Quantities.getQuantity(-1., SECOND.inverse()), dy);

		Quantity<?> dx1 = comp1.getScope().evaluate(
				"dx1_dt",
				new ImmutableMap.Builder<String, Quantity<?>>().put("x1",
						adim(10.)).build());
		assertEquals(Quantities.getQuantity(-1., SECOND.inverse()), dx1);

		Quantity<?> dz1 = comp1.getScope().evaluate(
				"dz1_dt",
				new ImmutableMap.Builder<String, Quantity<?>>().put("x0",
						adim(0.5)).build());
		assertEquals(Quantities.getQuantity(-1., SECOND.inverse()), dz1);

		comp0.withParameterValue("p0", "3");
		dz1 = comp1.getScope().evaluate(
				"dz1_dt",
				new ImmutableMap.Builder<String, Quantity<?>>().put("x0",
						adim(0.5)).build());
		assertEquals(Quantities.getQuantity(-1.5, SECOND.inverse()), dz1);

	}

	@Test
	public void testNamespaceLeak() throws Throwable {
		ComponentType Foo = (ComponentType) new ComponentType()
				.withName("Foo")
				.withParameters(new Parameter()
							.withName("p"))
				.withDerivedParameters(new DerivedParameter()
							.withName("twoP")
							.withValueDefinition("2 * p"))
				.withChildren(new Child()
							.withName("aBar")
							.withType("Bar"));

		ComponentType Bar = (ComponentType) new ComponentType()
				.withName("Bar")
				.withParameters(new Parameter()
							.withName("p"))
				.withDerivedParameters(new DerivedParameter()
							.withName("twoP1")
							.withValueDefinition("twoP * p"));

		Component bar = ((Component) new Component()
									.withType("Bar").withId("bar"))
									.withParameterValue("p", "3");

		Component foo = ((Component) new Component()
							.withType("Foo")
							.withId("foo")
							.withComponent(bar))
							.withParameterValue("p", "1");

		Lems lems = (Lems) new Lems().withComponentTypes(Foo, Bar).withComponents(foo);

		compiler.semanticAnalysis(lems);
		assertEquals(adim(1.0), foo.getScope().evaluate("p"));
		assertEquals(adim(2.0), foo.getScope().evaluate("twoP"));
		assertEquals(adim(6.0), bar.getScope().evaluate("twoP1"));

		foo.withParameterValue("p", "2");
		assertEquals(adim(2.0), foo.getScope().evaluate("p"));
		assertEquals(adim(4.0), foo.getScope().evaluate("twoP"));
		assertEquals(adim(12.0), bar.getScope().evaluate("twoP1"));
	}

	public Quantity<Dimensionless> adim(Double x) {
		return Quantities.getQuantity(x, ONE);
	}
}
