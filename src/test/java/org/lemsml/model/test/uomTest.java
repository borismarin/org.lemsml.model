package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lemsml.model.DerivedParameter;
import org.lemsml.model.Parameter;
import org.lemsml.model.compiler.LEMSCompilerFrontend;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Dimension;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.Unit;

public class uomTest extends BaseTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testUnitsDimensions() throws Throwable {

		Lems lems = (Lems) new Lems()
			.withDimensions(
					(Dimension) new Dimension()
						.withName("distance")
						.withL(BigInteger.valueOf(1)),
					(Dimension) new Dimension()
						.withName("time")
						.withT(BigInteger.valueOf(1)),
					(Dimension) new Dimension()
						.withName("speed")
						.withT(BigInteger.valueOf(-1))
						.withL(BigInteger.valueOf(1)))
			.withUnits(
					(Unit) new Unit()
							.withSymbol("m")
							.withDimension("distance"),
					(Unit) new Unit()
							.withSymbol("s")
							.withDimension("time"),
					(Unit) new Unit()
							.withSymbol("cm")
							.withPower(BigInteger.valueOf(-2))
							.withDimension("distance"),
					(Unit) new Unit()
							.withSymbol("m_s")
							.withDimension("speed")
					)
			.withComponentTypes(
				(ComponentType) new ComponentType()
					.withName("Car")
					.withParameters(
						new Parameter()
							.withName("v")
							.withDimension("speed"),
						new Parameter()
							.withName("dt")
							.withDimension("time"),
						new Parameter()
							.withName("x0")
							.withDimension("distance"))
					.withDerivedParameters(
							new DerivedParameter()
								.withName("x")
								.withValueDefinition("x0 + v*dt")
								.withDimension("distance")))
			.withComponents(
					((Component) new Component()
						.withType("Car")
						.withId("aCar"))
						.withParameterValue("v", "1m_s")
						.withParameterValue("x0", "1 cm")
						.withParameterValue("dt", "1s")
		);


		LEMSCompilerFrontend.semanticAnalysis(lems);

		assertEquals(1.01, lems
					.getComponentById("aCar")
					.getScope().evaluate("x"), 1e-12);


		lems.getComponentById("aCar").withParameterValue("dt", "0s");
		assertEquals(0.01, lems
					.getComponentById("aCar")
					.getScope().evaluate("x"), 1e-12);

	}



}
