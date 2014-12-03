package com.nurseschedule.mvc.service;

import com.nurseschedule.mvc.dto.NurseDto;

/**
 * @author Tomasz Morek
 */
public interface INurseService {

    /**
     * Get nurse by ID
     * @param id
     * @return NurseDto
     */
    NurseDto findById(Integer id);
}
