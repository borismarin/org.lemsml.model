package org.lemsml.model.compiler.semantic.visitors;

import java.io.File;

import org.lemsml.model.Include;
import org.lemsml.model.compiler.parser.LEMSXMLReader;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.DepthFirstTraverserImpl;
import org.lemsml.visitors.TraversingVisitor;

/**
 * @author borismarin
 *
 */
public class ProcessIncludes extends TraversingVisitor<Boolean, Throwable>
{

	private Lems inputLems;
	private File cwd;
	private File schema;

	/**
	 * @param lems
	 * @param schema
	 * @param cwd
	 */
	public ProcessIncludes(Lems lems, File schema, File cwd)
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
		ProcessIncludes incProcVisitor = new ProcessIncludes(includedLems, schema, cwd);
		incProcVisitor.setTraverseFirst(true);
		includedLems.accept(incProcVisitor);

		// will copy the content of the visited LEMS document to inputLems
		CopyContent extractContentVisitor = new CopyContent(inputLems);
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
