package com.nurseschedule.mvc.algorithm;

public class Pattern {

    private String pattern;

    private int hours;

    public Pattern(String pattern, int hours) {
        this.pattern = pattern;
        this.hours = hours;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

}
