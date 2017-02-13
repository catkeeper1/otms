package com.ckr.otms.common.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * This is the parent class for all other DAO.
 */
@Repository
public class BaseDao {

    /**
     * The session factory for hibernate access.
     */
    @Resource(name = "sessionFactory")
    protected SessionFactory sessionFactory;
}
