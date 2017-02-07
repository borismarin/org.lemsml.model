package org.lemsml.model.compiler.backend;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lemsml.model.compiler.LEMSCompilerFrontend;
import org.lemsml.model.compiler.semantic.visitors.TypeExtensionVisitor;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Lems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import com.google.common.base.CaseFormat;

import org.lemsml.expr_parser.utils.DirectedGraph;
import org.lemsml.expr_parser.utils.TopologicalSort;

public class JavaDomainModelBackend {

	private static final Logger logger = LoggerFactory
			.getLogger(JavaDomainModelBackend.class);
	private Lems domainDefs;
	private String pkgName;
	private STGroup typeSTG;
	private LEMSCompilerFrontend frontend;
	private File baseDir;

	private String mlName;
	private File outputDir;
	private File componentTypeDefs;

	public JavaDomainModelBackend(String ml, File compTypeDefs, File outDir) {
		this.mlName = ml;
		this.componentTypeDefs = compTypeDefs;
		this.outputDir = outDir;
	}

	public void generate() throws Throwable {
		logger.info(MessageFormat.format(
				"Generating domain model classes for {0},\n"
						+ "\t defined in {1},\n" + "\t onto {2}", mlName,
				componentTypeDefs, outputDir));

		pkgName = "org." + mlName + ".model";
		baseDir = new File(outputDir, pkgName.replace(".", "/"));
		frontend = new LEMSCompilerFrontend(componentTypeDefs);
		domainDefs = frontend.generateLEMSDocument();
		typeSTG = parseDomainTypeSTG();

		baseDir.mkdirs();
		createBaseDefinitions();
		generateDomainClasses();
		generateObjectFactory();
	}

	private void createBaseDefinitions() throws IOException {
		String fName = CaseFormat.LOWER_CAMEL
				.to(CaseFormat.UPPER_CAMEL, mlName) + ".java";
		logger.info("\t" + fName);

		ST merged = mergeRootElement();
		dumpTemplateToFile(fName, merged);

	}

	private void generateObjectFactory() throws IOException {
		String fName = "ObjectFactory.java";

		URL stURL = getClass().getResource(
				"/templates/java-domain-classes/obj_factory.stg");
		STGroup group = new STGroupFile(stURL, "UTF-8", '<', '>');
		group.registerRenderer(String.class, new SafeJavaStringRenderer());

		ST template = group.getInstanceOf("obj_factory");
		template.add("lems", domainDefs);
		template.add("package", pkgName);
		template.add("ml_name", mlName);
		dumpTemplateToFile(fName, template);

	}

	private void generateDomainClasses() throws IOException {
		for (ComponentType ct : domainDefs.getComponentTypes()) {
			SafeJavaStringRenderer renderer = new SafeJavaStringRenderer();
			String classFname = renderer.toString(ct.getName(), "cap,safe", null) + ".java";
			logger.info("\t" + classFname);

			ST merged = mergeCompTypeTemplate(ct);
			dumpTemplateToFile(classFname, merged);
		}
	}

	public ST mergeCompTypeTemplate(ComponentType ct) {
		ST template = typeSTG.getInstanceOf("class_file");

		TypeExtensionVisitor visitor = (TypeExtensionVisitor) frontend
				.getSemanticAnalyser().getTypeExtender().getVisitor();
		DirectedGraph<ComponentType> typeGraph = TopologicalSort
				.reverseGraph(visitor.getTypeGraph());

		Map<String, Set<ComponentType>> typeDepsMap = new HashMap<String, Set<ComponentType>>();
		for (ComponentType node : typeGraph.getGraph().keySet()) {
			List<ComponentType> result = new ArrayList<ComponentType>();
			Set<ComponentType> visited = new HashSet<ComponentType>();
			Set<ComponentType> expanded = new HashSet<ComponentType>();
			TopologicalSort.explore(node, typeGraph, result, visited, expanded);
			typeDepsMap.put(node.getName(), new HashSet<ComponentType>(result));
		}

		template.add("type_deps", typeDepsMap);
		template.add("type", ct);
		template.add("ml_name", mlName);
		template.add("package", pkgName);

		return template;
	}

	public ST mergeRootElement() {
		URL stURL = getClass().getResource(
				"/templates/java-domain-classes/root_element.stg");
		STGroup group = new STGroupFile(stURL, "UTF-8", '<', '>');
		group.registerRenderer(String.class, new SafeJavaStringRenderer());

		ST template = group.getInstanceOf("root_element");
		template.add("lems", domainDefs);
		template.add("ml_name", mlName);
		template.add("package", pkgName);

		return template;
	}

	private STGroup parseDomainTypeSTG() {
		URL stURL = getClass().getResource(
				"/templates/java-domain-classes/domain_type.stg");
		STGroup group = new STGroupFile(stURL, "UTF-8", '<', '>');
		group.registerRenderer(String.class, new SafeJavaStringRenderer());
		return group;
	}

	public void dumpTemplateToFile(String fName, ST merged) throws IOException {
		FileWriter out = new FileWriter(new File(baseDir, fName));
		out.append(merged.render());
		out.flush();
		out.close();
	}

}
