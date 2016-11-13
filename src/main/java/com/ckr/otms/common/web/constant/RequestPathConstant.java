package com.ckr.otms.common.web.constant;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;


public class RequestPathConstant {
	public static final String PAGE = ".do";
	
	public static final String PUBLIC_RESOURCE ="/pub";
	
	public static final String INTERNAL_WEB_DATA ="/iwd";
	
	public static final String EXTERNAL_WEB_SERVICE ="/ews";
	
	
	
	private static boolean determinChannel(HttpServletRequest request, String prefix){
		String conPath = request.getContextPath();
		
		String uri = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
		
		if(uri == null || "".equals(uri.trim())){
			uri = request.getRequestURI();
			
		}
		
		return uri.startsWith(conPath + prefix);
	}
	
	public static boolean isInternalWebData(HttpServletRequest request){
		return determinChannel(request,  INTERNAL_WEB_DATA);
	}
	
	public static boolean isPublicResource(HttpServletRequest request){
		return determinChannel(request,  PUBLIC_RESOURCE);
	}
	
}
