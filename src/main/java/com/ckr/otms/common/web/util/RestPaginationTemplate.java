package com.ckr.otms.common.web.util;


import com.ckr.otms.common.util.LongUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;

/**
 * This is a util class that implement pagination query protocol for dojo JsonRest under Spring MVC environment.
 * <p>This class is designed with GOF template method pattern. It extract pagination
 * info(such as the no of pages, sorting fields, etc) from HTTP request and store this info
 * in a {@link QueryRequest} object. Then, pass this {@link QueryRequest} object to
 * {@link RestPaginationTemplate#doQuery()} to do real data query. Since query logic is requirement specified,
 * {@link RestPaginationTemplate#doQuery()} is abstract and developer need to extend this
 * class and provide implementation for {@link RestPaginationTemplate#doQuery()}.</p>
 * <p>The {@link RestPaginationTemplate#query()} method can return a {@link ResponseEntity} object which can
 * be used as returned value for a Spring MVC controller method. The usage of this class is similar to:</p>
 * <pre>
 * <code>
 *  return new RestPaginationTemplate&lt;ResultType&gt;() {
 *      &#064;Override
 *      protected QueryResponse&lt;ResultType&gt; doQuery() {
 *          QueryResponse&lt;ResultType&gt; result = ...; //statement that can do real data query.
 *          return result;
 *      }
 *  }.query();
 * </code>
 * </pre>
 * <p>Please note that this class is designed for Spring MVC only, it will not work if you use it under other
 * environments(such as Servlet, Struts).</p>
 *
 * @param <R> The type of records of query result.
 *
 */

public abstract class RestPaginationTemplate<R> {

    private static final Logger LOG = LoggerFactory.getLogger(RestPaginationTemplate.class);

    private static final ThreadLocal<QueryRequest> queryPagingInfo =
            new ThreadLocal<>();

    /**
     * Trigger the query base on the query logic that is implemented in
     * {@link com.ckr.otms.common.web.util.RestPaginationTemplate#doQuery()}.
     *
     *
     *
     * @return a response entity object that can be returned by a controller to generate a
     * proper json response for JsonRest for current query.
     *
     * @see com.ckr.otms.common.web.util.RestPaginationTemplate#doQuery()
     */
    public ResponseEntity<Collection<R>> query() {

        HttpServletRequest webRequest =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();


        //request.getHeaders().get
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setStart((long) 0);
        //queryRequest.setQueryString(queryString);
        //queryRequest.setQueryParams(params);

        if (webRequest != null) {

            queryRequest = parsePageRange(queryRequest, webRequest);
            queryRequest = parseSortBy(queryRequest, webRequest);
        }


        ResponseEntity<Collection<R>> result = null;

        try {
            queryPagingInfo.set(queryRequest);

            result = generateResponse(doQuery());

        } finally {
            queryPagingInfo.remove();
        }

        return result;

    }

    public static QueryRequest getQueryPageInfo() {
        return queryPagingInfo.get();
    }

    /**
     * Th
     *
     * @return QueryResponse
     */
    protected abstract QueryResponse<R> doQuery();


    protected QueryRequest parsePageRange(QueryRequest range, HttpServletRequest webRequest) {


        Enumeration<String> rangeValues = webRequest.getHeaders("Range");

        if (rangeValues == null) {
            return range;
        }

        while (rangeValues.hasMoreElements()) {

            String rangeStr = rangeValues.nextElement();

            if (!rangeStr.startsWith("items=")) {
                continue;
            }

            StringTokenizer tokenizer = new StringTokenizer(rangeStr.substring("items=".length()), "-");

            if (tokenizer.hasMoreTokens()) {
                String startStr = tokenizer.nextToken();

                Long start = LongUtil.parse(startStr);

                if (start == null) {
                    return null;
                }

                range.setStart(start);
            }

            if (tokenizer.hasMoreTokens()) {
                String startStr = tokenizer.nextToken();

                Long end = LongUtil.parse(startStr);

                if (end != null) {
                    range.setEnd(end);
                }

            }


            break;

        }

        return range;
    }


    protected QueryRequest parseSortBy(QueryRequest request, HttpServletRequest webRequest) {

        String sortByStr = webRequest.getParameter("restSortBy");

        if (sortByStr == null) {
            request.setSortCriteriaList(new ArrayList<SortCriteria>());
            return request;
        }

        LOG.debug("parseSortBy(). sortByStr = {}", sortByStr);


        StringTokenizer tokenizer = new StringTokenizer(sortByStr, ",");

        List<SortCriteria> sortCriteriaList = new ArrayList<SortCriteria>();

        while (tokenizer.hasMoreTokens()) {
            String criteriaStr = tokenizer.nextToken();

            if (criteriaStr.length() <= 1) {
                LOG.error("invlaid sort critiera:{}", criteriaStr);
            }

            SortCriteria sortCriteria = new SortCriteria();

            if (criteriaStr.startsWith(" ") || criteriaStr.startsWith("+")) {
                sortCriteria.setAsc(true);

            } else if (criteriaStr.startsWith("-")) {
                sortCriteria.setAsc(false);
            } else {
                LOG.error("invlaid sort critiera:{}", criteriaStr);
                continue;
            }

            sortCriteria.setFieldName(criteriaStr.substring(1));

            sortCriteriaList.add(sortCriteria);
        }

        request.setSortCriteriaList(sortCriteriaList);

        return request;
    }


    private ResponseEntity<Collection<R>> generateResponse(QueryResponse<R> contentRange) {


        HttpHeaders headerMap = new HttpHeaders();


        headerMap.add("Content-Range", "items " + contentRange.getStart() + "-"
                + (contentRange.getStart() + contentRange.getContent().size() - 1) + "/"
                + contentRange.getTotal());

        return new ResponseEntity<Collection<R>>(contentRange.getContent(), headerMap, HttpStatus.OK);

    }


    public static class QueryRequest {

        private Long start;
        private Long end;


        List<SortCriteria> sortCriteriaList = new ArrayList<SortCriteria>();

        public Long getStart() {
            return start;
        }

        public void setStart(Long start) {
            this.start = start;
        }

        public Long getEnd() {
            return end;
        }

        public void setEnd(Long end) {
            this.end = end;
        }

        public void addSortCriteria(boolean isAsc, String fieldName) {
            SortCriteria criteria = new SortCriteria();
            criteria.setAsc(isAsc);
            criteria.setFieldName(fieldName);
            this.sortCriteriaList.add(criteria);
        }

        public List<SortCriteria> getSortCriteriaList() {
            return sortCriteriaList;
        }

        public void setSortCriteriaList(List<SortCriteria> sortCriteriaList) {
            this.sortCriteriaList = sortCriteriaList;
        }


    }

    public static class SortCriteria {
        private boolean isAsc;
        private String fieldName;

        public boolean isAsc() {
            return isAsc;
        }

        public void setAsc(boolean isAsc) {
            this.isAsc = isAsc;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }


    }

    public static class QueryResponse<C> {

        private Long start;
        private Long total;

        private Collection<C> content;

        public Long getStart() {
            return start;
        }

        public void setStart(Long start) {
            this.start = start;
        }

        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        public Collection<C> getContent() {
            return content;
        }

        public void setContent(Collection<C> content) {
            this.content = content;
        }


    }

}
