package com.ckr.otms.secuirty.dao;

import com.ckr.otms.common.dao.BaseDao;
import com.ckr.otms.secuirty.valueobject.User;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao extends BaseDao {

    public List<User> getAllUsers() {

        Query<?> query = sessionFactory.getCurrentSession().createQuery("from User");

        @SuppressWarnings("unchecked")
        List<User> result = (List<User>) query.getResultList();

        return result;
    }

    public User getUser(String userName) {

        return (User) sessionFactory.getCurrentSession().get(User.class, userName);

    }

    public User saveOrUpdateUser(User user) {
        sessionFactory.getCurrentSession().saveOrUpdate(user);

        return user;
    }

    public User updateUser(User user) {
        sessionFactory.getCurrentSession().update(user);

        return user;
    }

    public void deleteUser(User user) {
        sessionFactory.getCurrentSession().delete(user);
    }

}
