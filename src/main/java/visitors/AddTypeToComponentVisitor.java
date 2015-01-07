package visitors;

import java.awt.Component;

import org.lemsml.model.ComponentType;
import org.lemsml.visitors.BaseVisitor;

import extended.Lems;

public class AddTypeToComponentVisitor<R> extends BaseVisitor<Void, Throwable> {

	private Lems lems;

	public AddTypeToComponentVisitor(Lems lems) {
		this.lems = lems;
	}

	@Override
	public Void visit(org.lemsml.model.Component comp) throws Throwable {
		ComponentType ctToSet = this.lems.getComponentTypeByName(comp.getType());
		//TODO: create component_name:Component map (identical to the one for comptypes)
		//        and use it in the line below.
		// WARNING: the line below is hardcoded as a proof of concept!!
		this.lems.getComponent().get(0).set_ComponentType(ctToSet);
		return null;
	}

	public Lems getLems() {
		return lems;
	}

	public void setLems(Lems lems) {
		this.lems = lems;
	}

}
