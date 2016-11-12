package com.ckr.otms.secuirty.web.controller;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ckr.otms.common.web.constant.RequestPathConstant;
import com.ckr.otms.secuirty.constant.SecuriedAttribute;
import com.ckr.otms.secuirty.service.MenuService;
import com.ckr.otms.secuirty.valueobject.Menu;

@Controller
public class MenuController {
	
	@Autowired
	private MenuService menuService;
	
	@RequestMapping(value= RequestPathConstant.INTERNAL_WEB_DATA + "/getMenuForCurUser" )
	@Secured(SecuriedAttribute.ATT_AUTHENTICATED)
	public @ResponseBody Collection<Menu> getMenuForCurUser(HttpServletRequest request){
		
		/*if(true){
			ApplicationException ap = new ApplicationException("sys.error.need_authentication");
			ap.addMessage("sys.error.logon.badcredential", null);
			throw ap;
		}*/
		
		//String code = request.getParameter("code");
		
		//String parent = request.getParameter("parentCode");
		
		Collection<Menu> menuList = menuService.getMenuForUser(null);
		
		/*
		Collection<Menu> result = new ArrayList();
		
		if(code != null && code.length() > 0){
			for(Menu menu: menuList){
				if(menu.getCode().equals(code)){
					result.add( menu);
					
					return result;
				}
				
			}
		}
		
		if(parent != null && parent.length() > 0){
			for(Menu menu: menuList){
				if(parent.equals(menu.getParentCode()) ){
					result.add( menu);
					
					
				}
				
			}
			
			return result;
		}*/
		
		
		return menuList;
		
	}
}
