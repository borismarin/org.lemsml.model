package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;
import static tec.units.ri.AbstractUnit.ONE;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lemsml.model.Case;
import org.lemsml.model.ConditionalDerivedVariable;
import org.lemsml.model.DerivedVariable;
import org.lemsml.model.Dynamics;
import org.lemsml.model.Parameter;
import org.lemsml.model.compiler.LEMSCompilerFrontend;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Lems;

import tec.units.ri.quantity.Quantities;

public class ConditionTest extends BaseTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testConditionalDerivedVariable() throws Throwable {

		Lems lems = (Lems) new Lems()
			.withComponentTypes(
				(ComponentType)
				new ComponentType()
					.withName("Foo")
					.withParameters(
						new Parameter()
							.withName("p0"))
					.withDynamics(
						new Dynamics()
							.withDerivedVariables(
									new DerivedVariable()
										.withName("twoP0")
										.withValueDefinition("2*p0"),
									new DerivedVariable()
										.withName("TwoAbsTwoP0")
										.withValueDefinition("2*absTwoP0"))
							.withConditionalDerivedVariables(
									new ConditionalDerivedVariable()
										.withName("absTwoP0")
										.withCase(
											new Case().withCondition("twoP0 .gt. 0").withValueDefinition("twoP0"),
											new Case().withCondition("twoP0 .lt. 0").withValueDefinition("-twoP0")))))
			.withComponents(
					((Component)
					new Component()
						.withType("Foo")
						.withId("foo0"))
						.withParameterValue("p0", "1.0")
			);

		LEMSCompilerFrontend.semanticAnalysis(lems);

		Component foo0 = lems.getComponentById("foo0");

		assertEquals(Quantities.getQuantity(2.0, ONE),
				foo0.getScope().evaluate("absTwoP0"));
		assertEquals(Quantities.getQuantity(4.0, ONE),
				foo0.getScope().evaluate("TwoAbsTwoP0"));

		foo0.withParameterValue("p0", "-2");
		assertEquals(Quantities.getQuantity(4.0, ONE),
				foo0.getScope().evaluate("absTwoP0"));
		assertEquals(Quantities.getQuantity(8.0, ONE),
				foo0.getScope().evaluate("TwoAbsTwoP0"));


	}


}
