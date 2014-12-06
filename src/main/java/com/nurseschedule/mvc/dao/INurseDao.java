package com.nurseschedule.mvc.dao;

import com.nurseschedule.mvc.dto.NurseDto;

import java.util.List;

/**
 * @author Tomasz Morek
 */
public interface INurseDao {

    /**
     * Find nurse by ID
     * @param id
     * @return NurseDTO
     */
    NurseDto find(Integer id);

    /**
     * Find all nurses
     * @return List
     */
    List<NurseDto> findAll();

    /**
     * Save new or update exist nurse
     * @param nurse
     */
    Integer save(NurseDto nurse);

    /**
     * Update nurse
     * @param nurse
     */
    void update(NurseDto nurse);

    /**
     * Delete nurse
     * @param id
     */
    void delete(Integer id);
}
