package com.nurseschedule.mvc.object;

import java.io.Serializable;
import java.util.Date;

/**
 * Calendar event
 * @author Tomasz Morek
 */
public class Event implements Serializable {

    /**
     * Title
     */
    private String title;

    /**
     * Type
     */
    private String type;

    /**
     * Start date
     */
    private Date starts_at;

    /**
     * End date
     */
    private Date ends_at;

    /**
     * Is editable
     */
    private boolean editable = false;

    /**
     * Is deletable
     */
    private boolean deletable = false;

    /**
     * Default constructor
     */
    public Event() {}

    public Event(String fullName, String type, Date starts_at, Date ends_at) {
        this.title = fullName;
        this.type = type;
        this.starts_at = starts_at;
        this.ends_at = ends_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStarts_at() {
        return starts_at;
    }

    public void setStarts_at(Date starts_at) {
        this.starts_at = starts_at;
    }

    public Date getEnds_at() {
        return ends_at;
    }

    public void setEnds_at(Date ends_at) {
        this.ends_at = ends_at;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }
}
