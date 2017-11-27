package com.example.parkminhyun.foodworldcup;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;


public class ResultFoodInfoActivity extends AppCompatActivity {

    FloatingActionButton startBtn;
    FavoritesStoreSharedPreferences favoritesStoreSharedPreferences;

    private static final String TAG = "ResultFoodInfoActivity";

    private Boolean favoritesFlag;
    private String storeURL;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_food_info);

        propertyInit();

    }

    private void propertyInit() {

        startBtn = (FloatingActionButton) findViewById(R.id.fab_reviewBtn);

        // 현재 화면 즐겨찾기 상태 불러오기
        favoritesStoreSharedPreferences = FavoritesStoreSharedPreferences.getInstance();
        // 검색 URL 받기
        storeURL = getIntent().getExtras().getString("StoreURL");
        // SharedPreference를 통해 즐겨찾기 Flag값 파악하기
        getFavoritesStatus();

        // webView 띄우기
        webView = (WebView) findViewById(R.id.foodStoreWebview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(storeURL);
    }

    private void getFavoritesStatus() {
        favoritesFlag = favoritesStoreSharedPreferences.getPreferences(this, storeURL);
        changeTintColorOfFavoritesBtn(favoritesFlag);
    }

    public void favoritesStoreBtnClick(View view) {

        // 즐겨찾기 상태, TintColor 바꾸기
        favoritesFlag = (favoritesFlag == false) ? true : false;
        Log.i("Info Activity", String.valueOf(favoritesFlag));
        changeTintColorOfFavoritesBtn(favoritesFlag);

        favoritesStoreSharedPreferences.savePreferences(this, storeURL, favoritesFlag);
    }

    private void changeTintColorOfFavoritesBtn(Boolean favoritesFlag) {
        if (favoritesFlag == false)
            startBtn.setColorFilter(Color.WHITE);
        else
            startBtn.setColorFilter(Color.YELLOW);
    }
}