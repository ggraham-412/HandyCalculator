package com.widgetmath.handycalculator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.widgetmath.handycalculator.utils.DisplayMode;
import com.widgetmath.handycalculator.handycalculator.ButtonCode;
import com.widgetmath.handycalculator.handycalculator.HandyCalculator;
import com.widgetmath.handycalculator.handycalculator.IHandyCalculator;
import com.widgetmath.handycalculator.utils.DisplayEntry;
import com.widgetmath.handycalculator.utils.INumberEntry;

import java.math.BigDecimal;

public class HandyCalculatorFragment extends Fragment {

    // Constants
    public static final String key_numericValue = "numericValue";
    public static final String key_baseValue = "baseValue";
    public static final String key_memNumericValue = "memNumericValue";
    public static final String key_memBaseValue = "memBaseValue";

    // UI Input Elements
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
    private Button m_buttonMS;
    private Button m_buttonMR;

    // UI Output Elements
    private TextView m_txtDisplayMain;
    private TextView m_txtDisplayRemainder;
    private TextView m_txtDisplayMode;
    private TextView m_txtDisplayBase;
    private TextView m_txtDisplayOp;
    private TextView m_txtDisplayMem;

    // UI Debug output
    private TextView m_txtAccum;
    private TextView m_txtEntry;
    private View m_debugView;

    // UI Main View - where this fragment gets inflated
    private View m_mainView;

    // Model object
    private IHandyCalculator m_calculator;

    /**
     * After UI is inflated, finds UI elements by resource ID
     */
    private void findAndInitializeUI() {
        m_button0 = initButton(R.id.btn0, ButtonCode.ZERO);
        m_button1 = initButton(R.id.btn1, ButtonCode.ONE);
        m_button2 = initButton(R.id.btn2, ButtonCode.TWO);
        m_button3 = initButton(R.id.btn3, ButtonCode.THREE);
        m_button4 = initButton(R.id.btn4, ButtonCode.FOUR);
        m_button5 = initButton(R.id.btn5, ButtonCode.FIVE);
        m_button6 = initButton(R.id.btn6, ButtonCode.SIX);
        m_button7 = initButton(R.id.btn7, ButtonCode.SEVEN);
        m_button8 = initButton(R.id.btn8, ButtonCode.EIGHT);
        m_button9 = initButton(R.id.btn9, ButtonCode.NINE);
        m_buttonPlu = initButton(R.id.btnPlu, ButtonCode.ADD);
        m_buttonMin = initButton(R.id.btnMin, ButtonCode.SUBTRACT);
        m_buttonMul = initButton(R.id.btnMul, ButtonCode.MULTIPLY);
        m_buttonDiv = initButton(R.id.btnDiv, ButtonCode.DIVIDE);
        m_buttonCE = initButton(R.id.btnCE, ButtonCode.CLEAR_ENTRY);
        m_buttonC = initButton(R.id.btnC, ButtonCode.CLEAR);
        m_buttonEq = initButton(R.id.btnEq, ButtonCode.EQUALS);
        m_buttonChs = initButton(R.id.btnCHS, ButtonCode.CHANGE_SIGN);
        m_buttonDot = initButton(R.id.btnDot, ButtonCode.DEC_DECIMAL);
        m_buttonDot2 = initButton(R.id.btnDot2, ButtonCode.DEC_HALF);
        m_buttonDot4 = initButton(R.id.btnDot4, ButtonCode.DEC_FOURTH);
        m_buttonDot8 = initButton(R.id.btnDot8, ButtonCode.DEC_EIGHTH);
        m_buttonDot16 = initButton(R.id.btnDot16, ButtonCode.DEC_SIXTEENTH);
        m_buttonDot32 = initButton(R.id.btnDot32, ButtonCode.DEC_THIRTYSECOND);
        m_buttonDot64 = initButton(R.id.btnDot64, ButtonCode.DEC_SIXTYFOURTH);
        m_buttonDotDisp = initButton(R.id.btnDisp, ButtonCode.DISPLAY);
        m_buttonMR = initButton(R.id.btnMR, ButtonCode.MEMORY_RECALL);
        m_buttonMS = initButton(R.id.btnMS, ButtonCode.MEMORY_SAVE);

        m_txtDisplayMain = (TextView) (m_mainView.findViewById(R.id.txtDisplayMain));
        m_txtDisplayMode = (TextView) (m_mainView.findViewById(R.id.txtMode));
        m_txtDisplayBase = (TextView) (m_mainView.findViewById(R.id.txtBase));
        m_txtDisplayRemainder = (TextView) (m_mainView.findViewById(R.id.txtRemainder));
        m_txtDisplayOp = (TextView) (m_mainView.findViewById(R.id.txtOp));
        m_txtDisplayMem = (TextView) (m_mainView.findViewById(R.id.txtMem));

        m_txtAccum = (TextView)(m_mainView.findViewById(R.id.txtAccum));
        m_txtEntry = (TextView)(m_mainView.findViewById(R.id.txtEntry));
        m_debugView = (View)(m_mainView.findViewById(R.id.viewDebug));
    }

