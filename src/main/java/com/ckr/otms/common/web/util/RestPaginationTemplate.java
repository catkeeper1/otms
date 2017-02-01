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
     * Conduct the query base on the query logic that is implemented in
     * {@link com.ckr.otms.common.web.util.RestPaginationTemplate#doQuery()}.
     *
     * @return a response entity object that can be returned by a Spring MVC controller to generate a
     *     proper json response for JsonRest for current query.
     *
     * @see com.ckr.otms.common.web.util.RestPaginationTemplate#doQuery()
     */
    public ResponseEntity<List<R>> query() {

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


        ResponseEntity<List<R>> result = null;

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
     * The method that implement real query logic.
     * When this class is used, this method must be overridden to implement real query logic.
     * In this method, query request(such as the range of records) is available. Developer can use
     * {@link QueryRequest#getQueryPageInfo()}.
     *
     * @return QueryResponse the raw data that will be used to generate HTTP response
     * @see QueryResponse
     */
    protected abstract QueryResponse<R> doQuery();


    private QueryRequest parsePageRange(QueryRequest range, HttpServletRequest webRequest) {


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
            request.setSortCriteriaList(new ArrayList<>());
            return request;
        }

        LOG.debug("parseSortBy(). sortByStr = {}", sortByStr);


        StringTokenizer tokenizer = new StringTokenizer(sortByStr, ",");

        List<SortCriteria> sortCriteriaList = new ArrayList<>();

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


    private ResponseEntity<List<R>> generateResponse(QueryResponse<R> contentRange) {


        HttpHeaders headerMap = new HttpHeaders();


        headerMap.add("Content-Range", "items " + contentRange.getStart() + "-"
                + (contentRange.getStart() + contentRange.getContent().size() - 1) + "/"
                + contentRange.getTotal());

        return new ResponseEntity<>(contentRange.getContent(), headerMap, HttpStatus.OK);

    }

    /**
     * This is used to store the query raw data(the range of records that should be returned).
     * {@link RestPaginationTemplate} extract query data from HTTP request objects and store in a object of this
     * class. When developers implement {@link RestPaginationTemplate#doQuery()}, they just need to get the
     * query raw data from this class but not HTTP request so that it will not coupled with any thing in controller
     * layer.
     */
    public static class QueryRequest {

        /**
         * This is used to specified the range of records should be returned by the query.
         * If start = 11 and end = 20, it means it is expected that this query should return records from
         * 11th record to 20th record.
         */
        private Long start;

        /**
         * This is used to specify the range of records should be returned by the query.
         * @see QueryRequest#start
         */
        private Long end;

        /**
         * This is used to specify how sorting should be done.
         * Assume there are 2 records in this fields(record1 and record2). record1.isAsc = true,
         * record1.fieldName = "abc, record2.isAsc = false, record2.fieldName = "def". It means something like:
         * "SELECT ... FROM ... ORDER BY abc DESC, def ASC".
         *
         */
        private List<SortCriteria> sortCriteriaList = new ArrayList<>();

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


        public List<SortCriteria> getSortCriteriaList() {
            return sortCriteriaList;
        }

        public void setSortCriteriaList(List<SortCriteria> sortCriteriaList) {
            this.sortCriteriaList = sortCriteriaList;
        }


    }

    /**
     * This is used by the {@link QueryRequest} to store information about sorting.
     */
    public static class SortCriteria {
        /**
         * Indicate sorting will be done with asc or desc. True means asc.
         */
        private boolean isAsc;

        /**
         * the field used for sorting.
         */
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

    /**
     * This is used to decouple the query result from HTTP response. Objects of this class are used to store raw data
     * of query result. {@link RestPaginationTemplate#doQuery()} should return an instance of this class
     * and the {@link RestPaginationTemplate#query()} will use this object to
     * generate an response that can be returned by a Spring MVC controller method.
     * @param <C> The data type of the records of query result.
     */
    public static class QueryResponse<C> {

        /**
         * {@link QueryRequest#start} and {@link QueryRequest#end} is used to specify the range of records the caller
         * want to retrieve. However, it is possible that caller want to retrieve records 100 to 110 but there is only
         * 104 records available in total. At this moment, the {@link RestPaginationTemplate#doQuery()} should return
         * an object with {@link QueryResponse#start} = 100, {@link QueryResponse#total} = 104 and
         * {@link QueryResponse#content} include records from 100th record to 104th record.
         */
        private Long start;

        /**
         * This is used to store the actual total number of available records for this query.
         * @see QueryResponse#start
         */
        private Long total;

        /**
         * The list of records of the query result.
         * @see QueryResponse#start
         */
        private List<C> content;

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

        public List<C> getContent() {
            return content;
        }

        public void setContent(List<C> content) {
            this.content = content;
        }


    }

}
