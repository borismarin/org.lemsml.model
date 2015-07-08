package org.lemsml.model.compiler.semantic.visitors;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.TraversingVisitor;

import expr_parser.utils.DirectedGraph;
import expr_parser.utils.TopologicalSort;

/**
 * @author borismarin
 *
 */
public class TypeExtensionVisitor extends BaseVisitor<Boolean, Throwable> {

	private Lems lems;
	private DirectedGraph<ComponentType> dependencies = new DirectedGraph<ComponentType>();

	public TypeExtensionVisitor(Lems lems) throws Throwable {
		this.lems = lems;
	}

	@Override
	public Boolean visit(ComponentType compType) throws Throwable {

		// will copy the content of the parent type
		String ext = compType.getExtends();
		if (null != ext) {
			ComponentType base = lems.getComponentTypeByName(ext);
			if (null == base) {
				String err = MessageFormat
						.format("(ComponentType) {0} trying to extend from of unknow (ComponentType) {1}",
								compType.getName(), ext);
				throw new LEMSCompilerException(err,
						LEMSCompilerError.UndefinedComponentType);
			} else {
				dependencies.addNode(compType);
				dependencies.addNode(base);
				dependencies.addEdge(compType, base);
			}
		}

		return true;
	}

	public void visitToposortedTypes() throws Throwable {
		List<ComponentType> sorted = TopologicalSort.sort(dependencies);
		Collections.reverse(sorted);
		for (ComponentType ct : sorted) {
			ComponentType base = lems.getComponentTypeByName(ct.getExtends());
			if (base != null) {
				CopyComponentTypeDef typeCopier = new CopyComponentTypeDef(ct);
				TraversingVisitor<Boolean, Throwable> trav = new TraversingVisitor<Boolean, Throwable>(new DepthFirstTraverserExt<Throwable>(), typeCopier);
				trav.setTraverseFirst(true);
				base.accept(trav);
			}
		}
	}

}
