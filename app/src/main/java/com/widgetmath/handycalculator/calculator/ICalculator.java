package com.widgetmath.handycalculator.calculator;

import com.widgetmath.handycalculator.handycalculator.DisplayMode;
import com.widgetmath.handycalculator.utils.INumberEntry;

/**
 * Interface for a simple calculator with the following features:
 *   - An accumulator to hold results
 *   - A single numeric entry slot
 *   - A single memory slot
 *   - A state indicating if accumulator is ready or if input is being entered.
 *   - A state indicating the current pending operation
 *
 *
 * @author ggraham
 */
public interface ICalculator {

    public INumberEntry getAccumulator();
    public INumberEntry getEntry();
    public INumberEntry getMemory();
    public Object getDisplayMode();
    public Object getPendingOp();

    public boolean isNAN();
    public boolean isOE();
    public boolean isUE();

    public boolean isNumericError();

    public void HandleInput(Object code);

}

