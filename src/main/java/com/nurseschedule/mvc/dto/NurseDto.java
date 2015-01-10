package com.nurseschedule.mvc.dto;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Tomasz Morek
 */
@Entity
@Table(name = "nurse", schema = "", catalog = "tomek199_nurseschedule")
public class NurseDto {
    private Integer id;

    private String email;

    private String password;

    private String name;

    private String lastName;

    protected int workTime;

    private String type; // ?????

    private String fullPattern; // final nurse patter saving to database (move to dto class on the end)s

    /**
     * Default constructor
     */
    public NurseDto() {
    }

    /**
     * Constructor
     *
     * @param id
     */
    public NurseDto(Integer id) {
        this.id = id;
    }

    /**
     * Constructor
     */
    public NurseDto(String email, String password, String name, String lastName, Integer workTime, String type) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.workTime = workTime;
        this.type = type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "work_time")
    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    @Basic
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        NurseDto nurseDto = (NurseDto) o;

        if ( id != nurseDto.id ) {
            return false;
        }
        if ( type != nurseDto.type ) {
            return false;
        }
        if ( workTime != nurseDto.workTime ) {
            return false;
        }
        if ( email != null ? !email.equals(nurseDto.email) : nurseDto.email != null ) {
            return false;
        }
        if ( lastName != null ? !lastName.equals(nurseDto.lastName) : nurseDto.lastName != null ) {
            return false;
        }
        if ( name != null ? !name.equals(nurseDto.name) : nurseDto.name != null ) {
            return false;
        }
        if ( password != null ? !password.equals(nurseDto.password) : nurseDto.password != null ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + workTime;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
