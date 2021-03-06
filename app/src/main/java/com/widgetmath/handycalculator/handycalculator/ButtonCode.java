package com.widgetmath.handycalculator.handycalculator;

/***
 * Enumeration of handy calculator operations
 *
 * @author ggraham412
 */
public enum ButtonCode {

    // Null operator
    NULL(0),

    // Administrative functions
    CLEAR(0),
    CLEAR_ENTRY(0),
    EQUALS(0),
    MEMORY_SAVE(0),
    MEMORY_RECALL(0),

    // Mode to change display decimator
    DISPLAY(0),

    // Numbers
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),

    // Transforms
    CHANGE_SIGN(-1),
    SQUARE(-1),
    SQRT(-1),

    // Operators
    ADD(0,"+"),
    SUBTRACT(0,"-"),
    MULTIPLY(0,"X"),
    DIVIDE(0,"/"),
    QUADR_ADD(0,"Q+"),
    QUADR_SUB(0,"Q-"),

    // Decimators
    DEC_DECIMAL(0),
    DEC_HALF(2),
    DEC_FOURTH(4),
    DEC_EIGHTH(8),
    DEC_SIXTEENTH(16),
    DEC_THIRTYSECOND(32),
    DEC_SIXTYFOURTH(64);

    // Enum data

    private final String m_displayString;  // simple string to displat with enum constant
    private final int m_value;             // int value to store with enum constant

    /**
     *
     * @return opaque value of the enum constant
     */
    public int getValue() {
        return m_value;
    }

    /**
     *
     * @return string to display for this enum constant
     */
    @Override
    public String toString() {
        return m_displayString;
    }


    private ButtonCode(int value) {
        this(value,"");
    }

    private ButtonCode(int value, String displayString) {
        m_value = value;
        m_displayString = displayString;
    }
}
