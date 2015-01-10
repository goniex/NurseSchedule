package com.nurseschedule.mvc.utils;

import org.springframework.validation.ObjectError;

import java.io.Serializable;
import java.util.List;

/**
 * Class to return http response object
 * @author Tomasz Morek
 */
public class ResponseUtil implements Serializable {

    /**
     * Response status SUCCESS
     */
    private static final String STATUS_SUCCESS = "SUCCESS";

    /**
     * Response status ERROR
     */
    private static final String STATUS_ERROR = "ERROR";

    /**
     * Response status
     */
    private String status;

    /**
     * Response message
     */
    private String message;

    /**
     * Result list
     */
    private List<Object> list;

    /**
     * Response object
     */
    private Object object;

    /**
     * List of error fields
     */
    private List<ObjectError> errors;

    /**
     * Constructor
     * Generate response with status SUCCESS
     */
    public ResponseUtil() { }

    /**
     * Constructor
     * Generate success or error message
     * @param message
     * @param isSuccess
     */
    public ResponseUtil(String message, boolean isSuccess) {
        this.message = message;
        if (isSuccess) {
            this.status = STATUS_SUCCESS;
        } else {
            this.status = STATUS_ERROR;
        }
    }

    /**
     * Constructor
     * Generate error message
     * @param list
     * @param message
     */
    public ResponseUtil(String message, List<ObjectError> list) {
        this.errors = errors;
        this.message = message;
        this.status = STATUS_ERROR;
    }

    /**
     * Constructor
     * Generate success message
     * @param object
     */
    public ResponseUtil(Object object) {
        this.object = object;
        this.status = STATUS_SUCCESS;
    }

    /**
     * Constructor
     * Generate success message
     * @param object
     * @param message
     */
    public ResponseUtil(Object object, String message) {
        this.object = object;
        this.message = message;
        this.status = STATUS_SUCCESS;
    }

    /**
     * Constructor
     * Generate success message
     * @param list
     */
    public ResponseUtil(List<Object> list) {
        this.list = list;
        this.status = STATUS_SUCCESS;
    }

    /**
     * Constructor
     * Generate success message
     * @param list
     * @param message
     */
    public ResponseUtil(List<Object> list, String message) {
        this.list = list;
        this.message = message;
        this.status = STATUS_SUCCESS;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public List<ObjectError> getErrors() {
        return errors;
    }

    public void setErrors(List<ObjectError> errors) {
        this.errors = errors;
    }
}
