package com.ckr.otms.common.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ckr.otms.common.web.constant.RequestPathConstant;
import com.ckr.otms.secuirty.constant.SecuriedAttribute;

@Controller
public class EmptyContoller {
	
	private static Logger LOG = LoggerFactory.getLogger(EmptyContoller.class);
	
	
	
	
	static {
		
		
	}
	
	/**
	 * 
	 * Sometimes, we do not a gridx table to show something when the screen is created. The solution is
	 * let the JsonRest used by that gridx table point to a URL that return a empty json array.
	 * This method is used for this purpose. It always return a empty array and the expiried time for the 
	 * response will be very long so that browser will not request this again and again.
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping(value= RequestPathConstant.INTERNAL_WEB_DATA + "/emptyResponse")
	@Secured(SecuriedAttribute.ATT_AUTHENTICATED)
	public @ResponseBody ResponseEntity<Object> emptyResponse(HttpServletResponse response){
		
		response.setDateHeader("Last-Modified", 1000000000L);
		
		// expires time will be 1 year later.
		int expiryDuration = 3600 * 24 * 365;
		
		ResponseEntity<Object> emptyResponse = null;
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.setCacheControl("max-age="+expiryDuration);
		headers.set("Content-Range", "items " + 
		             0 +"-" + 
		             0 +"/" +
		             0);
		
		emptyResponse = new ResponseEntity<Object>(new ArrayList<Object>(), headers, HttpStatus.OK);
		
		return emptyResponse;
	}
}
