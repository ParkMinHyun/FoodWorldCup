package com.example.parkminhyun.foodworldcup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MenuWorldCupActivity extends AppCompatActivity {

    View view;
    FrameLayout roots, menuLayout, resultFoodPage;
    ImageView topImageCheck, downImageCheck;
    ImageView topImage, downImage, resultFoodImageView, findFoodStoreImageViewBtn;
    TextView foodNameTextView;

    private List<String> foodTournerment_menuList = new ArrayList<>();
    private String mDrawableName1, mDrawableName2;
    private int resID1, resID2;

    private int foodIndex = 0;
    private boolean quarterfinal_flag = false, semifinal_flag = false, final_flag = false;
    public static final int MenuWorldCupActivity = 0;

    FoodInfomation foodInfomation;
    private Animation translateUpAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_world_cup);

        propertyInit();
        menuListInit();
        foodImageViewSetting();
    }

    // id 추가 및 기타 속성 초기화
    public void propertyInit() {
        roots = (FrameLayout) findViewById(R.id.root);
        view = findViewById(R.id.includeFrame);
        resultFoodPage = (FrameLayout) findViewById(R.id.resultFoodPage);
        menuLayout = (FrameLayout) findViewById(R.id.menuLayout);
        topImage = (ImageView) findViewById(R.id.topImage);
        downImage = (ImageView) findViewById(R.id.downImage);
        resultFoodImageView = (ImageView) findViewById(R.id.resultFoodImageView);
        findFoodStoreImageViewBtn = (ImageView) findViewById(R.id.findFoodStoreImageViewBtn);
        foodNameTextView = (TextView) findViewById(R.id.foodNameTextView);

        topImageCheck = (ImageView) findViewById(R.id.topImageCheck);
        downImageCheck = (ImageView) findViewById(R.id.downImageCheck);

        foodInfomation = FoodInfomation.getInstance();

        // 애니메이션객체로딩
        translateUpAnim = AnimationUtils.loadAnimation(this, R.anim.translate_up);

        // 애니메이션객체에리스너설정
        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
        translateUpAnim.setAnimationListener(animListener);
    }

    public void foodImageViewSetting() {

        // 16강, 8강, 4강 진행
        if (foodIndex == 8 && quarterfinal_flag == false) {
            foodIndex = 0;
            quarterfinal_flag = true;
            showToast("8강!");
        } else if (foodIndex == 4 && quarterfinal_flag == true) {
            foodIndex = 0;
            semifinal_flag = true;
            showToast("4강!");
        } else if (foodIndex == 2 && semifinal_flag == true) {
            foodIndex = 0;
            final_flag = true;
            showToast("결승전!");
        }

        // 문자열로 drawable에 있는 음식 이미지 가져오기
        mDrawableName1 = foodTournerment_menuList.get(foodIndex++).toString();
        resID1 = getResources().getIdentifier(mDrawableName1, "drawable", getPackageName());
        topImage.setImageResource(resID1);

        mDrawableName2 = foodTournerment_menuList.get(foodIndex++).toString();
        resID2 = getResources().getIdentifier(mDrawableName2, "drawable", getPackageName());
        downImage.setImageResource(resID2);

        // 이미지 클릭 활성화 및 투명도 초기화
        topImage.setAlpha(1f);
        downImage.setAlpha(1f);
    }

    // Food 토너먼트 중 음식 이미지를 클릭했을 경우
    private void FoodImageClick_Result(String mode) {

        // Click Event 비활성화
        topImage.setClickable(false);
        downImage.setClickable(false);

        // 투명도 설정 + String 배열 삭제
        if (mode.equals("topImageClick")) {
            downImage.setAlpha(0.3f);
            topImageCheck.bringToFront();
            topImageCheck.setVisibility(view.VISIBLE);
            foodTournerment_menuList.remove((foodIndex--) - 1);

        } else {
            topImage.setAlpha(0.3f);
            downImageCheck.bringToFront();
            downImageCheck.setVisibility(view.VISIBLE);
            foodTournerment_menuList.remove((foodIndex--) - 2);
        }
    }

    // 윗 이미지 클릭시
    public void topImageClick(View v) throws InterruptedException {
        FoodImageClick_Result("topImageClick");

        // 1초 뒤 Handler 실행
        Handler myHandler = new Handler();
        myHandler.postDelayed(mMyRunnable1, 1000);


    }

    // 아래 이미지 클릭시
    public void downImageClick(View v) {
        FoodImageClick_Result("downImageClick");

        // 1초 뒤 Handler 실행
        Handler myHandler = new Handler();
        myHandler.postDelayed(mMyRunnable1, 1000);
    }

    public void findFoodStoreImageClicked(View v) {
        Intent intent = new Intent(getApplicationContext(), ResultFoodMapActivity.class);
        intent.putExtra("resultFood", foodTournerment_menuList.get(0));
        intent.putExtra("previousActivity", MenuWorldCupActivity);
        startActivity(intent);
    }

    // left Move 애니메이션 실행
    private Runnable mMyRunnable1 = new Runnable() {
        @Override
        public void run() {

            // left Move animation 실행
            menuLayout.startAnimation(new Animate());

            // check 이미지 Invisible
            topImageCheck.setVisibility(view.INVISIBLE);
            downImageCheck.setVisibility(view.INVISIBLE);

            // 0.1초 뒤 Handler 실행
            Handler Handler = new Handler();
            Handler.postDelayed(mMyRunnable2, 100);
        }
    };

    // view 삭제 실행
    private Runnable mMyRunnable2 = new Runnable() {
        @Override
        public void run() {
            roots.removeView(view);

            // 결승전 진행할 때 결과창 Activity 띄우기
            if (final_flag) {
                resultFoodPage.setVisibility(View.VISIBLE);
                resultFoodImageView.setImageResource(getResources()
                        .getIdentifier(foodTournerment_menuList.get(0).toString(), "drawable", getPackageName()));
                foodNameTextView.setText(foodInfomation.map.get(foodTournerment_menuList.get(0)));
            } else {
                // 0.1초 뒤 Handler 실행
                Handler Handler = new Handler();
                Handler.postDelayed(mMyRunnable3, 100);
            }
        }
    };

    // view 추가 및 fade in 애니메이션 실행
    private Runnable mMyRunnable3 = new Runnable() {
        @Override
        public void run() {

            // view 다시 추가 한 뒤 ImageView 채우기
            roots.addView(view);
            foodImageViewSetting();

            // 서서히 나타나는 애니메이션 적용
            Animation fade_in_animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            view.startAnimation(fade_in_animation);

            // topImage, downImage 다시 클릭 이벤트 활성화
            downImage.setClickable(true);
            topImage.setClickable(true);
        }
    };

    public void menuListInit() {
        foodTournerment_menuList.add("baekban");
        foodTournerment_menuList.add("bibimbab");
        foodTournerment_menuList.add("bread");
        foodTournerment_menuList.add("cake");
        foodTournerment_menuList.add("chicken");
        foodTournerment_menuList.add("dduckppoki");
        foodTournerment_menuList.add("donggas");
        foodTournerment_menuList.add("doughnut");
        foodTournerment_menuList.add("gimbab");
        foodTournerment_menuList.add("hamburger");
        foodTournerment_menuList.add("ramen");
        foodTournerment_menuList.add("sandwich");
        foodTournerment_menuList.add("spagetti");
        foodTournerment_menuList.add("steak");
        foodTournerment_menuList.add("sushi");
        foodTournerment_menuList.add("zazang");
    }

    // 애니메이션 리스너 정의
    private class SlidingPageAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        // 애니메이션이 끝날때 호출되는 메소드
        public void onAnimationEnd(Animation animation) {
        }
    }

    private void showToast(String showText){

        LayoutInflater inflater = getLayoutInflater();// 레이아웃인플레이터객체참조
        View layout = inflater.inflate(// 토스트를위한레이아웃인플레이터
                R.layout.toastborder,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.text);
        Toast toast = new Toast(this);// 토스트객체생성
        text.setText(showText);
        text.setGravity(View.TEXT_ALIGNMENT_CENTER);
        text.setTextColor(Color.WHITE);
        toast.setGravity(Gravity.CENTER, 0, -100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);// 토스트가보이는뷰설정
        toast.show();
    }
}
