package com.widgetmath.handycalculator.calculator;

import com.widgetmath.handycalculator.utils.DisplayMode;
import com.widgetmath.handycalculator.utils.INumberEntry;

/**
 * Interface for a simple calculator with the following features:
 *   - An accumulator to hold results
 *   - A single numeric entry slot
 *   - A single memory slot
 *   - A property indicating if accumulator is ready or if input is being entered.
 *   - A property indicating the current pending operation (for infix operations)
 *
 *
 * @author ggraham
 */
public interface ICalculator {

    /**
     * Returns the accumulator object, which holds results of operations.
     *
     * @return : the accumulator object
     */
    INumberEntry getAccumulator();

    /**
     * Returns the entry object, which holds current numeric entry while user is building it.
     *
     * @return : the entry object
     */
    INumberEntry getEntry();

    /**
     * Returns the memory object, which holds current value stored in memory .
     *
     * @return : the memory object
     */
    INumberEntry getMemory();

    /**
     * The current display mode - accumulator when there is a result to display, or
     * entry when the user is building a number entry
     *
     * @return : The current display mode
     */
    DisplayMode getDisplayMode();

    /**
     * Clears the accumulator and entry.
     *
     * @param clearMem : If true, will clear the memory as well
     */
    void clear(boolean clearMem);

    /**
     * Handles button input
     *
     * @param code : A code of
     */
    void HandleInput(Object code);

}

