package com.ckr.otms.ut.common.web.util;

import static mockit.Deencapsulation.invoke;


import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.ckr.otms.ut.common.InvokedCounter;
import mockit.*;


import org.hibernate.annotations.Cascade;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import com.ckr.otms.common.web.util.RestPaginationTemplate;
import com.ckr.otms.common.web.util.RestPaginationTemplate.QueryRequest;
import com.ckr.otms.common.web.util.RestPaginationTemplate.QueryResponse;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


public class RestPaginationTemplateTest {
	
	private @Mocked RequestContextHolder requestContextHolder;
	
	private @Mocked ServletRequestAttributes servletRequestAttributes;

    private @Mocked HttpServletRequest httpServletRequest;

	@Test
	public void testParsePageRange(){


		final ResponseEntity<Collection<Object>> expectedResult = new ResponseEntity<Collection<Object>>(new ArrayList<Object>(), HttpStatus.OK);

        final HttpServletRequest httpReq = new MockUp<HttpServletRequest>(){}.getMockInstance();

		new Expectations(){{

            ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

            result = httpReq;

		}};


        final InvokedCounter counter = new InvokedCounter();

        final RestPaginationTemplate pageUtil = new MockUp<RestPaginationTemplate<Object>>(){
            @Mock
            protected QueryResponse<Object> doQuery(){
                return new QueryResponse<Object>();
            }

            @Mock
            protected QueryRequest parsePageRange(QueryRequest range, HttpServletRequest webRequest){
                counter.countInvoke("parsePageRange");
                return range;
            }

            @Mock
            protected QueryRequest parseSortBy(QueryRequest request, HttpServletRequest webRequest){
                counter.countInvoke("parseSortBy");
                return request;
            }

            @Mock
            ResponseEntity<Collection<Object>> generateResponse(QueryResponse<Object> contentRange){
                return expectedResult;
            }

        }.getMockInstance();

		ResponseEntity<Collection<Object>> testResult = pageUtil.query();

        assertThat(testResult, sameInstance(expectedResult));

        assertThat(counter.getInvokeCount("parsePageRange"), equalTo(1));
        assertThat(counter.getInvokeCount("parseSortBy"), equalTo(1));
		

	}



}
