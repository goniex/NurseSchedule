package com.nurseschedule.mvc.algorithm;

/**
 * @author Caro
 * @date 5 gru 2014
 */
public enum Shift {
    N(0), d(1), R(2);

    public final int id;

    private Shift(int id) {
        this.id = id;
    }

    public static Shift forId(int id) {
        for (Shift shift : values()) {
            if ( shift.id == id ) {
                return shift;
            }
        }
        throw new IllegalArgumentException("Invalid Shift id: " + id);
    }
}
