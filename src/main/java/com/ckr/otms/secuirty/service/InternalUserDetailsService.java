package com.ckr.otms.secuirty.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ckr.otms.secuirty.valueobject.InternalUserDetails;

@Service
public class InternalUserDetailsService implements UserDetailsService {
	
	private static final Logger LOG = LoggerFactory.getLogger(InternalUserDetailsService.class);
	
	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		
		LOG.info("Retrieve user detail for {}." , userName);
		
		InternalUserDetails result = new InternalUserDetails();
		
		result.setPassword("abc");
		result.setUsername(userName);
		
		return result;
	}

}
