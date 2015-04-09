package org.lemsml.model.extended;

import org.lemsml.model.exceptions.LEMSCompilerError;

public class LemsNode {
	private LEMSCompilerError error;
	private String definedIn;

	public LEMSCompilerError getError() {
		return error;
	}
	public void setError(LEMSCompilerError error) {
		this.error = error;
	}
	public String getDefinedIn() {
		return definedIn;
	}
	public void setDefinedIn(String definedIn) {
		this.definedIn = definedIn;
	}

}
