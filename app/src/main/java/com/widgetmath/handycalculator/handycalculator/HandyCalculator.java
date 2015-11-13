package com.widgetmath.handycalculator.handycalculator;

import com.widgetmath.handycalculator.calculator.Calculator;
import com.widgetmath.handycalculator.utils.DisplayMode;

import java.math.BigDecimal;

/**
 * Implementation of IHandyCalculator extending Calculator
 *
 * This class implements the state diagram of a simple infix calculator
 *
 * Created by ggraham412 on 11/1/2015.
 */
public class HandyCalculator extends Calculator implements IHandyCalculator {

    // Constants
    public static final int MAX_SCALE = 16;
    public static final BigDecimal LARGEST = new BigDecimal("9999999999");
    public static final BigDecimal SMALLEST = new BigDecimal("0.0000000001");

    // Additional State
    private BaseMode m_displayBase;  // The fractional display base
    private boolean m_dispPending;   // indicates if the user is midstream in choosing the display base.
    private ButtonCode m_pendingOp;  // supports infix operations

    /**
     * Constructor
     */
    public HandyCalculator() {
        m_displayBase = BaseMode.DECIMAL;
        m_dispPending = false;
        m_pendingOp = ButtonCode.NULL;
    }

    // Properties

    @Override
    public BaseMode getDisplayBase() {
        return m_displayBase;
    }

    @Override
    public boolean isDisplayPending() {
        return m_dispPending;
    }

    @Override
    public ButtonCode getPendingOp() {
        return m_pendingOp;
    }
    protected void setPendingOp(Object pendingOp) {
        m_pendingOp = (ButtonCode)pendingOp;
    }


    // State diagram implementation

    @Override
    public void HandleInput(Object _code) {

        // Cast to our button code
        ButtonCode code = (ButtonCode)_code;

        // Check if the user has pressed the "DISP" key to change the accumulator display base
        if ( m_dispPending ) {
            // Handle the display change, but do nothing else
            if ( HandleDispPending(code) ) {
                // If the user made a choice, clear the display pending flag
                m_dispPending = false;
            }
            return;
        }

        // If we are not in the display pending state, handle the button code.
        switch(code) {

            case DISPLAY:
                // Go into display pending mode
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
                // Handle numeric input
                HandleNumberInput(code);
                break;

            case ADD:
            case SUBTRACT:
            case MULTIPLY:
            case DIVIDE:
                // Handle operations
                HandleOperator(code);
                break;

            case CHANGE_SIGN:
                // This is a simple unary transform
                HandleChangeSign(code);
                break;

            case MEMORY_RECALL:
            case MEMORY_SAVE:
                // Handle memory ops
                HandleMemoryOp(code);
                break;

            case DEC_DECIMAL:
            case DEC_HALF:
            case DEC_FOURTH:
            case DEC_EIGHTH:
            case DEC_SIXTEENTH:
            case DEC_THIRTYSECOND:
            case DEC_SIXTYFOURTH:
                // Handle the "decimators" - the keys that function like decimal key
                // but are for fractions too
                HandleDecimator(code);
                break;

            case EQUALS:
            case CLEAR:
            case CLEAR_ENTRY:
                // Handle administrative operations
                HandleAdmin(code);
                break;
        }

    }

    /**
     * Handles a digit input
     *
     * @param code : The button code of a digit 0-9
     */
    private void HandleNumberInput(ButtonCode code) {
        // If there is a numerical error, force the user to hit "C"
        if ( !getAccumulator().isValid() ) return;
        // Add the digit to "Entry" and make sure the entry will be displayed
        getEntry().addDigit(code.getValue());
        setDisplayMode(DisplayMode.ENTRY);
    }

    /**
     * Handles operator input + - x /
     *
     * @param code : A button code representing one of the operations + - x /
     */
    private void HandleOperator(ButtonCode code) {
        // If there is a numerical error, force the user to hit "C"
        if ( !getAccumulator().isValid() ) return;
        // If the display mode is accumulator, assume the user intends to start
        // an operation with the accumulator value - load it into entry and go
        // into entry display mode
        if ( getDisplayMode() == DisplayMode.ACCUMULATOR ) {
            getEntry().setValue(getAccumulator());
            setDisplayMode(DisplayMode.ENTRY);
        }

        // Check if there was a previous operation - in which case the current
        // operation may be signaling the end of input of a second operand.
        // Handle it now.
        HandlePendingOp();

        // To support infix notation, we need to wait for the second operand, so
        // set the current operation as "pending"
        setPendingOp(code);
    }

