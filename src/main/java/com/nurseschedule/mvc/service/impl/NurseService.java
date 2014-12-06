package com.nurseschedule.mvc.service.impl;

import com.nurseschedule.mvc.dao.INurseDao;
import com.nurseschedule.mvc.dto.NurseDto;
import com.nurseschedule.mvc.service.INurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Tomasz Morek
 */
@Service
public class NurseService implements INurseService {

    /**
     * Nurse DAO
     */
    @Autowired
    INurseDao nurseDao;

    @Override
    public NurseDto findById(Integer id) {
        return this.nurseDao.find(id);
    }

    @Override
    public List<NurseDto> findAll() {
        return this.nurseDao.findAll();
    }

    @Override
    public Integer create(String email, String password, String name, String lastName, Integer workTime, String type) {
        NurseDto nurse = new NurseDto(email, password, name, lastName, workTime, type);
        return this.nurseDao.save(nurse);
    }

    @Override
    public void update(NurseDto nurse) {
        this.nurseDao.update(nurse);
    }

    @Override
    public void delete(Integer id) {
        this.nurseDao.delete(id);
    }
}
