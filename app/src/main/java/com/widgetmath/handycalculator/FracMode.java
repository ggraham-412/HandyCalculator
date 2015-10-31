package com.widgetmath.handycalculator;


/**
 * Created by ggraham412 on 10/30/2015.
 */
public enum FracMode {

    DECIMAL("dec", 0),
    HALF("1/2", 2),
    FOURTH("1/4th", 4),
    EIGHTH("1/8th", 8),
    SIXTEENTH("1/16th", 16),
    THIRTYSECOND("1/32nd", 32),
    SIXTYFOURTH("1/64th", 64);

    private final String m_displayString;
    private final int m_base;

    private FracMode(String disp, int base) {
        m_displayString = disp;
        m_base = base;
    }

    public int getBase() {
        return m_base;
    }

    public static FracMode getFracMode(int base) {
        switch (base) {
            case 2:
                return HALF;
            case 4:
                return FOURTH;
            case 8:
                return EIGHTH;
            case 16:
                return SIXTEENTH;
            case 32:
                return THIRTYSECOND;
            case 64:
                return SIXTYFOURTH;
            default:
                return DECIMAL;
        }
    }

    @Override
    public String toString() {
        return m_displayString;
    }
}
