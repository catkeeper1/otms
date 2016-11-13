package com.ckr.otms.secuirty.dao;

import java.util.List;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.ckr.otms.common.dao.BaseDAO;
import com.ckr.otms.secuirty.valueobject.Role;

@Repository
public class RoleDAO extends BaseDAO{

    public List<Role> getAllRoles(){
	    Query<?> query = sessionFactory.getCurrentSession().createQuery("from Role");

        @SuppressWarnings("unchecked")
        List<Role> result = (List<Role>) query.getResultList();

		return result;
	}

	public Role getRole(String roleCode){

		return sessionFactory.getCurrentSession().get(Role.class, roleCode);
	}

}
