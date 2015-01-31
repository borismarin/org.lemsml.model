package compiler.semantic.visitors;

import java.io.File;

import org.lemsml.model.Include;
import org.lemsml.model.Lems;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

import compiler.parser.LEMSXMLReader;

/**
 * @author borismarin
 *
 */
public class ProcessIncludesVisitor extends TraversingVisitor<Boolean, Throwable>
{

	private Lems unextendedLems;
	private File cwd;
	private File schema;
	private extended.Lems resolvedLems = new extended.Lems();

	

	/**
	 * @param lems
	 * @param schema
	 * @param cwd
	 */
	public ProcessIncludesVisitor(extended.Lems lems, File schema, File cwd)
	{
		super(new DepthFirstTraverserImpl<Throwable>(), new BaseVisitor<Boolean, Throwable>());
		this.unextendedLems = (Lems) lems;
		this.cwd = cwd;
		this.schema = schema;
		lems.copyTo(this.resolvedLems);
	}

	/* (non-Javadoc)
	 * @see org.lemsml.visitors.TraversingVisitor#visit(org.lemsml.model.Include)
	 */
	@Override
	public Boolean visit(Include inc) throws Throwable
	{
		File included = new File(cwd.getPath(), inc.getFile());
		extended.Lems includedLems = LEMSXMLReader.unmarshall(included, schema);
		
		ProcessIncludesVisitor incProcVisitor = new ProcessIncludesVisitor(resolvedLems, schema, cwd);
		incProcVisitor.setTraverseFirst(true);
		includedLems.accept(incProcVisitor);
		
		//will copy the content of the visited LEMS document to resolvedLems
		CopyContentVisitor extractContentVisitor = new CopyContentVisitor(resolvedLems);
		extractContentVisitor.setTraverseFirst(true);
		includedLems.accept(extractContentVisitor);
		
		
		return true;
	}

	/**
	 * @return
	 */
	public Lems getUnextendedLems()
	{
		return unextendedLems;
	}

	/**
	 * @return
	 */
	public extended.Lems getResolvedLems()
	{
		return resolvedLems;
	}

}
