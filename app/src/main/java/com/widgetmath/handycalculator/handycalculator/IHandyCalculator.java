package com.widgetmath.handycalculator.handycalculator;

import com.widgetmath.handycalculator.calculator.ICalculator;

/**
 * Created by ggraham412 on 11/1/2015.
 */
public interface IHandyCalculator extends ICalculator {

    public FracMode getDisplayBase();
    public boolean isDisplayPending();

}
