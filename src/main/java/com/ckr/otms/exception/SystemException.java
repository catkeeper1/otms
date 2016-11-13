package com.ckr.otms.exception;



public class SystemException extends BaseException {

	private static final long serialVersionUID = 1087557111023898204L;

	public SystemException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SystemException(String arg0) {
		super(arg0);
	}

	public SystemException(Throwable arg0) {
		super(arg0);
	}
	
	public SystemException(Throwable arg0, String msg, Object[] params) {
		super(arg0);
		this.addMessage(msg, params);
	}
	
}
