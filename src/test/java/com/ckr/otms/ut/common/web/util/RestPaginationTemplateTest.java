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
import static org.junit.Assert.assertTrue;


public class RestPaginationTemplateTest {
	
	private @Mocked RequestContextHolder requestContextHolder;
	
	private @Mocked ServletRequestAttributes servletRequestAttributes;

    private @Mocked HttpServletRequest httpServletRequest;

	@Test
	public void testQuery(){

        final InvokedCounter counter = new InvokedCounter();

        List<RestPaginationTemplate.SortCriteria> sortCriteriaList = new ArrayList<>();

        final RestPaginationTemplate<String> pageUtil = new RestPaginationTemplate<String>(){
            @Override
            protected QueryResponse<String> doQuery() {

                counter.countInvoke("doQuery");

                QueryRequest queryReq = RestPaginationTemplate.getQueryPageInfo();
                assertThat(queryReq.getStart() , is(3L));
                assertThat(queryReq.getEnd() , is(100L));

                assertThat(queryReq.getSortCriteriaList().get(1).isAsc() , is(false));
                assertThat(queryReq.getSortCriteriaList().get(1).getFieldName() , is("sort field 2"));


                QueryResponse<String> result = new QueryResponse<>();
                result.setStart(1L);
                result.setTotal(100L);

                List<String> content = new ArrayList<>();
                for(int i = 0 ; i < 10; i++){
                    content.add("record" + i);
                }

                result.setContent(content);

                return result;
            }
        };

        final RestPaginationTemplateTest t = this;

        final ResponseEntity<Collection<Object>> expectedResult = new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);

        new Expectations(){{

            RequestContextHolder.getRequestAttributes(); result = servletRequestAttributes;

            servletRequestAttributes.getRequest(); result = httpServletRequest;

        }};

        new MockUp<RestPaginationTemplate>() {
            @Mock QueryRequest parsePageRange(QueryRequest range, HttpServletRequest webRequest) {
                range.setStart(3L);
                range.setEnd(100L);

                return range;
            }

            @Mock QueryRequest parseSortBy(QueryRequest request, HttpServletRequest webRequest) {

                List<RestPaginationTemplate.SortCriteria> sortCriteriaList = new ArrayList<>();
                RestPaginationTemplate.SortCriteria criteria = new RestPaginationTemplate.SortCriteria();
                criteria.setAsc(true);
                criteria.setFieldName("sort field 1");
                sortCriteriaList.add(criteria);
                criteria = new RestPaginationTemplate.SortCriteria();
                criteria.setAsc(false);
                criteria.setFieldName("sort field 2");
                sortCriteriaList.add(criteria);

                request.setSortCriteriaList(sortCriteriaList);

                return request;
            }
        };

        ResponseEntity<List<String>> testResult = pageUtil.query();
        assertThat("doQuery is not invoked", counter.getInvokeCount("doQuery"), is(1));

        assertThat(testResult.getStatusCode() , is(HttpStatus.OK) );

        assertThat(testResult.getHeaders().get("Content-Range").get(0) , is("items " + 1 + "-" + 10 + "/" + 100) );

        assertThat(testResult.getBody().get(4), is(("record" + 4)));

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
