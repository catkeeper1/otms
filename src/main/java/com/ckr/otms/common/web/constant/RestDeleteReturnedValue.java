package com.ckr.otms.common.web.constant;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RestDeleteReturnedValue extends ResponseEntity<Object> {

	private RestDeleteReturnedValue(Object body, HttpStatus statusCode) {
		super(body, statusCode);
	}

	public static RestDeleteReturnedValue SINGLETON = null;
	
	static {
		//this emptyObject will be used as body for the response so that the Converter will generate valid json response
		Object emptyObject = new HashMap<String, String>();
		
		//return HTTP status 204 to the client side.
		SINGLETON = new RestDeleteReturnedValue(emptyObject, HttpStatus.NO_CONTENT);
	}
	
	
}