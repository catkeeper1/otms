package com.ckr.otms.at.common.bo;

import java.util.HashMap;
import java.util.Map;


import org.junit.Test;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.ckr.otms.common.annotation.SpringTestCaseConfig;

import com.ckr.otms.common.bo.HibernateRestPaginationService;
import com.ckr.otms.common.web.util.RestPaginationTemplate.QueryRequest;

@SpringTestCaseConfig
public class HibernateRestPaginationServiceTest extends AbstractJUnit4SpringContextTests{
	
	@Test
	public void testQuery()throws Exception{
		HibernateRestPaginationService service = 
				(HibernateRestPaginationService) this.applicationContext.getBean("restPaginationService");
		
		QueryRequest request = new QueryRequest();
		/*request.setStart((long) 0);
		request.setQueryString("from User ");
		Map<String, Object> params = new HashMap();
		//params.put("desc", "ddd");
		request.setQueryParams(params);
		request.addSortCriteria(true, "password");
		request.addSortCriteria(false, "userName");
		/*
		QueryResponse response = service.query(request , (long)1000);
		
		for(Object obj: response.getContent()){
			User user = (User) obj;
			
			System.out.println(user.getUserName() +" "+ user.getUserDescription());
		}
		
		System.out.println(response.getTotal());
		*/
	}
}
