package com.widgetmath.handycalculator.utils;

import java.math.BigDecimal;

/**
 * Builds a BigDecimal by adding digits to the end like a common calculator.
 */
public interface INumberEntry {

    // Check and Change sign
    public boolean isNegative();
    public void negate();

    // Add digits, check and push the dot 
    public void addDigit(int digit);
    public boolean isDotPushed();
    public void pushDot(int arg);   // Dot arg allows us to extend to non-decimal cases
    public int getBase();
    public int getScale();

    // Get/set values and components
    public BigDecimal getValue();
    public void setValue(BigDecimal val, int arg);
    public void setValue(INumberEntry other);
    public BigDecimal getFractionalPart();  // Part to the right of the decimal
    public BigDecimal getIntegerPart();     // Part to the left of the decimal
    public BigDecimal getRemainder();       // Allows us to extend to non-decimal cases

    // Reset 
    public void clear();
}
