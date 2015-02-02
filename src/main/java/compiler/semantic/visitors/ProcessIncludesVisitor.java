package compiler.semantic.visitors;

import java.io.File;

import org.lemsml.model.Include;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

import compiler.parser.LEMSXMLReader;

import extended.Lems;

/**
 * @author borismarin
 *
 */
public class ProcessIncludesVisitor extends TraversingVisitor<Boolean, Throwable>
{

	private Lems inputLems;
	private File cwd;
	private File schema;

	/**
	 * @param lems
	 * @param schema
	 * @param cwd
	 */
	public ProcessIncludesVisitor(Lems lems, File schema, File cwd)
	{
		super(new DepthFirstTraverserImpl<Throwable>(), new BaseVisitor<Boolean, Throwable>());
		this.inputLems = lems;
		this.cwd = cwd;
		this.schema = schema;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lemsml.visitors.TraversingVisitor#visit(org.lemsml.model.Include)
	 */
	@Override
	public Boolean visit(Include inc) throws Throwable
	{
		File includedFile = new File(cwd.getPath(), inc.getFile());
		Lems includedLems = LEMSXMLReader.unmarshall(includedFile, schema);

		// recursively process inputs
		ProcessIncludesVisitor incProcVisitor = new ProcessIncludesVisitor(includedLems, schema, cwd);
		incProcVisitor.setTraverseFirst(true);
		includedLems.accept(incProcVisitor);

		// will copy the content of the visited LEMS document to inputLems
		CopyContentVisitor extractContentVisitor = new CopyContentVisitor(inputLems);
		extractContentVisitor.setTraverseFirst(true);
		includedLems.accept(extractContentVisitor);

		return true;
	}

	/**
	 * @return
	 */
	public Lems getInputLems()
	{
		return inputLems;
	}

}
