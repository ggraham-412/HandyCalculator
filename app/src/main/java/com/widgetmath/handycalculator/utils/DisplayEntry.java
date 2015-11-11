package com.widgetmath.handycalculator.utils;

import com.widgetmath.handycalculator.calculator.DisplayMode;

import java.math.BigDecimal;

/**
 * Created by ggraham412 on 10/31/2015.
 */
public final class DisplayEntry {

    public static String getRemainderDisplay(INumberEntry toDisplay, DisplayMode mode) {
        BigDecimal remainder = toDisplay.getRemainder().setScale(6, BigDecimal.ROUND_HALF_EVEN);
        if (remainder.compareTo(BigDecimal.ZERO) != 0) {
            StringBuilder remainderBuilder = new StringBuilder();
            if (remainder.compareTo(BigDecimal.ZERO) > 0) {
                remainderBuilder.append('+');
            }
            remainderBuilder.append(remainder.toPlainString());
            return remainderBuilder.toString();
        } else {
            return "";
        }
    }

    public static String getMainDisplay(INumberEntry toDisplay, DisplayMode mode) {
        StringBuilder mainBuilder = new StringBuilder();
        if (toDisplay.isNegative()) {
            mainBuilder.append("-");
        }
        mainBuilder.append(toDisplay.getIntegerPart().toPlainString());
        if (toDisplay.isDotPushed()) {
            int base = toDisplay.getBase();
            if (base > 0) {
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
