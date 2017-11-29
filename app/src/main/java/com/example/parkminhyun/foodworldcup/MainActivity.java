package com.example.parkminhyun.foodworldcup;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    boolean isPageOpen = false;

    Animation translateLeftAnim;
    Animation translateRightAnim;

    LinearLayout mainLayout;

    ImageButton myPageButton;

    ImageView myPageimageView;

    LinearLayout foodWorldCupLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 안태현
        // 안드로이드 statusBar Color 변경
        getWindow().setStatusBarColor(Color.parseColor("#E37FA8"));

        myPageButton = (ImageButton) findViewById(R.id.imgBtn_myPage);
        myPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPageOpen) {
                    mainLayout.startAnimation(translateRightAnim);
                } else {
                    mainLayout.setVisibility(View.VISIBLE);
                    mainLayout.startAnimation(translateLeftAnim);
                }
            }
        });

        mainLayout = (LinearLayout) findViewById(R.id.layout_myPage);

        // 애니메이션 객체 로딩
        translateLeftAnim = AnimationUtils.loadAnimation(this, R.anim.main_translate_left);
        translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.main_translate_right);

        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
        translateLeftAnim.setAnimationListener(animListener);
        translateRightAnim.setAnimationListener(animListener);

        foodWorldCupLayout = (LinearLayout) findViewById(R.id.ll_mainFoodWorldCup);
        foodWorldCupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MenuWorldCupActivity.class));
            }
        });

    }

    private class SlidingPageAnimationListener implements Animation.AnimationListener {
        public void onAnimationEnd(Animation animation) {
            if (isPageOpen) {
                mainLayout.setVisibility(View.INVISIBLE);
                isPageOpen = false;
            } else {
                isPageOpen = true;
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }
}
