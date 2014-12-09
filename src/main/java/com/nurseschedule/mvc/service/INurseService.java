package com.nurseschedule.mvc.service;

import com.nurseschedule.mvc.dto.NurseDto;

import java.util.List;

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

    /**
     * Find nurse by email.
     * Method use to user authentication
     * @param email
     * @return
     */
    NurseDto findByEmail(String email);

    /**
     * Get all nurses
     * @return List
     */
    List<NurseDto> findAll();

    /**
     * Create new nurse
     * @param email
     * @param password
     * @param name
     * @param lastName
     * @param workTime
     * @param type
     */
    Integer create(String email, String password, String name,
                   String lastName, Integer workTime, String type);

    /**
     * Update existing nurse
     * @param nurse
     */
    void update(NurseDto nurse);

    /**
     * Delete nurse by ID
     * @param id
     */
    void delete(Integer id);
}
