package org.lemsml.model.extended;

import java.io.File;

import javax.xml.bind.annotation.XmlTransient;

import org.lemsml.model.compiler.IHasParentFile;
import org.lemsml.model.exceptions.LEMSCompilerError;

@XmlTransient
public class LemsNode implements IHasParentFile{
	@XmlTransient
	private LEMSCompilerError error;
	//TODO: ugly.
	@XmlTransient
	private File definedIn = new File("");

	public LEMSCompilerError getError() {
		return error;
	}
	public void setError(LEMSCompilerError error) {
		this.error = error;
	}
	public File getDefinedIn() {
		return definedIn;
	}
	public void setDefinedIn(File includedFile) {
		this.definedIn = includedFile;
	}

}
