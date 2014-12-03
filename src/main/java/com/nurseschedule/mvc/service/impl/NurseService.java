package com.nurseschedule.mvc.service.impl;

import com.nurseschedule.mvc.dao.INurseDao;
import com.nurseschedule.mvc.dto.NurseDto;
import com.nurseschedule.mvc.service.INurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return nurseDao.find(id);
    }
}
