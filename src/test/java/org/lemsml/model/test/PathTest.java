package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;
import static tec.units.ri.AbstractUnit.ONE;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lemsml.model.Child;
import org.lemsml.model.Children;
import org.lemsml.model.DerivedVariable;
import org.lemsml.model.Dynamics;
import org.lemsml.model.Parameter;
import org.lemsml.model.compiler.LEMSCompilerFrontend;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Lems;

import tec.units.ri.quantity.Quantities;

public class PathTest extends BaseTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testPath() throws Throwable {

		Lems lems = (Lems) new Lems()
			.withComponentTypes(
				(ComponentType)
				new ComponentType()
					.withName("Foo")
					.withParameters(
						new Parameter()
							.withName("p0"))
					.withChildren(
							new Child()
								.withName("baz")
								.withType("Baz")),
				(ComponentType)
				new ComponentType()
					.withName("Baz")
					.withParameters(
							new Parameter()
							.withName("q0")),
				(ComponentType)
				new ComponentType()
					.withName("Bar")
					.withDynamics(
							new Dynamics()
								.withDerivedVariables(
										new DerivedVariable()
											.withName("foo1_p0")
											.withSelect("foo1/p0"),
										new DerivedVariable()
											.withName("foo2_baz_q0")
											.withSelect("foo2/baz/q0"),
										new DerivedVariable()
											.withName("foos_baz_q0_mult")
											.withSelect("Foo[*]/baz/q0")
											.withReduce("multiply"),
										new DerivedVariable()
											.withName("foos_p0_sum")
											.withSelect("Foo[*]/p0")
											.withReduce("add")))
					.withChildrens(
							new Children()
								.withName("Foos")
								.withType("Foo"))
				)
			.withComponents(
					(Component)
					new Component()
						.withType("Bar")
						.withId("bar0")
						.withComponent(
								((Component)
								new Component()
									.withType("Foo")
									.withComponent(
										((Component)
										new Component()
											.withType("Baz")
											.withId("baz"))
											.withParameterValue("q0", "0.1"))
									.withId("foo0"))
									.withParameterValue("p0", "0.0"),
								((Component)
									new Component()
										.withType("Foo")
										.withComponent(
											((Component)
												new Component()
													.withType("Baz")
													.withId("baz"))
													.withParameterValue("q0", "0.2"))
										.withId("foo1"))
										.withParameterValue("p0", "1.0"),
								((Component)
									new Component()
										.withType("Foo")
										.withComponent(
											((Component)
												new Component()
													.withType("Baz")
													.withId("baz"))
													.withParameterValue("q0", "0.3"))
										.withId("foo2"))
										.withParameterValue("p0", "2.0"))
			);

		LEMSCompilerFrontend.semanticAnalysis(lems);

		assertEquals(lems.getComponentById("foo1").getScope().evaluate("p0"),
				lems.getComponentById("bar0").getScope().evaluate("foo1_p0"));

		assertEquals(Quantities.getQuantity(3.0, ONE),
				lems.getComponentById("bar0").getScope().evaluate("foos_p0_sum"));

		assertEquals(Quantities.getQuantity(0.3, ONE),
				lems.getComponentById("bar0").getScope().evaluate("foo2_baz_q0"));

		assertEquals(0.006,
				lems.getComponentById("bar0").getScope().evaluate("foos_baz_q0_mult").getValue().doubleValue(), 1e-9);

	}


}
