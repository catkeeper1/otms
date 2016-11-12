package com.ckr.otms.secuirty.util;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;

import com.ckr.otms.secuirty.service.InternalUserDetailsService;

public class InternalControllerVoter implements AccessDecisionVoter<Object> {
	
	private static final Logger LOG = LoggerFactory.getLogger(InternalControllerVoter.class);
	
	@Override
	public boolean supports(ConfigAttribute attribute) {
		
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public int vote(Authentication authentication, Object object,
			Collection<ConfigAttribute> attributes) {
		
		for(ConfigAttribute attribute: attributes){
			
			LOG.debug("aaaaaaaaaaaaaaaa" + attribute.getAttribute());
			
		}
		
		return AccessDecisionVoter.ACCESS_DENIED;
		
		
	}
	
	

}
