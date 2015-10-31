package com.widgetmath.handycalculator.utils;

import java.math.BigDecimal;

/**
 * Builds a BigDecimal by adding digits to the end like a common calculator.
 *
 * (Wrapper Implementation)
 *
 * The dot push determines the implementation.
 *    dot arg == 0 : Decimal format
 *    dot arg != 0 : Fractional format with base == arg
 *
 */
public class NumberEntry_Wrapper implements INumberEntry {

    // The impl
    private INumberEntry m_impl;

    // Check and change sign
    public boolean isNegative() { return m_impl.isNegative(); }
    public void negate() { m_impl.negate(); };

    // Push a dot
    public boolean isDotPushed() { return m_impl.isDotPushed(); }
    public void pushDot(int arg) {
        // Don't allow double push
        if (m_impl.isDotPushed()) return;
        if ( arg == 0 ) {
            // Decimal
            m_impl.pushDot(arg);
        }
        else {
            // Change impl to fractional
            BigDecimal val = m_impl.getValue();
            m_impl = new NumberEntry_Fractional();
            m_impl.setValue(val, 0);
            m_impl.pushDot(arg);
        }
    }
    public int getBase() { return m_impl.getBase(); }
    public int getScale() { return m_impl.getScale(); }

    public void addDigit(int digit) { m_impl.addDigit(digit); }

    // Get values and components
    public BigDecimal getValue() { return m_impl.getValue(); }
    public void setValue(BigDecimal val, int arg) {
        if ( arg == 0 ) {
            m_impl = new NumberEntry_Decimal(val);
        }
        else {
            m_impl = new NumberEntry_Fractional(val, arg);
        }
    }
    public void setValue(INumberEntry other) {
        setValue(other.getValue(), other.getBase());
    }
    public BigDecimal getFractionalPart() { return m_impl.getFractionalPart(); }
    public BigDecimal getIntegerPart() { return m_impl.getIntegerPart(); }
    public BigDecimal getRemainder() { return m_impl.getRemainder(); }

    // Reset 
    public void clear() {
        m_impl = new NumberEntry_Decimal();
    }

    public NumberEntry_Wrapper() {
        clear();
    }

    public NumberEntry_Wrapper(BigDecimal val, int arg) {
        setValue(val, arg);
    }
}