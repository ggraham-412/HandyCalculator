package com.widgetmath.handycalculator.handycalculator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.widgetmath.handycalculator.R;
import com.widgetmath.handycalculator.calculator.ICalculator;
import com.widgetmath.handycalculator.utils.DisplayEntry;
import com.widgetmath.handycalculator.utils.INumberEntry;

import java.math.BigDecimal;

public class HandyCalculatorFragment extends Fragment {

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

    private View m_mainView;


    private ICalculator m_calculator;

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

        m_txtDisplayMain = (TextView) (m_mainView.findViewById(R.id.txtDisplayMain));
        m_txtDisplayMode = (TextView) (m_mainView.findViewById(R.id.textMode));
        m_txtDisplayBase = (TextView) (m_mainView.findViewById(R.id.textFrac));
        m_txtDisplayRemainder = (TextView) (m_mainView.findViewById(R.id.textRemainder));
        m_txtDisplayOp = (TextView) (m_mainView.findViewById(R.id.textOP));
        m_txtDisplayMem = (TextView) (m_mainView.findViewById(R.id.textMEM));

        m_txtAccum = (TextView)(m_mainView.findViewById(R.id.txtAccum));
        m_txtEntry = (TextView)(m_mainView.findViewById(R.id.txtEntry));
    }

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



    private void dispatchButtonPush(View v) {

        ButtonCode code = (ButtonCode)v.getTag();
        m_calculator.HandleInput(code);

        DoDisplay();
    }

    private void DisplayError(String code) {
        m_txtDisplayMain.setText(code);
        m_txtDisplayRemainder.setText("");
    }

    private void DisplayNumber(INumberEntry toDisplay) {
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
        m_txtDisplayMain.setText(DisplayEntry.getMainDisplay(toDisplay, (DisplayMode)m_calculator.getDisplayMode()));
        m_txtDisplayRemainder.setText(DisplayEntry.getRemainderDisplay(toDisplay, (DisplayMode)m_calculator.getDisplayMode()));
    }

    private void DoPending() {
        boolean isDisplayPending = ((IHandyCalculator)m_calculator).isDisplayPending();
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
        m_memoryRecall.setEnabled(!isDisplayPending);
        m_memorySave.setEnabled(!isDisplayPending);
    }

    private void DoDisplay() {

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        boolean bDebugMode = SP.getBoolean("debugMode", false);
        IHandyCalculator hcalc = (IHandyCalculator)m_calculator;

        if ( m_calculator.getDisplayMode() == DisplayMode.ACCUMULATOR ) {
            INumberEntry acc = hcalc.getAccumulator();
            acc.setValue(acc.getValue(), ((IHandyCalculator) m_calculator).getDisplayBase().getBase());
            DisplayNumber(acc);
        }
        else {
            DisplayNumber(hcalc.getEntry());
        }

        m_txtDisplayBase.setText(hcalc.getDisplayBase().toString());
        if ( ((IHandyCalculator) m_calculator).isDisplayPending() ) {
            m_txtDisplayMode.setText("DSP");
        }
        else {
            m_txtDisplayMode.setText(hcalc.getDisplayMode().toString());
        }
        m_txtDisplayOp.setText(hcalc.getPendingOp().toString());

        m_txtAccum.setText(hcalc.getAccumulator().getValue().toPlainString());
        m_txtEntry.setText(hcalc.getEntry().getValue().toPlainString());

        m_txtAccum.setVisibility(bDebugMode ? View.VISIBLE : View.INVISIBLE);
        m_txtEntry.setVisibility(bDebugMode? View.VISIBLE:View.INVISIBLE);

        if (hcalc.getMemory().getValue().compareTo(BigDecimal.ZERO)!=0) {
            m_txtDisplayMem.setText("M");
        }
        else {
            m_txtDisplayMem.setText("");
        }

        DoPending();

        return;

    }

    public HandyCalculatorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_calculator = new HandyCalculator();
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if ( m_mainView == null ) {
            m_mainView = inflater.inflate(R.layout.fragment_handycalculator, container, false);
        }
        findAndInitializeUI();
        DoDisplay();
        return m_mainView;
    }




}
