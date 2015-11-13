package com.widgetmath.handycalculator.calculator;

import com.widgetmath.handycalculator.utils.DisplayMode;
import com.widgetmath.handycalculator.utils.INumberEntry;
import com.widgetmath.handycalculator.utils.NumberEntry_Wrapper;

/**
 * Abstract implementation of the ICalculator interface
 *
 * Created by ggraham412 on 11/1/2015.
 */
public abstract class Calculator implements ICalculator {

    // Data members
    private INumberEntry m_accumulator;
    private INumberEntry m_numberEntry;
    private INumberEntry m_memoryValue;

    // State indicators
    private DisplayMode m_displayMode;

    /**
     * Constructor
     */
    public Calculator() {
        m_accumulator = new NumberEntry_Wrapper();
        m_numberEntry = new NumberEntry_Wrapper();
        m_memoryValue = new NumberEntry_Wrapper();
        m_displayMode = DisplayMode.ACCUMULATOR;
    }

    @Override
    public void clear(boolean clearMem) {
        m_accumulator.clear();
        m_numberEntry.clear();
        if ( clearMem ) m_memoryValue.clear();
    }

    @Override
    public INumberEntry getAccumulator() {
        return m_accumulator;
    }

    @Override
    public INumberEntry getEntry() {
        return m_numberEntry;
    }

    @Override
    public INumberEntry getMemory() {
        return m_memoryValue;
    }

    @Override
    public DisplayMode getDisplayMode() {
        return m_displayMode;
    }
    protected void setDisplayMode(Object mode) {
        m_displayMode = (DisplayMode)mode;
    }

    @Override
    public abstract void HandleInput(Object code);

}
