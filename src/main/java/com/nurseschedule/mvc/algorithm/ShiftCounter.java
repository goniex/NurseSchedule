package com.nurseschedule.mvc.algorithm;

public class ShiftCounter {

    String name;

    int value;

    int maxValue;

    public ShiftCounter(String name, int maxValue) {
        this.name = name;
        this.value = 0;
        this.maxValue = maxValue;
    }

    public boolean canIncrement() {
        return value < maxValue;
    }

    public void incrementCounter() {
        value++;
    }

    public void zeroCounter() {
        value = 0;
    }

    public int getCounter() {
        return value;
    }

    @Override
    public String toString() {
        return value + " ";
    }

}
