package com.widgetmath.handycalculator;

/**
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
