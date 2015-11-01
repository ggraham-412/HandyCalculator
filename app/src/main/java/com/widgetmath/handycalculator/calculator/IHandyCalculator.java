package com.widgetmath.handycalculator.calculator;

import com.widgetmath.handycalculator.FracMode;

/**
 * Created by ggraham412 on 11/1/2015.
 */
public interface IHandyCalculator extends ICalculator {

    public FracMode getDisplayBase();
    public boolean isDisplayPending();

}