    /**
     * Handles the pending operation after the end of input of the second operand
     */
    private void HandlePendingOp() {
        // If there is a numerical error, force the user to hit "C"
        if ( !getAccumulator().isValid() ) return;

        // Check if there is a pending op
        if ( getPendingOp() == ButtonCode.NULL ) {
            // If there is no pending operation, then this is the first operand
            // In that case we shove the value into the accumulator taking care to
            // use whatever display base is currently specified
            getAccumulator().setValue(getEntry().getValue(), m_displayBase.getBase());
        } else {
            // If there is a pending op, complete it and store results in the accumulator
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
                    // Check for div by zero
                    if ( operand.compareTo(BigDecimal.ZERO) == 0) {
                        getAccumulator().setNAN(true);  // causes isNumericError to be true
                    }
                    else {
                        getAccumulator().setValue(getAccumulator().getValue().divide(getEntry().getValue(),
                                        MAX_SCALE, BigDecimal.ROUND_HALF_EVEN),
                                m_displayBase.getBase());
                    }
                    break;
            }
        }

        // Set diaply mode to accumulator and clear the entry
        setDisplayMode(  DisplayMode.ACCUMULATOR );
        getEntry().clear();
    }

    /**
     * Unary change sign
     *
     * @param code : Should be he change sign button code
     */
    private void HandleChangeSign(ButtonCode code) {
        // If there is a numerical error, force the user to hit "C"
        if ( !getAccumulator().isValid() ) return;
        if ( getDisplayMode() == DisplayMode.ACCUMULATOR ) {
            getAccumulator().negate();
        }
        else {
            getEntry().negate();
        }
    }

    /**
     * Memory operations
     *
     * @param code : A memory operation MR or MS
     */
    private void HandleMemoryOp(ButtonCode code) {
        // If there is a numerical error, force the user to hit "C"
        if ( !getAccumulator().isValid() ) return;

        switch (code){
            case MEMORY_RECALL:
                // Copy the memory value into the entry
                getEntry().setValue(getMemory());
                setDisplayMode( DisplayMode.ENTRY);
                break;
            case MEMORY_SAVE:
                // Copy the value to memory depending on display mode
                if ( getDisplayMode() == DisplayMode.ACCUMULATOR ) {
                    getMemory().setValue(getAccumulator());
                } else {
                    getMemory().setValue(getEntry());
                }
                break;
        }
    }

    /**
     * Handles an administrative button input
     *
     * @param code : Clear, clear entry or equals
     */
    private void HandleAdmin(ButtonCode code) {
        switch (code) {
            case CLEAR:
                clear(false);
                setPendingOp(ButtonCode.NULL);
                setDisplayMode(DisplayMode.ACCUMULATOR);
                getAccumulator().setNAN(false);
                break;
            case CLEAR_ENTRY:
                // If there is a numerical error, force the user to hit "C"
                if ( !getAccumulator().isValid() ) return;
                getEntry().clear();
                if ( getDisplayMode() == DisplayMode.ACCUMULATOR ) {
                    getAccumulator().clear();
                    setPendingOp( ButtonCode.NULL );
                }
                break;
            case EQUALS:
                // If there is a numerical error, force the user to hit "C"
                if ( !getAccumulator().isValid() ) return;
                if ( getPendingOp() != ButtonCode.NULL ) {
                    HandlePendingOp();
                    setDisplayMode(DisplayMode.ACCUMULATOR);
                    getEntry().clear();
                }
                setPendingOp(ButtonCode.NULL);
        }
    }

    /**
     * Handles input from a fractional button.  When this is the decimal, we
     * start adding digits to the right of the dot.  When this is a fractional base,
     * we display the base and start adding to the numerator.
     *
     * @param code : A code for ., 1/2, 1/4, 1/8, 1/16, 1/32, or 1/64
     */
    private void HandleDecimator(ButtonCode code) {
        // Don't check numeric here
        switch (getDisplayMode()) {
            case ACCUMULATOR:
                // When pushing a decimator button from accumulator,
                // we treat it like fresh numeric input of a purely fractional number > 1
                if ( !getEntry().isDotPushed() ) {
                    getEntry().pushDot(code.getValue());
                    setDisplayMode( DisplayMode.ENTRY );
                }
                break;
            case ENTRY:
                // When pushing a decimator button from entry, we push the frac base
                if ( !getEntry().isDotPushed() ) getEntry().pushDot(code.getValue());
                break;
        }
    }

    /**
     * Handle the next button code after a display change operation.
     *
     * @param code : A code for ., 1/2, 1/4, 1/8, 1/16, 1/32, or 1/64
     * @return : true if the user made a display fraction choice, or if the
     *           user canceled out of a display change
     */
    private boolean HandleDispPending(ButtonCode code) {
        switch(code) {
            case DEC_DECIMAL:
            case DEC_HALF:
            case DEC_FOURTH:
            case DEC_EIGHTH:
            case DEC_SIXTEENTH:
            case DEC_THIRTYSECOND:
            case DEC_SIXTYFOURTH:
                m_displayBase = BaseMode.getFracMode(code.getValue());
                boolean isNan = getAccumulator().isNAN();
                getAccumulator().setValue(getAccumulator().getValue(), m_displayBase.getBase());
                getAccumulator().setNAN(isNan);
                return true;
            case DISPLAY:
                m_dispPending = false;
                return true;
            default:
                return false;
        }
    }

}
