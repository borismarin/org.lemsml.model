package visitors;

import java.io.File;

import org.lemsml.model.ComponentType;
import org.lemsml.model.Constant;
import org.lemsml.model.Include;
import org.lemsml.model.Lems;
import org.lemsml.model.Target;
import org.lemsml.model.Unit;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

import parser.LemsXmlUtils;

public class ProcessIncludesVisitor extends BaseVisitor<Object, Throwable> {

	private Lems unextendedLems;
	private File cwd;
	private File schema;
	private extended.Lems resolvedLems = new extended.Lems();

	public ProcessIncludesVisitor(extended.Lems lems, File schema, File cwd) {
		this.unextendedLems = (Lems) lems;
		this.cwd = cwd;
		this.schema = schema;
		lems.copyTo(this.resolvedLems);
	}

	@Override
	public Lems visit(Include inc) throws Throwable {
		File included = new File(cwd.getPath(), inc.getFile());
		extended.Lems includedLems = LemsXmlUtils.unmarshall(included, schema);

		TraversingVisitor<Object, Throwable> tv = new TraversingVisitor<Object, Throwable>(
				new DepthFirstTraverserImpl<Throwable>(), this);
		tv.setTraverseFirst(true);
		includedLems.accept(tv);

		return unextendedLems;
	}

	@Override
	public Lems visit(Constant constant) throws Throwable {
		resolvedLems.getConstant().add(constant);
		return unextendedLems;
	}

	@Override
	public Lems visit(ComponentType componentType) throws Throwable {
		resolvedLems.getComponentType().add(componentType);
		return unextendedLems;
	}

	@Override
	public Lems visit(extended.Component component) throws Throwable {
		resolvedLems.getComponent().add(component);
		return unextendedLems;
	}

	@Override
	public Lems visit(Target target) throws Throwable {
		resolvedLems.getTarget().add(target);
		return unextendedLems;
	}

	@Override
	public Lems visit(extended.Dimension dimension) throws Throwable {
		resolvedLems.getDimension().add(dimension);
		return unextendedLems;
	}

	@Override
	public Lems visit(Unit unit) throws Throwable {
		resolvedLems.getUnit().add(unit);
		return unextendedLems;
	}

	public Lems getUnextendedLems() {
		return unextendedLems;
	}

	public extended.Lems getResolvedLems() {
		return resolvedLems;
	}

}
