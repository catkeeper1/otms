package com.ckr.otms.common.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

@Component
@Lazy(false)
public class MessageResourceHolder {
	
	
	private static AbstractMessageSource messageSource;
	
	
	@Autowired
	@Qualifier("messageSource")
	public static void setMessageSource(AbstractMessageSource messageSource) {
		MessageResourceHolder.messageSource = messageSource;
	}

	public static String getMessage(String code, Object[] args, HttpServletRequest request){
		
		return messageSource.getMessage(code, 
										args, 
										RequestContextUtils.getLocale(request));
	}
	
	public static String getMessage(String code,  HttpServletRequest request){
		
		return messageSource.getMessage(code, 
										null,  
										RequestContextUtils.getLocale(request));
	}
	
}