    /**
     * Returns a configured Button object given the android resource id and
     * HandyCalculator button code.
     *
     * @param id : android resource ID of a button
     * @param code : Button code to assign to the button tag
     * @return : the configured Button object
     */
    public Button initButton(int id, ButtonCode code) {
        Button b = (Button)(m_mainView.findViewById(id));
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

    /**
     * Listens to a View (should be a button) for ButtonCode events and
     * dispatches the ButtonCode
     *
     * @param v : The calling View
     */
    private void dispatchButtonPush(View v) {
        if ( v.getTag() == null ) return;
        ButtonCode code = (ButtonCode)v.getTag();
        m_calculator.HandleInput(code);
        DoDisplay();
    }

    /**
     * View driver displays current state on the calculator output
     */
    private void DoDisplay() {

        // Get display parameters
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        boolean bDebugMode = SP.getBoolean(getString(R.string.pref_debugMode), false);
        String sFracMode = SP.getString(getString(R.string.pref_remainderBase),
                                        getString(R.string.opt_remainderBase_useDecimal));
        boolean bFracMode = sFracMode.equals(getString(R.string.opt_remainderBase_useDisplayBase));

        // Main Display and Remainder
        INumberEntry toDisplay;
        if ( m_calculator.getDisplayMode() == DisplayMode.ACCUMULATOR ) {
            toDisplay = m_calculator.getAccumulator();
            toDisplay.setValue(toDisplay.getValue(), m_calculator.getDisplayBase().getBase());
        }
        else {
            toDisplay = m_calculator.getEntry();
        }
        DisplayMainAndRemainder(toDisplay, bFracMode);

        // Mode indicators
        boolean isDisplayPending = m_calculator.isDisplayPending();
        m_txtDisplayMode.setText(isDisplayPending?"DSP":m_calculator.getDisplayMode().toString());
        DoDisplayPending(isDisplayPending);  // Enable/disable non-display buttons if Disp pending
        m_txtDisplayBase.setText(m_calculator.getDisplayBase().toString());
        m_txtDisplayOp.setText(m_calculator.getPendingOp().toString());
        // If there is a nonzero value in mem, display the "M"
        if (m_calculator.getMemory().getValue().compareTo(BigDecimal.ZERO)!=0) {
            m_txtDisplayMem.setText("M");
        }
        else {
            m_txtDisplayMem.setText("");
        }

        // Debug Modes
        m_debugView.setVisibility(bDebugMode ? View.VISIBLE : View.INVISIBLE);
        m_txtAccum.setText(m_calculator.getAccumulator().getValue().toPlainString());
        m_txtEntry.setText(m_calculator.getEntry().getValue().toPlainString());

        return;
    }

    /**
     * Displays a number in the main output display with remainder if using fractional base.
     *
     * @param toDisplay : The number to display
     */
    private void DisplayMainAndRemainder(INumberEntry toDisplay, boolean fracMode) {
        if ( m_calculator.isNAN() ) {
            DisplayError("NaN");
            return;
        }
        if ( m_calculator.isOE() ) {
            DisplayError("OE");
            return;
        }
        if ( m_calculator.isUE() ) {
            DisplayError("UE");
            return;
        }
        m_txtDisplayMain.setText(DisplayEntry.getMainDisplay(toDisplay, m_calculator.getDisplayMode()));
        m_txtDisplayRemainder.setText(DisplayEntry.getRemainderDisplay(toDisplay,
                m_calculator.getDisplayMode(),fracMode));
    }

    /**
     * Displays an arbitrary string in the main display, and blanks out the remainder
     *
     * @param code : An error code to duisplay in the main output
     */
    private void DisplayError(String code) {
        m_txtDisplayMain.setText(code);
        m_txtDisplayRemainder.setText("");
    }

    /**
     * When a user presses the "Disp" button to change the output display, they are
     * required to pick a display option (decimal, 1/2, 1/4th, etc) immediately.  To
     * enforce this, the non-display buttons will be disabled while the Disp is
     * pending.
     *
     * @param isDisplayPending : Indicates if the display button is pending
     */
    private void DoDisplayPending(boolean isDisplayPending) {
        m_button0.setEnabled(!isDisplayPending);
        m_button1.setEnabled(!isDisplayPending);
        m_button2.setEnabled(!isDisplayPending);
        m_button3.setEnabled(!isDisplayPending);
        m_button4.setEnabled(!isDisplayPending);
        m_button5.setEnabled(!isDisplayPending);
        m_button6.setEnabled(!isDisplayPending);
        m_button7.setEnabled(!isDisplayPending);
        m_button8.setEnabled(!isDisplayPending);
        m_button9.setEnabled(!isDisplayPending);
        m_buttonCE.setEnabled(!isDisplayPending);
        m_buttonC.setEnabled(!isDisplayPending);
        m_buttonMin.setEnabled(!isDisplayPending);
        m_buttonMul.setEnabled(!isDisplayPending);
        m_buttonPlu.setEnabled(!isDisplayPending);
        m_buttonDiv.setEnabled(!isDisplayPending);
        m_buttonEq.setEnabled(!isDisplayPending);
        m_buttonChs.setEnabled(!isDisplayPending);
        m_buttonMR.setEnabled(!isDisplayPending);
        m_buttonMS.setEnabled(!isDisplayPending);
    }


    /**
     * Constructor - empty
     */
    public HandyCalculatorFragment() {
    }

    // Android lifecycle

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create state
        m_calculator = new HandyCalculator();
        // Load state
        if ( savedInstanceState != null ) {
            int base = savedInstanceState.getInt(key_baseValue, 0);
            BigDecimal value = new BigDecimal(savedInstanceState.getString(key_numericValue, "0"));
            int memBase = savedInstanceState.getInt(key_memBaseValue, 0);
            BigDecimal memValue = new BigDecimal(savedInstanceState.getString(key_memNumericValue, "0"));
            m_calculator.clear(true);
            m_calculator.getAccumulator().setValue(value, base);
            m_calculator.getMemory().setValue(memValue, memBase);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(key_numericValue, m_calculator.getAccumulator().getValue().toPlainString());
        savedInstanceState.putInt(key_baseValue, m_calculator.getDisplayBase().getBase());
        savedInstanceState.putString(key_memNumericValue,
                m_calculator.getMemory().getValue().toPlainString());
        savedInstanceState.putInt(key_memBaseValue,
                m_calculator.getMemory().getBase());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        if ( m_mainView == null ) {
            m_mainView = inflater.inflate(R.layout.fragment_handycalculator, container, false);
            // Must be done after inflation
            findAndInitializeUI();
        }
        // Display current state to remove any leftover UI defaults
        DoDisplay();

        return m_mainView;
    }

    @Override
    public void onResume() {
        super.onResume();
        DoDisplay();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
