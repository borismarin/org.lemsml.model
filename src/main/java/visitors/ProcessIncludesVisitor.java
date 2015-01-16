package visitors;

import java.io.File;

import org.lemsml.model.ComponentType;
import org.lemsml.model.Constant;
import org.lemsml.model.Include;
import org.lemsml.model.Lems;
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
		System.out.println("found include " + inc.toString());
		File included = new File(cwd.getPath(), inc.getFile());
		extended.Lems includedLems = LemsXmlUtils.unmarshall(included, schema);

		TraversingVisitor<Object, Throwable> tv = new TraversingVisitor<Object, Throwable>(
				new DepthFirstTraverserImpl<Throwable>(), this);
		tv.setTraverseFirst(true);
		includedLems.accept(tv);	

		return unextendedLems;
	}

	public Lems visit(Constant constant) throws Throwable {
		System.out.println("#found constant " + constant.toString());
		resolvedLems.getConstant().add(constant);

		return unextendedLems;
	}

	public Lems visit(ComponentType componentType) throws Throwable {
		System.out.println("#found component " + componentType.toString());
		resolvedLems.getComponentType().add(componentType);

		return unextendedLems;
	}
	//TODO: add visit for all other lems subelements

	public Lems getUnextendedLems() {
		return unextendedLems;
	}

	public extended.Lems getResolvedLems() {
		return resolvedLems;
	}

}
