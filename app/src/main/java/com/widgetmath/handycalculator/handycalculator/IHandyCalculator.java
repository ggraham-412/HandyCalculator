package com.widgetmath.handycalculator.handycalculator;

import com.widgetmath.handycalculator.calculator.ICalculator;

/**
 * Interface for HandyCalculator
 *    -Supports infix notation with pending op
 *    -Fractional base display in Decimal or 1/2, 1/4, 1/8, 1/16, 1/32, 1/64
 *
 * Created by ggraham412 on 11/1/2015.
 */
public interface IHandyCalculator extends ICalculator {

    /**
     * Gets the fractional display base of the calculator for accumulator display.
     *
     * @return : returns fractional display base
     */
    public BaseMode getDisplayBase();

    /**
     * Indicates if there is a pending operation
     *
     * @return : true if there is a pending operation
     */
    public boolean isDisplayPending();

    /**
     * The current pending operation (to support infix notation)
     *
     * @return : The current pending operation, or null
     */
    ButtonCode getPendingOp();

}
