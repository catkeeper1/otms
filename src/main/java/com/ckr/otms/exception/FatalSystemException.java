package com.ckr.otms.exception;

public class FatalSystemException extends SystemException {

	private static final long serialVersionUID = -3691123144481103863L;
	

	public FatalSystemException(String arg0) {
		super(arg0);
	}


	public FatalSystemException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}


	public FatalSystemException(Throwable arg0, String msg, Object[] params) {
		super(arg0, msg, params);
	}


	public FatalSystemException(Throwable arg0) {
		super(arg0);
	}
	
	

}
