package com.ckr.otms.ut.common.web.util;

import static mockit.Deencapsulation.invoke;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.ckr.otms.common.web.util.RestPaginationTemplate;
import com.ckr.otms.common.web.util.RestPaginationTemplate.QueryRequest;
import com.ckr.otms.common.web.util.RestPaginationTemplate.QueryResponse;


import mockit.Expectations;
import mockit.Mocked;


@RunWith(JMockit.class)
public class RestPaginationTemplateTest {
	
	private @Mocked RequestContextHolder requestContextHolder;
	
	private @Mocked ServletRequestAttributes servletRequestAttributes;
	
	@Test
	public void testParsePageRange(final @Mocked HttpServletRequest request){
		
		final RestPaginationTemplate pageUtil = new RestPaginationTemplate(){

			@Override
			protected QueryResponse doQuery() {
				// TODO Auto-generated method stub
				return null;
			}

			 
		};
		
		final RestPaginationTemplateTest t = this;
		
		final ResponseEntity<Collection<Object>> expectedResult = new ResponseEntity<Collection<Object>>(new ArrayList<Object>(), HttpStatus.OK);
		
		new Expectations(){{
			
			RequestContextHolder.getRequestAttributes(); result = servletRequestAttributes;
			
			servletRequestAttributes.getRequest(); result = request;
			
		}};
		
		
		new Expectations(RestPaginationTemplate.class){{


			//invoke(pageUtil, "parsePageRange", new Class<?>[]{QueryRequest.class, HttpServletRequest.class}, (QueryRequest)any, request);
			//times = 1;

			//invoke(pageUtil,"parseSortBy", new Class<?>[]{QueryRequest.class, HttpServletRequest.class}, (QueryRequest)any, request);
			//times = 1;

			pageUtil.generateResponse((QueryResponse) any);
			this.result = expectedResult;
			
		}};
		
		ResponseEntity<Collection<Object>> testResult = pageUtil.query();
		
		
		
		assertThat(testResult , sameInstance(expectedResult));
		
		
	}
}
