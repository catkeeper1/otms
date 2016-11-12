package com.ckr.otms.common.web.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet Filter implementation class UrlRecordingFilter
 */
public class UrlRecordingFilter implements Filter {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(UrlRecordingFilter.class);
	
	
	

    /**
     * Default constructor. 
     */
    public UrlRecordingFilter() {
        super();
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {

	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		long time = System.currentTimeMillis();
		
		HttpServletRequest httpReq = (HttpServletRequest) request;
		
		if(LOG.isDebugEnabled()){
			LOG.debug("Request come from {}. Method is {}. Uri is {}.",
					new Object[]{httpReq.getRemoteHost(), httpReq.getMethod(), httpReq.getRequestURI()});
		}
		
		
		
		chain.doFilter(request, response);
		
		if(LOG.isDebugEnabled()){
			LOG.debug("Uri = {}. Process duration = {} ms",httpReq.getRequestURI(), (System.currentTimeMillis() - time));
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		
	}
	
	
}
