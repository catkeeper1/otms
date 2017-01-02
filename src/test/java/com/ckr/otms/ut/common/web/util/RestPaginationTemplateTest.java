package com.ckr.otms.ut.common.web.util;

import static mockit.Deencapsulation.invoke;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

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

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


public class RestPaginationTemplateTest {
	
	private @Mocked RequestContextHolder requestContextHolder;
	
	private @Mocked ServletRequestAttributes servletRequestAttributes;

    private @Mocked HttpServletRequest httpServletRequest;

	@Test
	public void testQuery(final @Mocked HttpServletRequest request){


        final RestPaginationTemplate pageUtil = new RestPaginationTemplateForTesting();

        final RestPaginationTemplateTest t = this;

        final ResponseEntity<Collection<Object>> expectedResult = new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);

        new Expectations(RestPaginationTemplate.class){{

            RequestContextHolder.getRequestAttributes(); result = servletRequestAttributes;

            servletRequestAttributes.getRequest(); result = httpServletRequest;

            invoke(pageUtil, "parsePageRange", new Class<?>[]{QueryRequest.class, HttpServletRequest.class}, (QueryRequest)any, httpServletRequest);
            times = 1;

            invoke(pageUtil,"parseSortBy", new Class<?>[]{QueryRequest.class, HttpServletRequest.class}, (QueryRequest)any, httpServletRequest);
            times = 1;

            invoke(pageUtil, "generateResponse", new Class<?>[]{QueryResponse.class}, ((QueryResponse) any)) ;
            result = expectedResult;

        }};


        ResponseEntity<Collection<Object>> testResult = pageUtil.query();

        assertThat(testResult , sameInstance(expectedResult));
		

	}

    @Test
    public void testParsePageRange(){
        doTestParasePageRange("items=1-20", 1l, 20l );
        doTestParasePageRange("items=13-34", 13l, 34l );
        doTestParasePageRange("items=2-9999", 2l, 9999l );
        doTestParasePageRange("items=3", 3l, null );
        doTestParasePageRange("items=", null, null );
    }

    private void doTestParasePageRange(String range, Long start, Long end ){
        new Expectations(){{

            List<String> rangeValues = new ArrayList<>();
            rangeValues.add(range);


            httpServletRequest.getHeaders("Range");
            times = 1;
            result = Collections.enumeration(rangeValues);
        }};

        final RestPaginationTemplate pageUtil = new RestPaginationTemplateForTesting();

        QueryRequest queryRequest = new QueryRequest();
        invoke(pageUtil, "parsePageRange", new Class<?>[]{QueryRequest.class, HttpServletRequest.class}, queryRequest, httpServletRequest);


        assertThat(queryRequest.getStart() , is(start));
        assertThat(queryRequest.getEnd() , is(end));
    }

    public static class RestPaginationTemplateForTesting extends RestPaginationTemplate{

        @Override
        protected QueryResponse doQuery() {
            return null;
        }
    }

}
