package com.ckr.otms.secuirty.util;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;

import com.ckr.otms.secuirty.constant.SecuriedAttribute;
import com.ckr.otms.secuirty.service.InternalUserDetailsService;
import com.ckr.otms.secuirty.valueobject.InternalUserDetails;

public class AuthenticatedVoter implements AccessDecisionVoter<Object> {

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
		
		//System.err.println("aaaaaaaaaaaaaaaaaaaaaaaa" + authentication.getPrincipal().getClass().getName());
		
		if(! (authentication.getPrincipal() instanceof InternalUserDetails)){
			
			throw new InsufficientAuthenticationException("insufficient authentication");
			
		}
		
		for(ConfigAttribute att: attributes){
			
			if(SecuriedAttribute.ATT_AUTHENTICATED.equals(att.getAttribute())){
				return AccessDecisionVoter.ACCESS_GRANTED;
			}
			
		}
		
		return AccessDecisionVoter.ACCESS_ABSTAIN;
	}

}
