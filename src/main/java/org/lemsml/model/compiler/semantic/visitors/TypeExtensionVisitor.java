package org.lemsml.model.compiler.semantic.visitors;

import java.text.MessageFormat;

import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.BaseVisitor;

import org.lemsml.expr_parser.utils.DirectedGraph;

/**
 * @author borismarin
 *
 */
public class TypeExtensionVisitor extends BaseVisitor<Boolean, Throwable> {

	private Lems lems;
	private DirectedGraph<ComponentType> typeGraph = new DirectedGraph<ComponentType>();

	public TypeExtensionVisitor(Lems lems) throws Throwable {
		this.lems = lems;
	}

	@Override
	public Boolean visit(ComponentType compType) throws Throwable {

		// will copy the content of the parent type
		String ext = compType.getExtends();
		if (null != ext) {
			ComponentType base = lems.getComponentTypeByName(ext);
			compType.setParent(base);
			if (null == base) {
				String err = MessageFormat
						.format("(ComponentType) {0} trying to extend from of unknow (ComponentType) {1}",
								compType.getName(), ext);
				throw new LEMSCompilerException(err,
						LEMSCompilerError.UndefinedComponentType);
			} else {
				getTypeGraph().addNode(compType);
				getTypeGraph().addNode(base);
				getTypeGraph().addEdge(compType, base);
			}
		}

		return true;
	}

	public DirectedGraph<ComponentType> getTypeGraph() {
		return typeGraph;
	}

	public void setTypeGraph(DirectedGraph<ComponentType> typeGraph) {
		this.typeGraph = typeGraph;
	}

}
