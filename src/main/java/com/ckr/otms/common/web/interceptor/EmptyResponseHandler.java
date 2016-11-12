package com.ckr.otms.common.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Sometimes, a controler just return nothing. At this moment, the content type of the response will be empty and the program
 * in browser may cannot handle properly. This filter make sure if the content type is empty, set it to "application/json" 
 * @author Administrator
 *
 */
public class EmptyResponseHandler extends HandlerInterceptorAdapter{
	
	private static Logger LOG = LoggerFactory.getLogger(EmptyResponseHandler.class);
	
	@Override
	public void postHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		
		if(response.isCommitted()){
			return;
		}
		
		
		LOG.debug("content type {}", response.getContentType());
		
		
		if(response.getContentType() == null || !response.getContentType().startsWith("application/json") ){
			LOG.debug("change content type to application/json;charset=UTF-8");
			
			response.setContentType("application/json;charset=UTF-8");

		}
		
	}
}
