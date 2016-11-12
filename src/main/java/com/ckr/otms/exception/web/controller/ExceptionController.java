package com.ckr.otms.exception.web.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ckr.otms.common.util.LogMarkerFactory;
import com.ckr.otms.common.web.constant.RequestPathConstant;
import com.ckr.otms.common.web.controller.InternalWebDataController;
import com.ckr.otms.exception.BaseException;
import com.ckr.otms.exception.BaseException.ExceptionMessage;
import com.ckr.otms.exception.FatalSystemException;
import com.ckr.otms.exception.SystemException;
import com.ckr.otms.exception.valueobject.ErrorResponse;

@Controller
public class ExceptionController {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExceptionController.class);
	
	public static final String ERROR_RESPONSE = "errorResponse";
	/*
	@RequestMapping(value="/errorHandler", produces="text/html")
	public ModelAndView handlePageError(HttpServletRequest request, HttpServletResponse response){
		
		handleNonPageError(request, response);
			
		ModelAndView mv = new ModelAndView();
			
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
			
		mv.setViewName("invalidHttpStatus");
		mv.addObject("httpStatus", status);
			
		return mv;
				
		
	}
	
	@RequestMapping(value="/errorHandler", produces="!text/html")
	public ModelAndView handleNonPageError(HttpServletRequest request, HttpServletResponse response){
		
		Throwable e = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
		
		if(e != null){
					
			handleException(e, response);
		}
		
	}*/
	
	
	@RequestMapping(value="/errorHandler")
	public ModelAndView handleAllError(HttpServletRequest request, HttpServletResponse response){
		
		Throwable e = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
		
		if(e != null){
			
			return handleException(e, request, response);
			
		} else {
			
			return handleNoException(request, response);
			
		}
		
	}
	
	
	private ModelAndView handleNoException(HttpServletRequest request, HttpServletResponse response){
		
		//LOG.error(LogMarkerFactory.SYS_FATAL, "Unhandled system exception: " , e);
		Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		
		LOG.warn("HTTP Status {}." , statusCode);
		
		if(RequestPathConstant.isPublicResource(request) ||
		   RequestPathConstant.isInternalWebData(request)){
			return null;
		}
		
		ModelAndView modelAndView = new ModelAndView("invalidHttpStatus");
		modelAndView.addObject("statusCode", statusCode);
		return modelAndView;
	}
	

	
	private ModelAndView handleException(Throwable e, HttpServletRequest request, HttpServletResponse response){
		
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
		
		//if the uri is /pub, do not response any content since these request are for static resource downloading only.
		if(RequestPathConstant.isPublicResource(request)){
			return null;
		}
		
		ErrorResponse result = new ErrorResponse();
		
		if(be instanceof SystemException){
			result.setExceptionId(be.getExceptionID());
		}
		
		for(ExceptionMessage expMsg : be.getMessageList() ){
				
			
			result.addMessage(expMsg.getMessageCode(),
							  expMsg.getMessageParams(), 
						      request);
								
				
		}
			
		
		
		ModelAndView modelAndView = new ModelAndView();
		
		
		
		if(RequestPathConstant.isInternalWebData(request)){
			
			//header "isSystemException" = true and status = SC_INTERNAL_SERVER_ERROR means this
			//response is return error info. Client side program should handle it as an error.
			response.setHeader("isSystemException", "true");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			
			//return the data
			return InternalWebDataController.generateModelAndView(result);
			
		} else {
			//forward to an screen that show the error message. 
			modelAndView.addObject(ERROR_RESPONSE, result);
			modelAndView.setViewName("errorPage");
		}
		
		return modelAndView;
	}
	
	/*
	@RequestMapping(value="/generateErrorResponse")
	public @ResponseBody ErrorResponse generateErrorResponse(HttpServletRequest request){
		return (ErrorResponse) request.getAttribute(ERROR_RESPONSE);
	}*/
	
	private boolean isFatalSystemException(Throwable e){
		
		if(getCause(e, FatalSystemException.class) != null ){
			return true;
		}
		
		return false;
	}
	
	
	private Throwable getCause(Throwable e, Class<?> c){
		
		Throwable tmp = e;
		
		while(tmp != null){
			if(c.isInstance(tmp)){
				return tmp;
			}
			
			tmp = tmp.getCause();
		}
		
		return null;
	}
	
}
