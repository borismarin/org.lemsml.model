package org.lemsml.model.compiler.semantic.visitors;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.lemsml.model.Include;
import org.lemsml.model.compiler.parser.LEMSXMLReader;
import org.lemsml.model.extended.Lems;
import org.lemsml.visitors.BaseVisitor;
import org.lemsml.visitors.TraversingVisitor;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

/**
 * @author borismarin
 *
 */
public class ProcessIncludes extends BaseVisitor<Boolean, Throwable> {

	private Lems inputLems;
	private File cwd;
	private File schema;
	private static final Logger logger = (Logger) LoggerFactory
			.getLogger(ProcessIncludes.class);
	private Set<HashCode> includedFiles = new HashSet<HashCode>();

	public ProcessIncludes(Lems lems, File schema, File cwd,
			Set<HashCode> includedFiles) throws Throwable {
		this.inputLems = lems;
		this.cwd = cwd;
		this.schema = schema;
		this.includedFiles = includedFiles;
	}

	public ProcessIncludes(Lems lems, File schema, File cwd) throws Throwable {
		this(lems, schema, cwd, new HashSet<HashCode>());
	}

	@Override
	public Boolean visit(Include inc) throws Throwable {

		File includedFile = new File(cwd.getPath(), inc.getFile());
		logger.debug("Processing included file {}", includedFile.getName());
		// Make sure that file has not been previously included
		if (registerIncludedFile(includedFile)) {
			Lems includedLems = LEMSXMLReader.unmarshall(includedFile, schema);
			includedLems.setDefinedIn(includedFile);

			// decorate elements to be added with source file
			TraversingVisitor<Boolean, Throwable> addFile = new TraversingVisitor<Boolean, Throwable>(
					new DepthFirstTraverserExt<Throwable>(),
					new DecorateWithSourceFile(includedFile));
			includedLems.accept(addFile);

			// recursively process inputs
			includedLems.accept(new TraversingVisitor<Boolean, Throwable>(new DepthFirstTraverserExt<Throwable>(), new ProcessIncludes(includedLems, schema, cwd,
					this.includedFiles)));

			// will copy the content of the visited LEMS document to inputLems
			TraversingVisitor<Boolean, Throwable> copyContent = new TraversingVisitor<Boolean, Throwable>(
					new DepthFirstTraverserExt<Throwable>(), new CopyContent(inputLems));
			includedLems.accept(copyContent);
		} else {
			logger.warn("Skipping double inclusion of file {}",
					includedFile.getName());
		}

		return true;
	}

	private Boolean registerIncludedFile(File incFile) throws IOException {
		HashCode hc = Files.hash(incFile, Hashing.md5());
		return this.includedFiles.add(hc);
	}

	public Lems getInputLems() {
		return this.inputLems;
	}

}
