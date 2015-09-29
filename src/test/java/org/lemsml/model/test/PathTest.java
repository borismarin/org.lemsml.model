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
import org.lemsml.model.Text;
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
								.withName("q0"))
					.withTexts(
							new Text()
								.withName("colour")),
				(ComponentType)
				new ComponentType()
					.withName("Bar")
					.withDynamics(
							new Dynamics()
								.withDerivedVariables(
										new DerivedVariable()
											.withName("foos_baz_q0_mult")
											.withSelect("foos[*]/baz/q0")
											.withReduce("multiply"),
										new DerivedVariable()
											.withName("blueFoos_Baz_q0_sum")
											.withSelect("foos[colour='blue']/baz/q0")
											.withReduce("add"),
										new DerivedVariable()
											.withName("redFoos_Baz_q0_sum")
											.withSelect("foos[colour='red']/baz/q0")
											.withReduce("add"),
										new DerivedVariable()
											.withName("greenFoos_Baz_q0_sum") // no green foos...
											.withSelect("foos[colour='green']/baz/q0")
											.withReduce("add"),
										new DerivedVariable()
											.withName("foos_p0_sum")
											.withSelect("foos[*]/p0")
											.withReduce("add")))
					.withChildrens(
							new Children()
								.withName("foos")
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
									.withTextValue("colour", "blue")
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
										.withTextValue("colour", "red")
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
										.withTextValue("colour", "red")
										.withParameterValue("p0", "2.0"))
			);

		LEMSCompilerFrontend.semanticAnalysis(lems);

		assertEquals(Quantities.getQuantity(3.0, ONE),
				lems.getComponentById("bar0").getScope().evaluate("foos_p0_sum"));

		assertEquals(0.006,
				lems.getComponentById("bar0").getScope().evaluate("foos_baz_q0_mult").getValue().doubleValue(), 1e-9);

		assertEquals(0.5,
				lems.getComponentById("bar0").getScope().evaluate("redFoos_Baz_q0_sum").getValue().doubleValue(), 1e-9);

		assertEquals(0.1,
				lems.getComponentById("bar0").getScope().evaluate("blueFoos_Baz_q0_sum").getValue().doubleValue(), 1e-9);

		assertEquals(0.,
				lems.getComponentById("bar0").getScope().evaluate("greenFoos_Baz_q0_sum").getValue().doubleValue(), 1e-9);

	}


}
