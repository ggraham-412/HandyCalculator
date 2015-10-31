package com.widgetmath.handycalculator.utils;

import java.math.BigDecimal;

/**
 * Builds a BigDecimal by adding digits to the end like a common calculator.
 *
 * (Decimal Implementation)
 *
 */
public class NumberEntry_Decimal implements INumberEntry {

    // State
    private BigDecimal m_value;
    private boolean m_dotPushed;
    private boolean m_negative;

    // Check and change sign
    public boolean isNegative() { return m_negative; }
    public void negate() { m_negative = (!m_negative) &&
            (m_value.compareTo(BigDecimal.ZERO) != 0); }  // Don't consider 0 "negative"


    // Add digits                     
    public void addDigit(int digit) {
        if ( isDotPushed() ) {
            // Add to the right of the decimal
            int scale = m_value.scale() + 1;
            BigDecimal ftmp = new BigDecimal(digit);
            ftmp = ftmp.divide(BigDecimal.TEN.pow(scale), scale, BigDecimal.ROUND_HALF_EVEN);
            m_value = m_value.setScale(scale).add(ftmp);
        } else {
            // Add to the left of the decimal
            m_value = m_value.multiply(BigDecimal.TEN).add(new BigDecimal(digit)).stripTrailingZeros();
        }
    }

    // Check and push dot - the pushDot argument is meaningless here
    public boolean isDotPushed() { return m_dotPushed; }
    public void pushDot(int ignore) { m_dotPushed = true; }

    public int getBase() {
        return 0;
    }
    public int getScale() { return m_value.scale(); }

    // Get/set value and components    
    public BigDecimal getValue() {
        BigDecimal retval = m_value.stripTrailingZeros();
        if ( isNegative() ) retval = retval.negate();
        return retval;
    }
    // - the pushDot argument is meaningless here
    public void setValue(BigDecimal val, int ignore) {
        m_negative = ( val.compareTo(BigDecimal.ZERO) < 0 );
        m_value = val.abs().stripTrailingZeros();
        m_dotPushed = (m_value.scale()>0);
    }
    public void setValue(INumberEntry other) {
        setValue(other.getValue(), other.getBase());
    }

    public BigDecimal getFractionalPart() {
        BigDecimal tmp = m_value.abs().subtract(getIntegerPart()).stripTrailingZeros();
        return tmp.multiply(BigDecimal.TEN.pow(tmp.scale())).stripTrailingZeros();
    }

    public BigDecimal getIntegerPart() {
        return m_value.abs().setScale(0, BigDecimal.ROUND_DOWN);
    }

    // The remainder is always zero 
    public BigDecimal getRemainder() {
        return BigDecimal.ZERO;
    }

    public void clear() {
        m_value = BigDecimal.ZERO;
        m_dotPushed = false;
        m_negative = false;
    }

    // Constructors
    public NumberEntry_Decimal() {
        clear();
    }

    public NumberEntry_Decimal(BigDecimal dec) {
        setValue(dec,0);
    }

}
