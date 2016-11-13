package com.ckr.otms.common.web.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ckr.otms.common.util.LongUtil;

public abstract class RestPaginationTemplate<R> {
	
	private final static Logger LOG = LoggerFactory.getLogger(RestPaginationTemplate.class);
	
	private static final ThreadLocal<QueryRequest> queryPagingInfo =
			new ThreadLocal<QueryRequest>();
	
	public ResponseEntity<Collection<R>> query() {
		
		HttpServletRequest webRequest = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  

		
		//request.getHeaders().get
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setStart((long)0);
		//queryRequest.setQueryString(queryString);
		//queryRequest.setQueryParams(params);
		
		if(webRequest != null){
		
			queryRequest = parsePageRange(queryRequest, webRequest);
			queryRequest = parseSortBy(queryRequest, webRequest);
		}
		
		
		ResponseEntity<Collection<R>> result = null;
		
		try {
			queryPagingInfo.set(queryRequest);
			
			result = generateResponse(doQuery());
		
		} finally{
			queryPagingInfo.remove();
		}
		
		return result;
		
	}

	public static QueryRequest getQueryPageInfo(){
		return queryPagingInfo.get();
	}
	
	abstract protected QueryResponse<R> doQuery();
	
	
	protected QueryRequest parsePageRange(QueryRequest range, HttpServletRequest webRequest){
		
		
		Enumeration<String> rangeValues = webRequest.getHeaders("Range");
		
		if(rangeValues == null ){
			return range;
		}
		
		while(rangeValues.hasMoreElements()){
			
			String rangeStr = rangeValues.nextElement();
			
			if(!rangeStr.startsWith("items=")){
				continue;
			}
			
			StringTokenizer tokenizer = new StringTokenizer(rangeStr.substring("items=".length() ), "-");
			
			if(tokenizer.hasMoreTokens()){
				String startStr = tokenizer.nextToken();
				
				Long start = LongUtil.parse(startStr);
				
				if(start == null){
					return null;
				}
				
				range.setStart(start);
			}
			
			if(tokenizer.hasMoreTokens()){
				String startStr = tokenizer.nextToken();
				
				Long end = LongUtil.parse(startStr);
				
				if(end != null){
					range.setEnd(end);
				}
				
			}
			
			
			break;
			
		}
		
		return range;
	}
	
	
	protected QueryRequest parseSortBy(QueryRequest request, HttpServletRequest webRequest){
		
		String sortByStr = webRequest.getParameter("restSortBy");
		
		if(sortByStr == null){
			request.setSortCriteriaList(new ArrayList<SortCriteria>());
			return request;
		}
		
		LOG.debug("parseSortBy(). sortByStr = {}", sortByStr );
		
		
		StringTokenizer tokenizer =new StringTokenizer(sortByStr, ",");
		
		List<SortCriteria> sortCriteriaList = new ArrayList<SortCriteria>();
		
		while(tokenizer.hasMoreTokens()){
			String criteriaStr = tokenizer.nextToken();
			
			if(criteriaStr.length() <=1){
				LOG.error("invlaid sort critiera:{}", criteriaStr );
			}
			
			SortCriteria sortCriteria = new SortCriteria();
			
			if(criteriaStr.startsWith(" ") || criteriaStr.startsWith("+")){
				sortCriteria.setAsc(true);
				
			} else if(criteriaStr.startsWith("-")){
				sortCriteria.setAsc(false);
			} else {
				LOG.error("invlaid sort critiera:{}", criteriaStr );
				continue;
			}
			
			sortCriteria.setFieldName(criteriaStr.substring(1));
			
			sortCriteriaList.add(sortCriteria);
		}
		
		request.setSortCriteriaList(sortCriteriaList);
		
		return request;
	}
	
	
	public ResponseEntity<Collection<R>> generateResponse(QueryResponse<R> contentRange){
		
		
		
		HttpHeaders headerMap = new HttpHeaders();
		
		headerMap.add("Content-Range", "items " + 
		              contentRange.getStart()+"-" + 
		              (contentRange.getStart() + contentRange.getContent().size() - 1) +"/" +
		              contentRange.getTotal());
		
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
		
		public void addSortCriteria(boolean isAsc, String fieldName){
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
	
	public static class SortCriteria{
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
