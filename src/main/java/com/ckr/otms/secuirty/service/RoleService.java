package com.ckr.otms.secuirty.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ckr.otms.common.annotation.ReadOnlyTx;
import com.ckr.otms.secuirty.dao.RoleDAO;
import com.ckr.otms.secuirty.valueobject.Role;

@Service
public class RoleService {
	@Autowired
	private RoleDAO roleDAO;
	
	@ReadOnlyTx
	public Collection<Role> getAllRoles(){
		return roleDAO.getAllRoles();
	}
}
