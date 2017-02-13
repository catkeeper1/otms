package com.ckr.otms.secuirty.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ckr.otms.common.annotation.ReadOnlyTx;
import com.ckr.otms.secuirty.dao.MenuDao;
import com.ckr.otms.secuirty.valueobject.Menu;

@Service
public class MenuService {
	
	@Autowired
	private MenuDao menuDAO;
	
	@ReadOnlyTx
	public Collection<Menu> getMenuForUser(String userId){
		return menuDAO.getAllMenus();
	}
}
