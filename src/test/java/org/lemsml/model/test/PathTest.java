package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;
import static tec.units.ri.AbstractUnit.ONE;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
							.withName("p0")),
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
										.withId("foo0"))
										.withParameterValue("p0", "0.0"),
								((Component)
									new Component()
										.withType("Foo")
										.withId("foo1"))
										.withParameterValue("p0", "1.0"),
								((Component)
									new Component()
										.withType("Foo")
										.withId("foo2"))
										.withParameterValue("p0", "2.0"))
			);

		LEMSCompilerFrontend.semanticAnalysis(lems);

		assertEquals(lems.getComponentById("foo1").getScope().evaluate("p0"),
				lems.getComponentById("bar0").getScope().evaluate("foo1_p0"));

		assertEquals(Quantities.getQuantity(3.0, ONE),
				lems.getComponentById("bar0").getScope().evaluate("foos_p0_sum"));

	}


}
