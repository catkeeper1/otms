package com.ckr.otms.common.bo;

import com.ckr.otms.common.web.util.RestPaginationTemplate;
import com.ckr.otms.common.web.util.RestPaginationTemplate.QueryRequest;
import com.ckr.otms.common.web.util.RestPaginationTemplate.QueryResponse;

/**
 * This is a util class that should be used by service class that implement pagination
 * query.
 * <p>
 * In controller layer, {@link RestPaginationTemplate} is used to parse pagination request info
 * from HTTP request and generate proper response. Inside {@link RestPaginationTemplate#doQuery()}, developer
 * can call {@link RestPaginationTemplate#getQueryPageInfo()} to retrieve pagination request info from thread local.
 * This class is designed with GOF template method pattern and used to wrap up logic above.
 * Below is an example about how to use this class:</p>
 * <pre>
 * <code>
 *  return new BaseRestPaginationServiceTemplate() {
 *      &#064;Override
 *      protected QueryResponse&lt;Object&gt; doQuery(QueryResponse&lt;Object&gt; response, QueryRequest request) {
 *          // query data from somewhere
 *          ...
 *
 *          //set the query result into the response object.
 *          response.setContent(resultList);
 *          //set the total number of records of the query without pagination consideration
 *          response.setTotal(totalNoOfRecords);
 *
 *          return response;
 *      }
 *
 *  }.query(maxNoRecordsPerPage);
 * </code>
 * </pre>
 */
public abstract class BaseRestPaginationServiceTemplate {


    /**
     * Retrieve pagination request info(a {@link RestPaginationTemplate.QueryRequest} object)
     * from {@link RestPaginationTemplate#getQueryPageInfo()} and create
     * a {@link RestPaginationTemplate.QueryResponse} object. Use these 2 objects to call
     * {@link BaseRestPaginationServiceTemplate#doQuery(QueryResponse, QueryRequest)} so that developer can
     * just focus on how to implement the query logic inside a
     * {@link BaseRestPaginationServiceTemplate#doQuery(QueryResponse, QueryRequest)} implementation and no need to
     * think about how to retrieve or create those 2 objects.
     *
     * @param maxNoRecordsPerPage the max no of records will be returned by this query.
     * @return a {@link RestPaginationTemplate.QueryResponse} that will be used by
     * {@link RestPaginationTemplate} to generate HTTP response.
     * @see RestPaginationTemplate#query()
     * @see RestPaginationTemplate#getQueryPageInfo()
     */
    public QueryResponse<Object> query(Long maxNoRecordsPerPage) {

        QueryRequest request = RestPaginationTemplate.getQueryPageInfo();
        //request.getHeaders().get

        if (request != null) {
            request = adjustRange(request, maxNoRecordsPerPage);
            //request = adjustQueryParams(request);

        }


        QueryResponse<Object> contentRange = new QueryResponse<>();

        contentRange = doQuery(contentRange, request);

        if (request != null && request.getStart() != null) {
            contentRange.setStart(request.getStart());
        }

        return contentRange;
    }

    /**
     * When this class is used, this abstract method must be implemented to provide real query logic.
     *
     * @param response After the query is done, the result and total no of records should be saved in this object.
     *                 This object must be returned.
     * @param request  Developer can retrieve pagination request info from this parameter. Such as the no of page.
     * @return a {@link RestPaginationTemplate.QueryResponse} that will be used by
     * {@link RestPaginationTemplate} to generate HTTP response.
     */
    protected abstract QueryResponse<Object> doQuery(QueryResponse<Object> response, QueryRequest request);

    private QueryRequest adjustRange(QueryRequest request, Long maxNoRecordsPerPage) {

        if (request == null) {
            return null;
        }

        if (request.getStart() == null) {
            request.setStart((long) 0);
        }

        if (maxNoRecordsPerPage != null) {
            if (request.getEnd() == null || request.getEnd() - request.getStart() > maxNoRecordsPerPage - 1) {
                //make sure the total number records will not exceed the maxNoRecordPerPage
                request.setEnd(request.getStart() + maxNoRecordsPerPage - 1);
            }
        }

        return request;
    }


}
