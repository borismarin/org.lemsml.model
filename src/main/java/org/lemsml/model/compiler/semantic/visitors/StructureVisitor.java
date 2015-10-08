package org.lemsml.model.compiler.semantic.visitors;

import java.lang.reflect.Field;

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
				//TODO: that should be a copy.
//				Component componentById = this.lems.getComponentById(attrName.toString());
				Field field = null;
				try {
					Class<? extends Component> clazz = comp.getClass();
					field = clazz.getField(attrName.toString());
				} catch (NoSuchFieldException | SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} //ugly
				Component componentById = null;
				try {
					componentById = this.lems.getComponentById((String) field.get(comp));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				context.bindSubCompToName(componentById, attrName.toString());
				comp.withComponent(componentById);
			}
		}
		return true;
	}




}
