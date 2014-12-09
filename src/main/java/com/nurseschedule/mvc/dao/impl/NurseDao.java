package com.nurseschedule.mvc.dao.impl;

import com.nurseschedule.mvc.dao.INurseDao;
import com.nurseschedule.mvc.dto.NurseDto;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Tomasz Morek
 */
@Repository
@Transactional
public class NurseDao implements INurseDao {

    /**
     * Session factory
     */
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Get current session from sessionFactory
     * @return Session
     */
    private Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    @Override
    public NurseDto find(Integer id) {
        NurseDto nurse = (NurseDto) getSession().get(NurseDto.class, id);
        return nurse;
    }

    @Override
    public NurseDto findByEmail(String email) {
        String hqlQuery = "From NurseDto Where email = :email";
        Query query = getSession().createQuery(hqlQuery);
        query.setParameter("email", email);
        return (NurseDto) query.uniqueResult();
    }

    @Override
    public List<NurseDto> findAll() {
        return getSession().createCriteria(NurseDto.class).list();
    }

    @Override
    public Integer save(NurseDto nurse) {
        Integer id = (Integer) getSession().save(nurse);
        return id;
    }

    @Override
    public void update(NurseDto nurse) {
        getSession().update(nurse);
    }

    @Override
    public void delete(Integer id) {
        NurseDto nurse = new NurseDto(id);
        getSession().delete(nurse);
    }
}
