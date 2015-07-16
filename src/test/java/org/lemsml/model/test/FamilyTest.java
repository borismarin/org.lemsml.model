package org.lemsml.model.test;

import java.io.File;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lemsml.model.Child;
import org.lemsml.model.compiler.LEMSCompilerFrontend;
import org.lemsml.model.compiler.parser.LEMSXMLWriter;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Lems;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class FamilyTest extends BaseTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	ComponentType Bar = (ComponentType) new ComponentType()
			.withName("Bar");


	ComponentType Foo = (ComponentType) new ComponentType()
				.withName("Foo")
				.withChildren(
						new Child()
							.withType("Bar")
							.withName("bar0"));

//	TODO: see https://github.com/LEMS/jLEMS/issues/71
	@Test
	public void testTooManyChilds() throws Throwable {

		exception.expect(LEMSCompilerException.class);
		exception.expectMessage(LEMSCompilerError.TooManyChildren.toString());

		Component bar0 = (Component) new Component()
				.withType("Bar")
				.withName("bar0");

		Component bar1 = (Component) new Component()
				.withType("Bar")
				.withName("bar0");

		Component foo = (Component) new Component()
				.withType("Foo")
				.withComponent(bar0, bar1);

		Lems lems = (Lems) new Lems()
				.withComponentTypes(Bar, Foo)
				.withComponents(foo);

		LEMSCompilerFrontend.semanticAnalysis(lems);

	}

	@Test
	public void testUnboundChild() throws Throwable {

		exception.expect(LEMSCompilerException.class);
		exception.expectMessage(LEMSCompilerError.UnboundChild.toString());

		ComponentType Goo = (ComponentType) new ComponentType()
								.withName("Goo")
								.withChildren(
										new Child().withType("Bar").withName("bar0"),
										new Child().withType("Bar").withName("bar1"));

		Component bar0 = (Component) new Component()
				.withType("Bar")
				.withName("bar0");

		Component bar1 = (Component) new Component()
				.withType("Bar")
				.withName("bar11");

		Component goo = (Component) new Component()
				.withType("Goo")
				.withComponent(bar0, bar1);

		Lems lems = (Lems) new Lems()
				.withComponentTypes(Bar, Goo)
				.withComponents(goo);

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
	public void testParents() throws Throwable {

		ComponentType Baz = (ComponentType) new ComponentType()
							.withName("Baz")
							.withChildren(new Child().withType("Foo"));

		Component bar0 = (Component) new Component()
				.withType("Bar")
				.withName("bar0");

		Component foo0 = (Component) new Component()
					.withId("foo0")
					.withType("Foo")
					.withComponent(bar0);

		Component baz0 = (Component) new Component()
					.withId("baz0")
					.withType("Baz")
					.withComponent(foo0);

		Lems lems = (Lems) new Lems()
				.withComponentTypes(Bar, Foo, Baz)
				.withComponents(baz0);

		LEMSCompilerFrontend.semanticAnalysis(lems);


		Assert.assertEquals(foo0, bar0.getParent());
		Assert.assertEquals(baz0, foo0.getParent());
		Assert.assertEquals(null, baz0.getParent());
	}

	@Test
	public void testDeepNesting() throws Throwable {

		ComponentType Baz = (ComponentType) new ComponentType()
							.withName("Baz")
							.withChildren(new Child().withType("Foo"));

		Component bar0 = (Component) new Component()
				.withType("Bar")
				.withName("bar0");

		Component bar1 = (Component) new Component()
				.withType("Bar")
				.withName("bar1");

		Component foo0 = (Component) new Component()
					.withId("foo0")
					.withType("Foo")
					.withComponent(bar0);

		Component foo1 = (Component) new Component()
					.withId("foo1")
					.withType("Foo")
					.withComponent(bar1);

		Component baz0 = (Component) new Component()
					.withId("baz0")
					.withType("Baz")
					.withComponent(foo1);

		Lems lems = (Lems) new Lems()
				.withComponentTypes(Bar, Foo, Baz)
				.withComponents(foo0, baz0);

		LEMSCompilerFrontend.semanticAnalysis(lems);

		File tmpFile = File.createTempFile("family", ".xml");
		LEMSXMLWriter.marshall(lems, tmpFile);
		System.out.println(Files.toString(tmpFile, Charsets.UTF_8));

		Assert.assertEquals(foo0, bar0.getParent());
		Assert.assertEquals(foo1, bar1.getParent());
		Assert.assertEquals(baz0, foo1.getParent());

	}

}
