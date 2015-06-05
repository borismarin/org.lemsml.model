package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;

import javax.xml.namespace.QName;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lemsml.model.Parameter;
import org.lemsml.model.compiler.LEMSCompilerFrontend;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Lems;

public class InheritanceTest extends BaseTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testInheritedPar() throws Throwable {
		
		Lems lems = (Lems) new Lems()
			.withComponentTypes(
				(ComponentType)
				new ComponentType()
					.withName("Foo")
					.withParameters(
						new Parameter()
							.withName("p0")))
			.withComponentTypes(
				(ComponentType)
				new ComponentType()
					.withName("Bar")
					.withExtends("Foo"));

		Component comp0 = (Component) new Component()
							.withType("Bar")
							.withId("comp0");
		//ugly...
		comp0.getOtherAttributes().put(new QName("p0"), "1");
		
		lems.getComponents().add(comp0);

		LEMSCompilerFrontend.semanticAnalysis(lems);
		
		assertEquals(1, lems
					.getComponentById("comp0")
					.getParameterByName("p0")
					.getDimensionalValue()
					.evaluateSI(), 1e-12);
	}

	@Test
	public void testInheritedParBis() throws Throwable {
		
		Lems lems = (Lems) new Lems()
			.withComponentTypes(
				(ComponentType)
				new ComponentType()
					.withName("Foo")
					.withParameters(
						new Parameter()
							.withName("p0")))
			.withComponentTypes(
				(ComponentType)
				new ComponentType()
					.withName("Bar")
					.withExtends("Foo"))
			.withComponents(
					(Component)
					new Component()
						.withType("Bar")
						.withId("comp0")
			);
		exception.expect(LEMSCompilerException.class);
		exception.expectMessage(LEMSCompilerError.RequiredParameterUndefined.toString());

		LEMSCompilerFrontend.semanticAnalysis(lems);
		
	}

	@Test
	public void testWrongInheritance() throws Throwable {
		
		Lems lems = (Lems) new Lems()
			.withComponentTypes(
				(ComponentType)
				new ComponentType()
					.withName("Foo")
					.withExtends("Bar"));
		exception.expect(LEMSCompilerException.class);
		exception.expectMessage(LEMSCompilerError.ComponentTypeNotDefined.toString());

		LEMSCompilerFrontend.semanticAnalysis(lems);
		
	}
	

}
