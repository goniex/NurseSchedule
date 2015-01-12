package com.nurseschedule.mvc.object;

import com.nurseschedule.mvc.dto.NurseDto;

import java.io.Serializable;

/**
 * @author Tomasz Morek
 */
public class Nurse implements Serializable {

    /**
     * ID
     */
    private Integer id;

    /**
     * Name
     */
    private String name;

    /**
     * Last name
     */
    private String lastName;

    /**
     * email
     */
    private String email;

    /**
     * Work time
     */
    private int workTime;

    /**
     * Default constructor
     */
    public Nurse() { }

    /**
     * Constructor
     * @param nurseDto
     */
    public Nurse(NurseDto nurseDto) {
        this.id = nurseDto.getId();
        this.name = nurseDto.getName();
        this.lastName = nurseDto.getLastName();
        this.email = nurseDto.getEmail();
        this.workTime = nurseDto.getWorkTime();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }
}
