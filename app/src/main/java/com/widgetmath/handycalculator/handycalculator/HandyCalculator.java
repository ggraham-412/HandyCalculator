package com.widgetmath.handycalculator.handycalculator;

import com.widgetmath.handycalculator.calculator.Calculator;

import java.math.BigDecimal;

/**
 * Created by ggraham412 on 11/1/2015.
 */
public class HandyCalculator extends Calculator implements IHandyCalculator {


    private FracMode m_displayBase;
    private boolean m_dispPending;
    private ButtonCode m_pendingOp;
    private DisplayMode m_displayMode;

    public HandyCalculator() {
        m_displayBase = FracMode.DECIMAL;
        m_dispPending = false;
        m_pendingOp = ButtonCode.NULL;
    }

    @Override
    public DisplayMode getDisplayMode() {
        return m_displayMode;
    }
    protected void setDisplayMode(Object mode) {
        m_displayMode = (DisplayMode)mode;
    }

    @Override
    public FracMode getDisplayBase() {
        return m_displayBase;
    }

    @Override
    public boolean isDisplayPending() {
        return m_dispPending;
    }

    private void HandleMemoryOp(ButtonCode code) {
        switch (code){
            case MEMORY_RECALL:
                getEntry().setValue(getMemory());
                setDisplayMode( DisplayMode.ENTRY);
                break;
            case MEMORY_SAVE:
                if ( isNumericError() ) return;
                if ( getDisplayMode() == DisplayMode.ACCUMULATOR ) {
                    getMemory().setValue(getAccumulator());
                } else {
                    getMemory().setValue(getAccumulator());
                }
                break;
        }
    }

    private void HandleNumberInput(ButtonCode code) {
        if ( isNumericError() ) return;
        getEntry().addDigit(code.getValue());
        setDisplayMode(DisplayMode.ENTRY);
    }

    private void HandleOperator(ButtonCode code) {
        if ( isNumericError() ) return;
        if ( getDisplayMode() == DisplayMode.ACCUMULATOR ) {
            getEntry().setValue(getAccumulator());
            setDisplayMode(DisplayMode.ENTRY);
        }
        HandlePendingOp();
        setPendingOp(code);
    }

    private void HandleChangeSign(ButtonCode code) {
        if ( isNumericError() ) return;
        if ( getDisplayMode() == DisplayMode.ACCUMULATOR ) {
            getAccumulator().negate();
        }
        else {
            getEntry().negate();
        }
    }

    @Override
    public ButtonCode getPendingOp() {
        return m_pendingOp;
    }

    @Override
    protected void setPendingOp(Object pendingOp) {
        m_pendingOp = (ButtonCode)pendingOp;
    }

    private void HandlePendingOp() {
        if ( isNumericError() ) return;
        if ( getPendingOp() == ButtonCode.NULL ) {
            getAccumulator().setValue(getEntry().getValue(), m_displayBase.getBase());
        } else {
            switch (getPendingOp()) {
                case ADD:
                    getAccumulator().setValue(getAccumulator().getValue().add(getEntry().getValue()),
                            m_displayBase.getBase());
                    break;
                case SUBTRACT:
                    getAccumulator().setValue(getAccumulator().getValue().subtract(getEntry().getValue()),
                            m_displayBase.getBase());
                    break;
                case MULTIPLY:
                    getAccumulator().setValue(getAccumulator().getValue().multiply(getEntry().getValue()),
                            m_displayBase.getBase());
                    break;
                case DIVIDE:
                    BigDecimal operand = getEntry().getValue();
                    if ( operand.compareTo(BigDecimal.ZERO) == 0) {
                        setNAN( true );
                    }
                    else {
                        getAccumulator().setValue(getAccumulator().getValue().divide(getEntry().getValue(),
                                        16, BigDecimal.ROUND_HALF_EVEN),
                                m_displayBase.getBase());
                    }
                    break;
            }
        }
        setDisplayMode(  DisplayMode.ACCUMULATOR );
        getEntry().clear();
    }

    private void HandleAdmin(ButtonCode code) {
        switch (code) {
            case CLEAR:
                getEntry().clear();
                getAccumulator().clear();
                setPendingOp(ButtonCode.NULL);
                setDisplayMode(DisplayMode.ACCUMULATOR);
                setOE(false);
                setUE(false);
                setNAN( false );
                break;
            case CLEAR_ENTRY:
                if ( isNumericError() ) return;
                getEntry().clear();
                if ( getDisplayMode() == DisplayMode.ACCUMULATOR ) {
                    getAccumulator().clear();
                    setPendingOp( ButtonCode.NULL );
                }
                break;
            case EQUALS:
                if ( isNumericError() ) return;
                HandlePendingOp();
                setDisplayMode( DisplayMode.ACCUMULATOR );
                setPendingOp( ButtonCode.NULL );
        }
        getEntry().clear();
    }

    private void HandleDecimator(ButtonCode code) {
        switch (getDisplayMode()) {
            case ACCUMULATOR:
                if ( !getEntry().isDotPushed() ) {
                    getEntry().pushDot(code.getValue());
                    setDisplayMode( DisplayMode.ENTRY );
                }
                break;
            case ENTRY:
                if ( !getEntry().isDotPushed() ) getEntry().pushDot(code.getValue());
                break;
        }
    }

    private boolean HandleDispPending(ButtonCode code) {
        switch(code) {
            case DEC_DECIMAL:
            case DEC_HALF:
            case DEC_FOURTH:
            case DEC_EIGHTH:
            case DEC_SIXTEENTH:
            case DEC_THIRTYSECOND:
            case DEC_SIXTYFOURTH:
                m_displayBase = FracMode.getFracMode(code.getValue());
                return true;
            case DISPLAY:
                m_dispPending = false;
                return true;
            default:
                return false;
        }
    }

    @Override
    public void HandleInput(Object _code) {
        ButtonCode code = (ButtonCode)_code;

        if ( m_dispPending ) {
            if ( HandleDispPending(code) ) {
                m_dispPending = false;
            }
            return;
        }

        switch(code) {
            case DISPLAY:
                m_dispPending = true;
                break;

            case ZERO:
            case ONE:
            case TWO:
            case THREE:
            case FOUR:
            case FIVE:
            case SIX:
            case SEVEN:
            case EIGHT:
            case NINE:
                HandleNumberInput(code);
                break;

            case ADD:
            case SUBTRACT:
            case MULTIPLY:
            case DIVIDE:
                HandleOperator(code);
                break;

            case CHANGE_SIGN:
                HandleChangeSign(code);
                break;

            case MEMORY_RECALL:
            case MEMORY_SAVE:
                HandleMemoryOp(code);
                break;

            case DEC_DECIMAL:
            case DEC_HALF:
            case DEC_FOURTH:
            case DEC_EIGHTH:
            case DEC_SIXTEENTH:
            case DEC_THIRTYSECOND:
            case DEC_SIXTYFOURTH:
                HandleDecimator(code);
                break;

            case EQUALS:
            case CLEAR:
            case CLEAR_ENTRY:
                HandleAdmin(code);
                break;
        }

    }
}
