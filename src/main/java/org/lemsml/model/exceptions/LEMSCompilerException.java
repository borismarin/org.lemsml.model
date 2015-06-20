package org.lemsml.model.exceptions;


public class LEMSCompilerException extends Exception {

	private static final long serialVersionUID = 1L;

	private LEMSCompilerError errorCode;

	public LEMSCompilerException(String message, LEMSCompilerError err) {
		super(String.format("LEMS error [%s]: ", err) + message);
		this.errorCode = err;
	}

}
