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

    /**
     * Returns true if it is Not a Number
     *
     * @return : True if it is Not a Number
     */
    boolean isNAN();
    void setNAN(boolean isNan);

    /**
     * Returns true if it is overflow
     *
     * @return : True if it is overflow
     */
    boolean isOE();

    /**
     * Returns true if it is underflow
     *
     * @return : True if it is underflow
     */
    boolean isUE();

    /**
     * Returns true if this is a valid number
     *
     * @return : False if any of isNAN, isOE, or isUE are True
     */
    boolean isValid();

}
