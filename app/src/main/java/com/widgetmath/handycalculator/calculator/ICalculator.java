package com.widgetmath.handycalculator.calculator;

import com.widgetmath.handycalculator.ButtonCode;
import com.widgetmath.handycalculator.DisplayMode;
import com.widgetmath.handycalculator.utils.INumberEntry;

/**
 * Created by ggraham412 on 11/1/2015.
 */
public interface ICalculator {

    public INumberEntry getAccumulator();
    public INumberEntry getEntry();
    public INumberEntry getMemory();
    public DisplayMode getDisplayMode();
    public ButtonCode getPendingOp();

    public boolean isNAN();
    public boolean isOE();
    public boolean isUE();

    public boolean isNumericError();

    public void HandleInput(ButtonCode code);

}
