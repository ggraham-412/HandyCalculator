package com.widgetmath.handycalculator.utils;

import java.math.BigDecimal;

/**
 * Builds a BigDecimal by adding digits to the end like a common calculator.
 *
 * (Fractional Implementation)
 *
 * The dot arg is the fractional base (denominator)
 *
 * Keeps the number represented as an integer part i, fractional part f, base b, and 
 * remainder r (to represent "off tick" number with no loss of precision).  
 *
 * The equivalent value is i + f/b + r.
 *
 */
public class NumberEntry_Fractional implements INumberEntry {

    public static final int MAX_SCALE = 16;
    public static final BigDecimal HALF = new BigDecimal("0.5");

    // State
    private BigDecimal m_ivalue;    // Integer part
    private BigDecimal m_fvalue;    // Fractional part
    private BigDecimal m_base;      // Fractional base
    private BigDecimal m_quantum;   // 1 / m_base
    private BigDecimal m_remainder; // Remainder   
    private boolean m_dotPushed;
    private boolean m_negative;

    // Option to allow improper fractions
    private boolean m_improperMode = false;
    public boolean getImproperMode() { return m_improperMode; }
    public void setImproperMode(boolean mode) { m_improperMode = true; }
    public boolean isImproper() {
        return (m_base.compareTo(BigDecimal.ZERO) != 0) &&
                (m_fvalue.compareTo(m_base)) > 0;
    }

    // Check and change sign
    public boolean isNegative() { return m_negative; }
    public void negate() { m_negative = (!m_negative) &&
            (m_ivalue.compareTo(BigDecimal.ZERO) != 0 ||
                    m_fvalue.compareTo(BigDecimal.ZERO) != 0 ||
                    m_remainder.compareTo(BigDecimal.ZERO) != 0); }  // Don't consider 0 "negative"

    // Add digits
    public void addDigit(int digit) {
        if ( isDotPushed() ) {
            // Add to fractional part
            if ( m_fvalue.compareTo(BigDecimal.ZERO) == 0 && digit == 0 ) return;
            BigDecimal tmp = m_fvalue.multiply(BigDecimal.TEN).add(new BigDecimal(digit));
            if ( !m_improperMode && tmp.compareTo(m_base) >= 0 ) return;
            m_fvalue = tmp;
        } else {
            // Add to integer part
            if ( m_ivalue.compareTo(BigDecimal.ZERO) == 0 && digit == 0 ) return;
            m_ivalue = m_ivalue.multiply(BigDecimal.TEN).add(new BigDecimal(digit));
        }
    }

    // Check and push a dot
    private void setBaseAndQuantum(int base) {
        if ( base <= 0 ) {
            m_base = BigDecimal.ONE;
        }
        else {
            m_base = new BigDecimal(base);
        }
        m_quantum = BigDecimal.ONE.divide(m_base, MAX_SCALE, BigDecimal.ROUND_HALF_EVEN);
    }
    public boolean isDotPushed() { return m_dotPushed; }
    // base should be > 0 
    public void pushDot(int base) {
        if ( m_dotPushed ) return;  // Don't allow push dot twice
        m_dotPushed = true;
        setBaseAndQuantum(base);
    }
    public int getBase() { return m_base.intValue(); }
    public int getScale() { return m_fvalue.toPlainString().length(); }

    // Get/set values and components
    public BigDecimal getValue() {
        BigDecimal retval = m_ivalue
                .add(m_quantum.multiply(m_fvalue))
                .add(m_remainder).stripTrailingZeros();
        if ( isNegative() ) retval = retval.negate();
        return retval;
    }

    public void setValue(BigDecimal val, int base) {
        m_negative = ( val.compareTo(BigDecimal.ZERO) < 0 );
        val = val.stripTrailingZeros().abs();
        if ( val.scale() > 0 ) {
            m_dotPushed = true;
            setBaseAndQuantum(base);
            m_ivalue = val.setScale(0, BigDecimal.ROUND_DOWN);
            BigDecimal ftmp = val.subtract(m_ivalue);
            ftmp = ftmp.divide(m_quantum, MAX_SCALE, BigDecimal.ROUND_HALF_EVEN);
            m_fvalue = ftmp.setScale(0, BigDecimal.ROUND_DOWN);
            BigDecimal fracfrac = ftmp.subtract(m_fvalue);
            m_remainder = m_quantum.multiply(fracfrac);
            if ( fracfrac.compareTo( HALF ) > 0 ) {
                m_fvalue = m_fvalue.add(BigDecimal.ONE);
                m_remainder = m_quantum.multiply(BigDecimal.ONE.subtract(fracfrac)).negate();
            }
        }
        else {
            m_dotPushed = false;
            setBaseAndQuantum(0);
            m_remainder = BigDecimal.ZERO;
            m_ivalue = val;
            m_fvalue = BigDecimal.ZERO;
        }
    }
    public void setValue(INumberEntry other) {
        setValue(other.getValue(), other.getBase());
    }

    public BigDecimal getFractionalPart() {
        return m_fvalue;
    }

    public BigDecimal getIntegerPart() {
        return m_ivalue;
    }

    public BigDecimal getRemainder() {
        return m_remainder.stripTrailingZeros();
    }

    public void clear() {
        m_remainder = BigDecimal.ZERO;
        m_ivalue = BigDecimal.ZERO;
        m_fvalue = BigDecimal.ZERO;
        m_dotPushed = false;
        m_negative = false;
        m_quantum = BigDecimal.ZERO;
    }


    public NumberEntry_Fractional() {
        clear();
    }

    public NumberEntry_Fractional(BigDecimal dec, int base) {
        setValue(dec, base);
    }

}
