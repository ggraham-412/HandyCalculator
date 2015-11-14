package com.widgetmath.handycalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web);

        WebView browser = (WebView)findViewById(R.id.webview);
        WebSettings settings = browser.getSettings();
        settings.setJavaScriptEnabled(false);
        settings.setDefaultTextEncodingName("utf-8");

        Intent i = getIntent();
        String url = i.getExtras().getString("URL");

        browser.loadUrl("file:///android_asset/" + url);    }
}
