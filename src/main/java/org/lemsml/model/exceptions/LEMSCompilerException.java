package org.lemsml.model.exceptions;


public class LEMSCompilerException extends Exception {

	private static final long serialVersionUID = 1L;

	public LEMSCompilerError errorCode;

	public LEMSCompilerException(String message, LEMSCompilerError err) {
		super(message);
		this.errorCode = err;
	}

}
