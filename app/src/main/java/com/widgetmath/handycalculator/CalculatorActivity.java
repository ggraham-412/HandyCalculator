package com.widgetmath.handycalculator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class CalculatorActivity extends AppCompatActivity {


    private boolean m_toolsVisible = false;
    private View m_mainLayout;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;

            case R.id.action_help:
                i = new Intent(this, WebActivity.class);
                i.putExtra("URL", "help.html");
                startActivity(i);
                return true;

            case R.id.action_about:
                i = new Intent(this, WebActivity.class);
                i.putExtra("URL", "about.html");
                startActivity(i);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calculator);

        m_mainLayout = findViewById(R.id.layoutMain);
        m_mainLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        HandyCalculatorFragment fragment = new HandyCalculatorFragment();
        getFragmentManager().beginTransaction().add(R.id.layoutMain, fragment).commit();

    }

}
