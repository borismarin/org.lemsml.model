package visitors;

import org.lemsml.model.ComponentType;
import org.lemsml.visitors.BaseVisitor;

public class BuildNameComponentTypeMapVisitor<L> extends BaseVisitor<L, Throwable>  {

	private L lems;

	public BuildNameComponentTypeMapVisitor(L lems) {
		this.lems = lems;
	}

	@Override
	public L visit(ComponentType ct) throws Throwable {
		((extended.Lems) this.lems).registerComponentTypeName(ct.getName(), ct);
		return lems;
	}

	public L getLems() {
		return lems;
	}

	public void setLems(L lems) {
		this.lems = lems;
	}

}
