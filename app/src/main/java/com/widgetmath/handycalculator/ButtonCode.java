package com.widgetmath.handycalculator;

public enum ButtonCode {

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

    // Operators
    ADD(0,"+"),
    SUBTRACT(0,"-"),
    MULTIPLY(0,"X"),
    DIVIDE(0,"/"),

    // Decimators
    DEC_DECIMAL(0),
    DEC_HALF(2),
    DEC_FOURTH(4),
    DEC_EIGHTH(8),
    DEC_SIXTEENTH(16),
    DEC_THIRTYSECOND(32),
    DEC_SIXTYFOURTH(64);

    public int getValue() {
        return m_value;
    }

    @Override
    public String toString() {
        return m_displayString;
    }

    private final String m_displayString;

    private final int m_value;

    private ButtonCode(int value) {
        this(value,"");
    }

    private ButtonCode(int value, String displayString) {
        m_value = value;
        m_displayString = displayString;
    }
}
