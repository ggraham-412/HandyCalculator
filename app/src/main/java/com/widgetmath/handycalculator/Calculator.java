package com.widgetmath.handycalculator;

import com.widgetmath.handycalculator.utils.DisplayEntry;
import com.widgetmath.handycalculator.utils.INumberEntry;
import com.widgetmath.handycalculator.utils.NumberEntry_Wrapper;
import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Calculator extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    // UI Elements
    private Button m_button0;
    private Button m_button1;
    private Button m_button2;
    private Button m_button3;
    private Button m_button4;
    private Button m_button5;
    private Button m_button6;
    private Button m_button7;
    private Button m_button8;
    private Button m_button9;
    private Button m_buttonDiv;
    private Button m_buttonMul;
    private Button m_buttonMin;
    private Button m_buttonPlu;
    private Button m_buttonDot;
    private Button m_buttonEq;
    private Button m_buttonC;
    private Button m_buttonCE;
    private Button m_buttonChs;
    private Button m_buttonDot2;
    private Button m_buttonDot4;
    private Button m_buttonDot8;
    private Button m_buttonDot16;
    private Button m_buttonDot32;
    private Button m_buttonDot64;
    private Button m_buttonDotDisp;
    private Button m_memorySave;
    private Button m_memoryRecall;

    private TextView m_txtDisplayMain;
    private TextView m_txtDisplayRemainder;
    private TextView m_txtDisplayMode;
    private TextView m_txtDisplayBase;
    private TextView m_txtDisplayOp;
    private TextView m_txtDisplayMem;

    private TextView m_txtAccum;
    private TextView m_txtEntry;

    private View m_mainLayout;

    private boolean m_toolsVisible = false;

    private INumberEntry m_accumulator = new NumberEntry_Wrapper();
    private INumberEntry m_numberEntry = new NumberEntry_Wrapper();
    private DisplayMode m_displayMode = DisplayMode.ENTRY;
    private FracMode m_displayBase = FracMode.DECIMAL;
    private ButtonCode m_pendingOp = ButtonCode.NULL;
    private INumberEntry m_memoryValue = new NumberEntry_Wrapper(BigDecimal.ZERO, 0);

    private boolean m_isNan = false;
    private boolean m_isOE = false;
    private boolean m_isUE = false;

    private boolean m_dispPending = false;

    private void findAndInitializeUI() {
        m_button0 = initButton(R.id.button0, ButtonCode.ZERO);
        m_button1 = initButton(R.id.button1, ButtonCode.ONE);
        m_button2 = initButton(R.id.button2, ButtonCode.TWO);
        m_button3 = initButton(R.id.button3, ButtonCode.THREE);
        m_button4 = initButton(R.id.button4, ButtonCode.FOUR);
        m_button5 = initButton(R.id.button5, ButtonCode.FIVE);
        m_button6 = initButton(R.id.button6, ButtonCode.SIX);
        m_button7 = initButton(R.id.button7, ButtonCode.SEVEN);
        m_button8 = initButton(R.id.button8, ButtonCode.EIGHT);
        m_button9 = initButton(R.id.button9, ButtonCode.NINE);
        m_buttonPlu = initButton(R.id.buttonPlu, ButtonCode.ADD);
        m_buttonMin = initButton(R.id.buttonMin, ButtonCode.SUBTRACT);
        m_buttonMul = initButton(R.id.buttonMul, ButtonCode.MULTIPLY);
        m_buttonDiv = initButton(R.id.buttonDiv, ButtonCode.DIVIDE);
        m_buttonCE = initButton(R.id.buttonCE, ButtonCode.CLEAR_ENTRY);
        m_buttonC = initButton(R.id.buttonC, ButtonCode.CLEAR);
        m_buttonEq = initButton(R.id.buttonEq, ButtonCode.EQUALS);
        m_buttonChs = initButton(R.id.buttonCHS, ButtonCode.CHANGE_SIGN);
        m_buttonDot = initButton(R.id.buttonDot, ButtonCode.DEC_DECIMAL);
        m_buttonDot2 = initButton(R.id.buttonDot2, ButtonCode.DEC_HALF);
        m_buttonDot4 = initButton(R.id.buttonDot4, ButtonCode.DEC_FOURTH);
        m_buttonDot8 = initButton(R.id.buttonDot8, ButtonCode.DEC_EIGHTH);
        m_buttonDot16 = initButton(R.id.buttonDot16, ButtonCode.DEC_SIXTEENTH);
        m_buttonDot32 = initButton(R.id.buttonDot32, ButtonCode.DEC_THIRTYSECOND);
        m_buttonDot64 = initButton(R.id.buttonDot64, ButtonCode.DEC_SIXTYFOURTH);
        m_buttonDotDisp = initButton(R.id.buttonDisp, ButtonCode.DISPLAY);
        m_memoryRecall = initButton(R.id.buttonMR, ButtonCode.MEMORY_RECALL);
        m_memorySave = initButton(R.id.buttonMS, ButtonCode.MEMORY_SAVE);
        m_mainLayout = findViewById(R.id.layoutMain);

        m_txtDisplayMain = (TextView) findViewById(R.id.txtDisplayMain);
        m_txtDisplayMode = (TextView) findViewById(R.id.textMode);
        m_txtDisplayBase = (TextView) findViewById(R.id.textFrac);
        m_txtDisplayRemainder = (TextView) findViewById(R.id.textRemainder);
        m_txtDisplayOp = (TextView) findViewById(R.id.textOP);
        m_txtDisplayMem = (TextView) findViewById(R.id.textMEM);

        m_txtAccum = (TextView)findViewById(R.id.txtAccum);
        m_txtEntry = (TextView)findViewById(R.id.txtEntry);
    }

    public Button initButton(int id, ButtonCode code) {
        Button b = (Button)findViewById(id);
        if ( b == null ) return null;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dispatchButtonPush(arg0);
            }
        });
        b.setTag(code);
        return b;
    }

    private boolean isNumericError() {
        return m_isNan || m_isOE || m_isUE;
    }

    private void HandleMemoryOp(ButtonCode code) {
        switch (code){
            case MEMORY_RECALL:
                m_numberEntry.setValue(m_memoryValue);
                m_displayMode = DisplayMode.ENTRY;
                break;
            case MEMORY_SAVE:
                if ( isNumericError() ) return;
                if ( m_displayMode == DisplayMode.ACCUMULATOR ) {
                    m_memoryValue.setValue(m_accumulator);
                } else {
                    m_memoryValue.setValue(m_numberEntry);
                }
                break;
        }
    }

    private void HandleNumberInput(ButtonCode code) {
        if ( isNumericError() ) return;
        m_numberEntry.addDigit(code.getValue());
        m_displayMode = DisplayMode.ENTRY;
    }

    private void HandleOperator(ButtonCode code) {
        if ( isNumericError() ) return;
        HandlePendingOp();
        m_pendingOp = code;
    }

    private void HandleChangeSign(ButtonCode code) {
        if ( isNumericError() ) return;
        if ( m_displayMode == DisplayMode.ACCUMULATOR ) {
            m_accumulator.negate();
        }
        else {
            m_numberEntry.negate();
        }
    }

    private void HandlePendingOp() {
        if ( isNumericError() ) return;
        if ( m_pendingOp == ButtonCode.NULL ) {
            m_accumulator.setValue(m_numberEntry.getValue(), m_displayBase.getBase());
        } else {
            switch (m_pendingOp) {
                case ADD:
                    m_accumulator.setValue(m_accumulator.getValue().add(m_numberEntry.getValue()),
                            m_displayBase.getBase());
                    break;
                case SUBTRACT:
                    m_accumulator.setValue(m_accumulator.getValue().subtract(m_numberEntry.getValue()),
                            m_displayBase.getBase());
                    break;
                case MULTIPLY:
                    m_accumulator.setValue(m_accumulator.getValue().multiply(m_numberEntry.getValue()),
                            m_displayBase.getBase());
                    break;
                case DIVIDE:
                    BigDecimal operand = m_numberEntry.getValue();
                    if ( operand.compareTo(BigDecimal.ZERO) == 0) {
                        m_isNan = true;
                    }
                    else {
                        m_accumulator.setValue(m_accumulator.getValue().divide(m_numberEntry.getValue(), 16, BigDecimal.ROUND_HALF_EVEN),
                                m_displayBase.getBase());
                    }
                    break;
            }
        }
        m_displayMode = DisplayMode.ACCUMULATOR;
        m_numberEntry.clear();
    }

    private void HandleAdmin(ButtonCode code) {
        switch (code) {
            case CLEAR:
                m_numberEntry.clear();
                m_accumulator.clear();
                m_pendingOp = ButtonCode.NULL;
                m_displayMode = DisplayMode.ACCUMULATOR;
                m_isUE = false;
                m_isOE = false;
                m_isNan = false;
                break;
            case CLEAR_ENTRY:
                if ( isNumericError() ) return;
                m_numberEntry.clear();
                if ( m_displayMode == DisplayMode.ACCUMULATOR ) {
                    m_accumulator.clear();
                    m_pendingOp = ButtonCode.NULL;
                }
                break;
            case EQUALS:
                if ( isNumericError() ) return;
                HandlePendingOp();
                m_displayMode = DisplayMode.ACCUMULATOR;
                m_pendingOp = ButtonCode.NULL;
        }
        m_numberEntry.clear();
    }

    private void HandleDecimator(ButtonCode code) {
        switch (m_displayMode) {
            case ACCUMULATOR:
                if ( !m_numberEntry.isDotPushed() ) {
                    m_numberEntry.pushDot(code.getValue());
                    m_displayMode = DisplayMode.ENTRY;
                }
                break;
            case ENTRY:
                if ( !m_numberEntry.isDotPushed() ) m_numberEntry.pushDot(code.getValue());
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
            default:
                return false;
        }
    }

    private void dispatchButtonPush(View v) {

        ButtonCode code = (ButtonCode)v.getTag();

        if ( m_dispPending ) {
            if ( HandleDispPending(code) ) {
                m_dispPending = false;
                DoDisplay();
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

        DoDisplay();

    }

    private void DisplayError(String code) {
        m_txtDisplayMain.setText(code);
        m_txtDisplayRemainder.setText("");
    }

    private void DisplayNumber(INumberEntry toDisplay) {
        if ( m_isNan ) {
            DisplayError("NaN");
            return;
        }
        if ( m_isOE ) {
            DisplayError("OE");
            return;
        }
        if ( m_isUE ) {
            DisplayError("UE");
            return;
        }
        m_txtDisplayMain.setText(DisplayEntry.getMainDisplay(toDisplay, m_displayMode));
        m_txtDisplayRemainder.setText(DisplayEntry.getRemainderDisplay(toDisplay, m_displayMode));
    }

    private void DoDisplay() {

        if ( m_displayMode == DisplayMode.ACCUMULATOR ) {
            m_accumulator.setValue(m_accumulator.getValue(), m_displayBase.getBase());
            DisplayNumber(m_accumulator);
        }
        else {
            DisplayNumber(m_numberEntry);
        }

        m_txtDisplayBase.setText(m_displayBase.toString());
        m_txtDisplayMode.setText(m_displayMode.toString());
        m_txtDisplayOp.setText(m_pendingOp.toString());

        m_txtAccum.setText(m_accumulator.getValue().toPlainString());
        m_txtEntry.setText(m_numberEntry.getValue().toPlainString());

        if (m_memoryValue.getValue().compareTo(BigDecimal.ZERO)!=0) {
            m_txtDisplayMem.setText("M");
        }
        else {
            m_txtDisplayMem.setText("");
        }

        return;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calculator);

        findAndInitializeUI();

        // Set up the user interaction to manually show or hide the system UI.
        m_mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
        DoDisplay();
    }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private void toggle() {
        if (m_toolsVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        m_toolsVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            m_mainLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        m_mainLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        m_toolsVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };

    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
