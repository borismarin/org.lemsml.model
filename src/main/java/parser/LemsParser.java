package parser;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.lemsml.model.ComponentType;
import org.lemsml.model.test.BuildNameComponentTypeMapVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

import extended.Lems;

public class LemsParser {
	

	public LemsParser(File schema, File hr_def) {
		// TODO Auto-generated constructor stub
	}

	public void populateNameComponentTypeHM(Lems lems) throws Throwable {
		BuildNameComponentTypeMapVisitor<Lems> nameComptypeVisitor = new BuildNameComponentTypeMapVisitor<Lems>(
				lems);
		TraversingVisitor<Lems, Throwable> tv = new TraversingVisitor<Lems, Throwable>(
				new DepthFirstTraverserImpl<Throwable>(), nameComptypeVisitor);
		tv.setTraverseFirst(true);
		lems.accept(tv);

		Map<String, ComponentType> nameTypeMap = nameComptypeVisitor.getLems().getComponentTypesByName();
		for (Entry<String, ComponentType> entry : nameTypeMap.entrySet()) {
			System.out.println(entry.getKey() + " : "
					+ entry.getValue().getClass().toString());

		}
	}

}
