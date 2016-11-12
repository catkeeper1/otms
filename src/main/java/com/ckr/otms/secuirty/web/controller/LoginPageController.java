package com.ckr.otms.secuirty.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ckr.otms.common.web.constant.RequestPathConstant;
import com.ckr.otms.common.web.controller.InternalWebDataController;
import com.ckr.otms.exception.valueobject.ErrorResponse;
import com.ckr.otms.secuirty.constant.SecuriedAttribute;




@Controller
public class LoginPageController {
	
	private static final String LOGIN_PAGE_URL = "/loginPage"+ RequestPathConstant.PAGE;
	
	//private static final String SEND_LOGIN_REQUEST_URL = "/sendLoginRequest"+ RequestPathConstant.PAGE;
	
	@RequestMapping(value= LOGIN_PAGE_URL )
	public ModelAndView openLoginPage(){
		
		return new ModelAndView("loginPage");
		
	}
	
	
	@RequestMapping(value= "/startAuthentication"+ RequestPathConstant.PAGE )
	public ModelAndView startAuthentication(HttpServletRequest request, HttpServletResponse response){
		
		ModelAndView result = null;
		
		if(RequestPathConstant.isInternalWebData(request)){
			
			ErrorResponse errorResponse = new ErrorResponse();
			
			errorResponse.addMessage("sys.error.need_authentication", null, request);
		
			result = InternalWebDataController.generateModelAndView(errorResponse);
			
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			
			response.setHeader("needAuthentication", "true");
			
		} else {
			result = new ModelAndView();
			result.setViewName("redirect:" + LOGIN_PAGE_URL +"?msg=TO");
		}
		return result;
		
		
	}
	
	
	@RequestMapping(value= "/loginFailure"+ RequestPathConstant.PAGE )
	public String loginFailure(HttpServletRequest request){
		
		Object ae = request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		
		String msg = "";
		
		if(ae instanceof AuthenticationException){
			
			AuthenticationException authException = (AuthenticationException) ae;
			
			msg = getLoginMsgFromException(authException);
			
		}
				
		return "redirect:" + LOGIN_PAGE_URL +"?msg="+msg+ "&username="+ request.getParameter("username");
	}
	
	private String getLoginMsgFromException(AuthenticationException authException){
		
		if(authException instanceof CredentialsExpiredException){
			return "CE";
		}
		
		if(authException instanceof AccountExpiredException){
			return "AE";
		}
		
		if(authException instanceof DisabledException){
			return "DB";
		}
		
		if(authException instanceof LockedException){
			return "LK";
		}
		
		if(authException instanceof AuthenticationServiceException){
			return "AS";
		}
		
		//means bad credential
		return "BC";
	}
	
	/*
	@RequestMapping(value= SEND_LOGIN_REQUEST_URL)
	public @ResponseBody ErrorResponse sendLoginRequest(HttpServletResponse response){
		ErrorResponse result = new ErrorResponse();
		
		
		result.addMessage("error.need_authentication", "Please login again.");
		result.setNeedAuthentication(Boolean.TRUE);
		
		return result;
	}*/
	
	
	@RequestMapping(value= "/mainPage"+ RequestPathConstant.PAGE )
	@Secured(SecuriedAttribute.ATT_AUTHENTICATED)
	public ModelAndView openMainPage(){
		
		return new ModelAndView("mainPage");
		
	}
	
	/*
	@RequestMapping(value= "/blocking"+ RequestPathConstant.PAGE )
	public ModelAndView blocking(){
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		return null;
		
	}
	*/
}
