package parser;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.lemsml.model.ComponentType;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

import visitors.BuildNameComponentTypeMapVisitor;
import extended.Lems;

public class LemsParser {
	
	Lems lems;

	public Lems getLems() {
		return lems;
	}

	public LemsParser(File lemsdocument, File schema) {
		this.lems = LemsXmlUtils.unmarshall(lemsdocument, schema);
	}

	public void populateNameComponentTypeHM() throws Throwable {
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
