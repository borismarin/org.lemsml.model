package org.lemsml.model.test;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lemsml.model.Child;
import org.lemsml.model.compiler.LEMSCompilerFrontend;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Lems;

public class FamilyTest extends BaseTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	ComponentType Bar = (ComponentType) new ComponentType()
			.withName("Bar");


	ComponentType Foo = (ComponentType) new ComponentType()
				.withName("Foo")
				.withChildren(new Child().withType("Bar"));

	@Test
	public void testTooManyChilds() throws Throwable {

		exception.expect(LEMSCompilerException.class);
		exception.expectMessage(LEMSCompilerError.TooManyChildren.toString());

		Component bar0 = (Component) new Component()
				.withType("Bar")
				.withName("bar0");

		Component bar1 = (Component) new Component()
				.withType("Bar")
				.withName("bar1");

		Component foo = (Component) new Component()
				.withType("Foo")
				.withComponent(bar0, bar1);

		Lems lems = (Lems) new Lems()
				.withComponentTypes(Bar, Foo)
				.withComponents(foo);

		LEMSCompilerFrontend.semanticAnalysis(lems);

	}

	@Test
	public void testMissingChilds() throws Throwable {
		exception.expectMessage(LEMSCompilerError.MissingChildren.toString());

		Lems lems = (Lems) new Lems()
				.withComponentTypes(Bar, Foo)
				.withComponents((Component) new Component().withType("Foo"));

		LEMSCompilerFrontend.semanticAnalysis(lems);
	}

	@Test
	public void testFamilty() throws Throwable {

		Component bar0 = (Component) new Component()
				.withType("Bar")
				.withName("bar0");

		Component foo0 = (Component) new Component()
					.withId("foo0")
					.withType("Foo")
					.withComponent(bar0);

		Lems lems = (Lems) new Lems()
				.withComponentTypes(Bar, Foo)
				.withComponents(foo0);

		LEMSCompilerFrontend.semanticAnalysis(lems);
		
		Assert.assertEquals(foo0, bar0.getParent());
		Assert.assertEquals(lems, foo0.getParent());
	}

}
