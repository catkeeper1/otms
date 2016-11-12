package com.ckr.otms.exception.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.ckr.otms.common.web.annotation.PageController;
import com.ckr.otms.exception.valueobject.ErrorResponse;

/*@ControllerAdvice(annotations=PageController.class)
public class GlobalExceptionHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(value = Throwable.class)
	public ModelAndView handleException(final Throwable e){
		
		LOG.error("Exception is found.");
		
		ErrorResponse result = GlobalIWDExceptionHandler.responseForException(e);
		
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("errorPage");
		mv.addObject("resut", result);
		
		return mv;
		
	}
	
}
*/