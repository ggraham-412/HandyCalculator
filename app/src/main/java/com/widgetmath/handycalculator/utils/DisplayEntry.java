package com.widgetmath.handycalculator.utils;

import java.math.BigDecimal;

/**
 * Display driver for INumberEntry objects.
 *
 * Gets string components for the integer part of a number, and the fractional part.
 * Supports two modes: accumulator for displaying results, and entry for displaying
 * partially entered numbers.
 *
 * Created by ggraham412 on 10/31/2015.
 */
public final class DisplayEntry {

    /**
     * Gets a string representing the remainder of this number.
     * If the INumberEntry has a decoimal base, this always returns the empty string.
     * If the base is not decimal, then it returns a deviation with respect of the
     * displayed number and its true value if it is "off tick"
     *
     * @param toDisplay : The number object to disply
     * @param mode : The display mode (accumulator or entry)
     * @param fracRemainder : If true, the remainder will be in terms of the same
     *                        fractional base as the number, otherwise absolute
     * @return : a String representing the remainder.
     */
    public static String getRemainderDisplay(INumberEntry toDisplay,
                                             DisplayMode mode,
                                             boolean fracRemainder) {
        BigDecimal remainder = toDisplay.getRemainder().setScale(6, BigDecimal.ROUND_HALF_EVEN);
        int base = toDisplay.getBase();
        if (remainder.compareTo(BigDecimal.ZERO) != 0) {
            if ( fracRemainder ) {
                if ( base != 0 ) {
                    remainder = remainder.multiply(new BigDecimal(base)).setScale(6, BigDecimal.ROUND_HALF_EVEN);
                }
            }
            StringBuilder remainderBuilder = new StringBuilder();
            if (remainder.compareTo(BigDecimal.ZERO) > 0) {
                remainderBuilder.append('+');
            }
            remainderBuilder.append(remainder.toPlainString());
            if ( fracRemainder ) {
                remainderBuilder.append(" / ").append(base);
            }
            return remainderBuilder.toString();
        } else {
            return "";
        }
    }

    /**
     * Gets a string representing the main value of this number.
     * If the INumberEntry has a decoimal base, this always returns the exact number.
     * If the base is not decimal, then it returns the floor of the number with respect to
     * the fractional base if it is "off tick"
     *
     * @param toDisplay : The number object to disply
     * @param mode : The display mode (accumulator or entry)
     * @return
     */
    public static String getMainDisplay(INumberEntry toDisplay, DisplayMode mode) {
        StringBuilder mainBuilder = new StringBuilder();
        if (toDisplay.isNegative()) {
            mainBuilder.append("-");
        }
        mainBuilder.append(toDisplay.getIntegerPart().toPlainString());
        if (toDisplay.isDotPushed()) {
            int base = toDisplay.getBase();
            if (base > 0 &&
                    (mode == DisplayMode.ENTRY || toDisplay.getFractionalPart().compareTo(BigDecimal.ZERO)>0) ) {
                mainBuilder.append(' ');
                mainBuilder.append(toDisplay.getFractionalPart().toPlainString());
                mainBuilder.append('/');
                mainBuilder.append(base);
            } else {
                mainBuilder.append('.');
                BigDecimal frac = toDisplay.getFractionalPart();
                String fracPart = "";
                if (frac.compareTo(BigDecimal.ZERO) > 0) {
                    fracPart = toDisplay.getFractionalPart().toPlainString();
                    int pads = toDisplay.getScale() - fracPart.length();
                    for (int i = 0; i < pads; i++) mainBuilder.append(('0'));
                    mainBuilder.append(fracPart);
                } else if (mode == DisplayMode.ENTRY) {
                    int pads = toDisplay.getScale();
                    for (int i = 0; i < pads; i++) mainBuilder.append(('0'));
                }
            }
        }
        return mainBuilder.toString();
    }


    private DisplayEntry()
    {
    }


}
