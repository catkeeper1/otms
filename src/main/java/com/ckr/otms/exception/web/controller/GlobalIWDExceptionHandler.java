package com.ckr.otms.exception.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ckr.otms.common.util.LogMarkerFactory;
import com.ckr.otms.common.web.annotation.InternalWebDataController;
import com.ckr.otms.exception.BaseException;
import com.ckr.otms.exception.BaseException.ExceptionMessage;
import com.ckr.otms.exception.FatalSystemException;
import com.ckr.otms.exception.SystemException;
import com.ckr.otms.exception.valueobject.ErrorResponse;

/*
@ControllerAdvice(annotations= InternalWebDataController.class)
public class GlobalIWDExceptionHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(GlobalIWDExceptionHandler.class);
	
	@ExceptionHandler(value = Throwable.class)
	public @ResponseBody ErrorResponse handleException(final Throwable e){
		return GlobalIWDExceptionHandler.responseForException(e);
	}
	
	public static ErrorResponse responseForException(Throwable e){
		
		LOG.error("handle exception: ", e);
		
		BaseException be = null;
		
		if(e instanceof BaseException ){
			
			be = (BaseException) e;
			
		} else {
			be = (BaseException) getCause(e, BaseException.class);
		}
		
		if(be == null){
			be = new SystemException("System exception.", e);
		}
		
		
		if(be instanceof SystemException){
			
			if(isFatalSystemException(be)){
				
				LOG.error(LogMarkerFactory.SYS_FATAL, "Exception stack is:", be);
				
			}else {
				
				LOG.error("Exception stack is:", be);
				
			}
		}
		
		ErrorResponse result = new ErrorResponse();
		
		result.setExceptionId(be.getExceptionID());
		
		
		
		for(ExceptionMessage expMsg : be.getMessageList() ){
				
			//TODO This should be changed after i18n is implemented.
			result.addMessage(expMsg.getMessageCode(), expMsg.getMessageCode() + expMsg.getMessageParams());
				
		}
			
			
		
		
		return result;
	}
	
	private static boolean isFatalSystemException(Throwable e){
		
		if(getCause(e, FatalSystemException.class) != null ){
			return true;
		}
		
		return false;
	}
	
	
	private static Throwable getCause(Throwable e, Class<?> c){
		
		Throwable tmp = e;
		
		while(tmp != null){
			if(c.isInstance(tmp)){
				return tmp;
			}
			
			tmp = tmp.getCause();
		}
		
		return null;
	}
	
}*/
