package com.example.parkminhyun.foodworldcup;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.parkminhyun.foodworldcup.ETC.FavoritesStoreSharedPreferences;


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

        // Toast 띄우기
        Toast.makeText(getApplicationContext(),
                (favoritesFlag)? "즐겨찾기 등록되었습니다." : "즐겨찾기 등록이 해제되었습니다." ,Toast.LENGTH_SHORT);
    }

    private void changeTintColorOfFavoritesBtn(Boolean favoritesFlag) {
        if (favoritesFlag == false)
            startBtn.setColorFilter(Color.WHITE);
        else
            startBtn.setColorFilter(Color.YELLOW);
    }
}