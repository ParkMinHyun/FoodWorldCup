package com.example.parkminhyun.foodworldcup;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkminhyun.foodworldcup.Data.FoodInfomationVO;
import com.google.common.collect.BiMap;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    boolean isPageOpen = false;
    private int randomNum;
    public static final int DrawingLotActivityMode = 1;

    private FoodInfomationVO foodInfomationVO;
    private BiMap<String, String> foodNameMap;
    private List<String> addedFoodNameList = new ArrayList<>();

    Animation translateLeftAnim;
    Animation translateRightAnim;

    LinearLayout mainLayout;

    ImageButton myPageButton;

    ImageView myPageimageView;

    LinearLayout foodWorldCupLayout;

    AlertDialog.Builder changeNickNameAlertDialog;
    AlertDialog.Builder changePassWordAlertDialog;

    TextView nickNameTextView;
    TextView nickNameChangeTextView;
    TextView passWordChangeTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuInit();

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

        nickNameTextView = (TextView) findViewById(R.id.txtView_nickName);

        // 닉네임을 변경하는 AlertDialog
        changeNickNameAlertDialog = new AlertDialog.Builder(MainActivity.this);
        changeNickNameAlertDialog.setTitle("닉네임 변경");
        changeNickNameAlertDialog.setMessage("변경할 닉네임을 입력해주세요 :)");

        final EditText inputChangeEditText = new EditText(MainActivity.this);
        changeNickNameAlertDialog.setView(inputChangeEditText);

        changeNickNameAlertDialog.setPositiveButton("변경", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                nickNameTextView.setText(inputChangeEditText.getText());
            }
        });

        changeNickNameAlertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        nickNameChangeTextView = (TextView) findViewById(R.id.txtView_changeNickName);
        nickNameChangeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeNickNameAlertDialog.show();
            }
        });

        // 비밀번호를 변경하는 Dialog Alert Custom (EditText 추가)
        changePassWordAlertDialog = new AlertDialog.Builder(MainActivity.this);
        changePassWordAlertDialog.setTitle("비밀번호 변경");
        changePassWordAlertDialog.setMessage("변경할 비밀번호를 입력해주세요 :)");
        final EditText inputPassWordEditText = new EditText(MainActivity.this);
        inputPassWordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        changePassWordAlertDialog.setView(inputPassWordEditText);

        changePassWordAlertDialog.setPositiveButton("변경", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "비밀번호가 정상적으로 변경되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

        changePassWordAlertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        passWordChangeTextView = (TextView) findViewById(R.id.txtView_changePW);
        passWordChangeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassWordAlertDialog.show();
            }
        });

    } // onCreate

    private void menuInit() {
        foodInfomationVO = new FoodInfomationVO();
        foodNameMap = foodInfomationVO.getMap();
        addedFoodNameList = foodInfomationVO.getFood_menuList();
    }

    // Dialog 띄우기
    public void todayMenuClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.resultfood_dialog, null);
        ImageView resultfoodImageView = dialogLayout.findViewById(R.id.resultfoodDialogImage);

        // 랜덤으로 선택된 음식 Dialog ImageView에 뿌려주기
        Random random = new Random();
        randomNum = random.nextInt(16);

        // food Data Set에 있는 음식일 경우
        int resID = getResources().getIdentifier(addedFoodNameList.get(randomNum), "drawable", getPackageName());
        resultfoodImageView.setImageResource(resID);

        // Dialog 띄우기
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

    }

    public void drawinglotBtnClick(View view) {
        startActivity(new Intent(getApplicationContext(), DrawingLotActivity.class));
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


    public void findFoodStoreImageClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), ResultFoodMapActivity.class);
        intent.putExtra("resultFood", foodNameMap.get(addedFoodNameList.get(randomNum)));
        intent.putExtra("previousActivity", DrawingLotActivityMode);
        startActivity(intent);
    }

}
