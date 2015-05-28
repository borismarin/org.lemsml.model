package org.lemsml.model.compiler;

import java.io.File;

import javax.xml.bind.annotation.XmlTransient;

public interface IHasParentFile {
	@XmlTransient
	public File getDefinedIn();

}
