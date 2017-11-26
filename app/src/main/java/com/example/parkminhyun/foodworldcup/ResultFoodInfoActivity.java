package com.example.parkminhyun.foodworldcup;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ResultFoodInfoActivity extends AppCompatActivity {

    private static final String TAG = "ResultFoodInfoActivity";

    private String storeURL;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_food_info);

        // 검색 URL 받기
        storeURL = getIntent().getExtras().getString("StoreURL");

        // webView 띄우기
        webView = (WebView)findViewById(R.id.foodStoreWebview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(storeURL);
    }

}