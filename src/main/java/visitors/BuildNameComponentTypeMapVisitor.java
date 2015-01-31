package visitors;

import org.lemsml.model.ComponentType;
import org.lemsml.model.Lems;
import org.lemsml.visitors.BaseVisitor;

public class BuildNameComponentTypeMapVisitor extends BaseVisitor<Object, Throwable>  {

	private Lems lems;

	public BuildNameComponentTypeMapVisitor(Lems lems) {
		this.lems = lems;
	}

	@Override
	public Lems visit(ComponentType ct) throws Throwable {
		((extended.Lems) this.lems).registerComponentTypeName(ct.getName(), ct);
		return lems;
	}

	public Lems getLems() {
		return lems;
	}

	public void setLems(Lems lems) {
		this.lems = lems;
	}

}
