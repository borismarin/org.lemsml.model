package org.lemsml.model.compiler.semantic.visitors;

import javax.xml.namespace.QName;

import org.lemsml.model.ChildInstance;
import org.lemsml.model.Structure;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.BaseVisitor;

public class StructureVisitor extends BaseVisitor<Boolean, Throwable> {

	private Lems lems;
	private Component context;

	public StructureVisitor(Lems lems) throws Throwable {
		this.lems = lems;
	}

	//TODO: TOPOSORT?

	@Override
	public Boolean visit(Component comp) throws LEMSCompilerException {
		this.context = comp;
		ComponentType type = comp.getComponentType();
		//TODO: visiting / traversing
		for(Structure struct : type.getStructures()){
			for(ChildInstance child: struct.getChildInstances()){
				QName attrName = new QName(child.getComponent());
				//TODO: what is the scope for comp refs? considering toplevel only
				Component componentById = this.lems.getComponentById(comp.getOtherAttributes().get(attrName));
				//TODO: should be a copy?
				context.bindSubCompToName(componentById, attrName.toString());
				comp.withComponent(componentById);
			}
		}
		return true;
	}




}
