package com.widgetmath.handycalculator.utils;

/**
 * Indicates the DisplayMode of the calculator.  This is tied into the
 * calculator state.
 *   -ACCUMULATOR:  A result has recently been calculated, ready to display accumulator
 *   -ENTRY: An entry is being built by the user
 *
 * Created by ggraham412 on 10/30/2015.
 */
public enum DisplayMode {

    ACCUMULATOR("ACC"),
    ENTRY("ENT");

    private final String m_displayString;

    private DisplayMode(String disp) {
        m_displayString = disp;
    }

    @Override
    public String toString() {
        return m_displayString;
    }
}
