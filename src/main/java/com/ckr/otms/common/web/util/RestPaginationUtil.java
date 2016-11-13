package com.ckr.otms.common.web.util;
/*
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.request.WebRequest;

import com.ckr.otms.common.bo.BaseRestPaginationServiceTemplate;
//import com.ckr.otms.common.bo.BaseRestPaginationService.QueryRequest;
import com.ckr.otms.common.bo.BaseRestPaginationServiceTemplate.QueryResponse;
//import com.ckr.otms.common.bo.BaseRestPaginationService.SortCriteria;
import com.ckr.otms.common.util.LongUtil;
/*
public class RestPaginationUtil {
	
	private final static Logger LOG = LoggerFactory.getLogger(RestPaginationUtil.class);
	
	
	private BaseRestPaginationService restPaginationService = null;
	
	
	public void setRestPaginationService(
			BaseRestPaginationService restPaginationService) {
		this.restPaginationService = restPaginationService;
	}



	public QueryResponse query(WebRequest webRequest, 
												String queryString, 
												Map<String, Object> params){
		return query(webRequest, queryString, params, (long) 1000);
	}
			
	
	
	public QueryResponse query(WebRequest webRequest, 
			String queryString, 
			Map<String, Object> params,
			Long maxNoRecordsPerPage) {

		//request.getHeaders().get
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setStart((long)0);
		queryRequest.setQueryString(queryString);
		queryRequest.setQueryParams(params);

		queryRequest = adjustQueryParams(queryRequest);
		queryRequest = adjustQueryString(queryRequest);
		queryRequest = parsePageRange(queryRequest, webRequest);
		queryRequest = parseSortBy(queryRequest, webRequest);



		return restPaginationService.query(queryRequest, maxNoRecordsPerPage);
		
	}
	
	
	
	public static HttpEntity<Collection<Object>> generateResponse(QueryResponse contentRange){
		
		
		
		LinkedMultiValueMap<String, String> headerMap = new LinkedMultiValueMap<String, String>();
		
		headerMap.add("Content-Range", "items " + 
		              contentRange.getStart()+"-" + 
		              (contentRange.getStart() + contentRange.getContent().size() - 1) +"/" +
		              contentRange.getTotal());
		
		return new HttpEntity<Collection<Object>>(contentRange.getContent(), headerMap);
		
	}
	
	protected QueryRequest adjustQueryString(QueryRequest request){
		
		StringBuffer queryStr = new StringBuffer(request.getQueryString());
		
		LOG.debug("before adjustment, the query string is {}", queryStr);
		
		
		
		for(int startInd = queryStr.indexOf("/*"); startInd >=0; startInd = queryStr.indexOf("/*")){
			
			int endInd = queryStr.indexOf("*", startInd);
			
			if(endInd < 0){
				break;
			}
			
			String criteriaStr = queryStr.substring(startInd + 2, endInd );
			
			LOG.debug("criteria string: {}", criteriaStr);
			
			StringTokenizer tokenizer = new StringTokenizer(criteriaStr,"|");
			if(tokenizer.countTokens() < 2){
				LOG.error("invalid criteria string: {}", criteriaStr);
			}
			
			String criteriaName = tokenizer.nextToken().trim();
			
			if(request.getQueryParams().keySet().contains(criteriaName)){
				
				String criteriaContent = tokenizer.nextToken();
				
				queryStr.replace(startInd, endInd + 2, criteriaContent);
				
			} else {
			
				queryStr.replace(startInd, endInd + 2 , "");
			}
			
		}
		
		LOG.debug("after adjustment, the query string is {}", queryStr);
		
		request.setQueryString(queryStr.toString());
		
		return request;
		
	}
	
	protected QueryRequest parseSortBy(QueryRequest request, WebRequest webRequest){
		
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
	
	protected QueryRequest adjustQueryParams(QueryRequest request){
		
		Map<String, Object> params = request.getQueryParams();
		
		if(params == null){
			request.setQueryParams(new HashMap<String, Object>());
			return request;
		}
		
		Set<String> removedKeySet = new HashSet<String>();
		
		for(String key: params.keySet()){
			if(params.get(key) == null){
				removedKeySet.add(key);				
			}
		}
		
		for(String key: removedKeySet){
			params.remove(key);
		}
		
		request.setQueryParams(params);
		
		return request;
	}
	
	protected QueryRequest parsePageRange(QueryRequest range, WebRequest webRequest){
		
		
		String[] rangeValues = webRequest.getHeaderValues("Range");
		
		if(rangeValues == null || rangeValues.length == 0){
			return range;
		}
		
		for(String rangeStr: rangeValues){
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
	
	
		
		
		
		
		
	
	
}*/
