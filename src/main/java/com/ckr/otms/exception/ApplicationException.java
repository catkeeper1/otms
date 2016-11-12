package com.ckr.otms.exception;

public class ApplicationException extends BaseException {

	private static final long serialVersionUID = 1799296168836812569L;

	


	public ApplicationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		addMessage(arg0, null);
	}

	public ApplicationException(String arg0) {
		super(arg0);
		addMessage(arg0, null);
	}
	
	public ApplicationException(String arg0, Object[] params, Throwable arg1) {
		super(arg0, arg1);
		addMessage(arg0, params);
	}

	public ApplicationException(String arg0,Object[] params) {
		super(arg0);
		addMessage(arg0, params);
	}
	

}
