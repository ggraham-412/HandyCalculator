package com.widgetmath.handycalculator.utils;

import java.math.BigDecimal;

/**
 * Builds a BigDecimal by adding digits to the end like a common calculator.
 */
public interface INumberEntry {

    // Check and Change sign
    boolean isNegative();
    void negate();

    // Add digits, check and push the dot 
    void addDigit(int digit);
    boolean isDotPushed();
    void pushDot(int arg);   // Dot arg allows us to extend to non-decimal cases
    int getBase();
    int getScale();

    // Get/set values and components
    BigDecimal getValue();
    void setValue(BigDecimal val, int arg);
    void setValue(INumberEntry other);
    BigDecimal getFractionalPart();  // Part to the right of the decimal
    BigDecimal getIntegerPart();     // Part to the left of the decimal
    BigDecimal getRemainder();       // Allows us to extend to non-decimal cases

    int getImproperMax();
    void setImproperMax(int n);
    boolean isImproper();
    
    // Reset 
    void clear();
}
