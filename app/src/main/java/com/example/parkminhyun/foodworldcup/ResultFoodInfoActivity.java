package com.example.parkminhyun.foodworldcup;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ResultFoodInfoActivity extends AppCompatActivity {

    @BindView(R.id.fab_review)
    FloatingActionButton reviewBtn;

    private static final String TAG = "ResultFoodInfoActivity";

    private String storeURL;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_food_info);
        ButterKnife.bind(this);

        // 검색 URL 받기
        storeURL = getIntent().getExtras().getString("StoreURL");

        // webView 띄우기
        webView = (WebView)findViewById(R.id.foodStoreWebview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(storeURL);
    }


    @OnClick(R.id.fab_review)
    void reviewButtonClick() {
        Toast.makeText(getApplicationContext(),"clicekced",Toast.LENGTH_SHORT).show();
    }
}