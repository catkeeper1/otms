package com.ckr.otms.common.dao;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class BaseDAO {
	@Resource(name="sessionFactory")
	protected SessionFactory sessionFactory;
}
