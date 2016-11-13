package com.ckr.otms.common.web.controller;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.ckr.otms.common.web.constant.RequestPathConstant;


@Controller
public class InternalWebDataController {
	
	public static final String GEN_IWD = RequestPathConstant.INTERNAL_WEB_DATA + "/generateInternalWebData";
	
	public static final String VIEW = "internalWebDataView";
	
	public static final String RESPONSE_DATA = "responseWebData";
	
	private static final Logger LOG = LoggerFactory.getLogger(InternalWebDataController.class);
	
	@RequestMapping(value = GEN_IWD)
	public @ResponseBody Object generateInternalWebData(HttpServletRequest request){
		
		Object data = request.getAttribute(RESPONSE_DATA);
		
		if(data != null){
			LOG.debug("generate response data for {}", data.toString());
		}
		
		return data;
		
	}
	
	public static ModelAndView generateModelAndView(Object result){
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.addObject(InternalWebDataController.RESPONSE_DATA, result);
		modelAndView.setViewName(InternalWebDataController.VIEW);
		
		return modelAndView;
	}
	
	public static class InternalWebDataView implements View{

		@Override
		public String getContentType() {
			return "application/json";
		}

		@Override
		public void render(Map<String, ?> model, HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			
			LOG.debug("internal web data view.");
			
			if(model == null){
				return ;
			}
			
			if(model.isEmpty()){
				return ;
			}
			
			Object result = null;
			
			result = model.get(RESPONSE_DATA);
			
			if(result != null){
				LOG.debug("response data class is {}", result.getClass().getName());
			}
			
			
			request.setAttribute(RESPONSE_DATA, result);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher(GEN_IWD);
			
			dispatcher.forward(request, response);
			
		}
		
	}
}
