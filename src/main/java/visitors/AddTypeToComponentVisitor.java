package visitors;

import org.lemsml.model.Component;
import org.lemsml.model.ComponentType;
import org.lemsml.visitors.BaseVisitor;


public class AddTypeToComponentVisitor<L> extends BaseVisitor<L, Throwable>{

	private L lems;

	public AddTypeToComponentVisitor(L lems) {
		this.lems = lems;
	}

	@Override
	public L visit(Component comp) throws Throwable {
		ComponentType ctToSet = ((extended.Lems) this.lems)
				.getComponentTypeByName(comp.getType());
		((extended.Component) comp).set_ComponentType(ctToSet);
		return null;
	}

	public L getLems() {
		return lems;
	}

	public void setLems(L lems) {
		this.lems = lems;
	}

}
