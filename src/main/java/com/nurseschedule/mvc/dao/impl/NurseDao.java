package com.nurseschedule.mvc.dao.impl;

import com.nurseschedule.mvc.dao.INurseDao;
import com.nurseschedule.mvc.dto.HibernateUtil;
import com.nurseschedule.mvc.dto.NurseDto;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Tomasz Morek
 */
@Repository
public class NurseDao implements INurseDao {

    @Override
    public NurseDto find(Integer id) {
        NurseDto nurse = (NurseDto) HibernateUtil.getSession().get(NurseDto.class, 1);
        return nurse;
    }

    @Override
    public List<NurseDto> findAll() {
        // todo
        return null;
    }

    @Override
    public void saveOrUpdate(NurseDto nurseDto) {
        // todo
    }

    @Override
    public void delete(NurseDto nurseDto) {
        // todo
    }
}
