package org.lemsml.model.test;

import java.io.File;

public class BaseTest {
	protected File getLocalFile(String fname) {
		return new File(getClass().getResource(fname).getFile());
	}
}
