package com.ckr.otms.secuirty.dao;

import java.util.List;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.ckr.otms.common.dao.BaseDAO;
import com.ckr.otms.secuirty.valueobject.Menu;

@Repository
public class MenuDAO extends BaseDAO{
	
	
	public List<Menu> getAllMenus(){
		
		Query<?> query = sessionFactory.getCurrentSession().createQuery("from Menu");
		
		@SuppressWarnings("unchecked")
		List<Menu> result = (List<Menu>) query.getResultList();
		
		return result;
	}
	
}
