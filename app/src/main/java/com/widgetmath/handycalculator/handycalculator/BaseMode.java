package com.widgetmath.handycalculator.handycalculator;


/**
 * Represents powers of 2 fractions from 2 to 64, and decimal (represented as 0)
 *
 * Created by ggraham412 on 10/30/2015.
 */
public enum BaseMode {

    DECIMAL("dec", 0),
    HALF("1/2", 2),
    FOURTH("1/4th", 4),
    EIGHTH("1/8th", 8),
    SIXTEENTH("1/16th", 16),
    THIRTYSECOND("1/32nd", 32),
    SIXTYFOURTH("1/64th", 64);

    private final String m_displayString;
    private final int m_base;

    private BaseMode(String disp, int base) {
        m_displayString = disp;
        m_base = base;
    }

    /**
     * Returns the base (denominator) of the fraction
     * @return
     */
    public int getBase() {
        return m_base;
    }

    /**
     * Converts a base into a BaseMode instance
     *
     * @param base : The base (2,4,8,16,32, or 64)
     * @return : The specified BaseMode instance, or decimal (base == 0)
     */
    public static BaseMode getFracMode(int base) {
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
