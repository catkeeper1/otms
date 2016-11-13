package com.ckr.otms.common.bo;

import com.ckr.otms.common.web.util.RestPaginationTemplate;
import com.ckr.otms.common.web.util.RestPaginationTemplate.QueryRequest;
import com.ckr.otms.common.web.util.RestPaginationTemplate.QueryResponse;

public abstract class BaseRestPaginationServiceTemplate {
	
	//private Long maxNoRecordsPerPage = (long) 1000;
	
	public QueryResponse<Object> query(Long maxNoRecordsPerPage) {
		
		QueryRequest request = RestPaginationTemplate.getQueryPageInfo();
		//request.getHeaders().get
		
		if(request != null){
			request = adjustRange(request, maxNoRecordsPerPage);
			//request = adjustQueryParams(request);
		
		}
		
		
		QueryResponse<Object> contentRange = new QueryResponse<Object>();
				
		contentRange = doQuery(contentRange, request);
		
		if(request != null && request.getStart() != null){
			contentRange.setStart(request.getStart());
		}
		
		return contentRange;
	}
	
	protected abstract QueryResponse<Object> doQuery(QueryResponse<Object> responseRange, QueryRequest range);
	
	private QueryRequest adjustRange(QueryRequest request, Long maxNoRecordsPerPage){
		
		if(request == null){
			return request;
		}
		
		if(request.getStart() == null){
			request.setStart((long)0);
		}
		
		if(maxNoRecordsPerPage != null){
			if(request.getEnd() == null || request.getEnd() - request.getStart() > maxNoRecordsPerPage -1){
				//make sure the total number records will not exceed the maxNoRecordPerPage
				request.setEnd(request.getStart() + maxNoRecordsPerPage -1);
			}
		}
		
		return request;
	}
	
	
}
