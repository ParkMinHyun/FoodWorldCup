package com.example.parkminhyun.foodworldcup;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;


public class ResultFoodInfoActivity extends AppCompatActivity {

    FloatingActionButton startBtn;

    private static final String TAG = "ResultFoodInfoActivity";

    private String storeURL;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_food_info);

        propertyInit();

    }

    private void propertyInit() {

        startBtn = (FloatingActionButton)findViewById(R.id.fab_reviewBtn);

        // 검색 URL 받기
        storeURL = getIntent().getExtras().getString("StoreURL");

        // webView 띄우기
        webView = (WebView)findViewById(R.id.foodStoreWebview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(storeURL);
    }

    public void reviewButtonClick(View view) {

        startBtn.setColorFilter(Color.YELLOW);
        Toast.makeText(getApplicationContext(),"clicekced",Toast.LENGTH_SHORT).show();

    }
}