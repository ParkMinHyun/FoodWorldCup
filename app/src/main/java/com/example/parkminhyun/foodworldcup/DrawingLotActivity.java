package com.example.parkminhyun.foodworldcup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkminhyun.foodworldcup.ETC.FoodInfomationVO;
import com.example.parkminhyun.foodworldcup.ETC.JsonParser;
import com.example.parkminhyun.foodworldcup.NaverAPI.NaverAsyncResponse;
import com.example.parkminhyun.foodworldcup.NaverAPI.NaverAPI_AsnycTask;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class DrawingLotActivity extends AppCompatActivity implements NaverAsyncResponse, NaverAPI_AsnycTask.NaverAsyncResponse {

    ImageView foodImage1, foodImage2, foodImage3, foodImage4, foodImage5, foodImage6;
    ImageView resultfoodDialogImage;
    EditText inputText;
    TextView inputedTextView;

    private FoodInfomationVO foodInfomationVO;
    private BiMap<String, String> foodNameMap;
    private BiMap<String, String> resultFoodImageInfoMap;

    private List<String> addedFoodNameList = new ArrayList<>();
    private int addedfoodCount = 0, randomNum = 0;

    public static final int DrawingLotActivityMode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_lot);
        initProperty();
    }

    private void initProperty() {
        foodImage1 = (ImageView) findViewById(R.id.image1);
        foodImage2 = (ImageView) findViewById(R.id.image2);
        foodImage3 = (ImageView) findViewById(R.id.image3);
        foodImage4 = (ImageView) findViewById(R.id.image4);
        foodImage5 = (ImageView) findViewById(R.id.image5);
        foodImage6 = (ImageView) findViewById(R.id.image6);

        resultfoodDialogImage = (ImageView) findViewById(R.id.resultfoodDialogImage);

        foodInfomationVO = FoodInfomationVO.getInstance();
        foodNameMap = foodInfomationVO.getReverseMap();
        resultFoodImageInfoMap = HashBiMap.create();

        inputedTextView = (TextView) findViewById(R.id.inputedTextView);
        inputText = (EditText) findViewById(R.id.editFoodText);

        RelativeLayout rootView = (RelativeLayout) findViewById(R.id.rootView);
    }

    // 추가 버튼 클릭시
    public void plusBtnClicked(View view) {
        String inputFoodName = inputText.getText().toString();


        if (inputFoodName.length() == 0) {
            Toast.makeText(getApplicationContext(), "음식을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        ImageView currentFoodImageView;
        switch (addedfoodCount) {
            case 0:
                currentFoodImageView = foodImage1;
                foodImage1.setVisibility(View.VISIBLE);
                break;
            case 1:
                currentFoodImageView = foodImage2;
                foodImage2.setVisibility(View.VISIBLE);
                break;
            case 2:
                currentFoodImageView = foodImage3;
                foodImage3.setVisibility(View.VISIBLE);
                break;
            case 3:
                currentFoodImageView = foodImage4;
                foodImage4.setVisibility(View.VISIBLE);
                break;
            case 4:
                currentFoodImageView = foodImage5;
                foodImage5.setVisibility(View.VISIBLE);
                break;
            case 5:
                currentFoodImageView = foodImage6;
                foodImage6.setVisibility(View.VISIBLE);
                break;
            default:
                Toast.makeText(getApplicationContext(), "더 이상 추가할 수 없습니다", Toast.LENGTH_SHORT).show();
                return;
        }

        if (foodNameMap.get(inputFoodName) != null) {
            int resID = getResources().getIdentifier(foodNameMap.get(inputFoodName), "drawable", getPackageName());
            currentFoodImageView.setImageResource(resID);

            // 음식 이름 추가
            addFoodName();
            addedfoodCount++;
            return;
        }

        // 네이버 검색 API 어싱크로 동작시키기
        new NaverAPI_AsnycTask(this, inputText.getText().toString(),DrawingLotActivityMode).execute();
        addedfoodCount++;
    }

    // 음식 이름추가 및 Text 초기화
    private void addFoodName() {
        addedFoodNameList.add(inputText.getText().toString());
        inputedTextView.setText(inputedTextView.getText() + " " + inputText.getText().toString());
        inputText.setText("");
    }

    // 제비뽑기 버튼 클릭 시
    public void startDrawingLot(View view) {

        // 키보드 숨기기
        hideSoftKeyboard(view);

        if (addedfoodCount == 0) {
            Toast.makeText(getApplicationContext(), "음식을 추가해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        showResultFood();
    }

    // Dialog 띄우기
    private void showResultFood() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.resultfood_dialog, null);
        ImageView resultfoodImageView = dialogLayout.findViewById(R.id.resultfoodDialogImage);

        // 랜덤으로 선택된 음식 Dialog ImageView에 뿌려주기
        Random random = new Random();
        randomNum = random.nextInt(addedfoodCount);

        // food Data Set에 있는 음식일 경우
        if (foodNameMap.get(addedFoodNameList.get(randomNum)) != null) {
            int resID = getResources().getIdentifier(foodNameMap.get(addedFoodNameList.get(randomNum)), "drawable", getPackageName());
            resultfoodImageView.setImageResource(resID);
        }
        // 아닐 경우
        else {
            // 이미지 읽어 오기
            Picasso.with(getApplicationContext())
                    .load(resultFoodImageInfoMap.get(addedFoodNameList.get(randomNum)))
                    .into(resultfoodImageView);
        }

        // Dialog 띄우기
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    // AsyncTask onPost 작업 처리
    @Override
    public void processOfNaverAsyncFinish(String response) {
        JsonParser jsonParser = JsonParser.getInstance();
        String foodThumbnail = jsonParser.receiveFoodThumbnailUsingJSON(response);

        // 이미지 읽어 오기
        Picasso.with(getApplicationContext())
                .load(foodThumbnail)
                .into((ImageView) findViewById(getResources().getIdentifier("image" + addedfoodCount, "id", getPackageName())));

        // 음식 이름 추가
        resultFoodImageInfoMap.put(inputText.getText().toString(),foodThumbnail);
        addFoodName();
    }

    public void findFoodStoreImageClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), ResultFoodMapActivity.class);
        intent.putExtra("resultFood", addedFoodNameList.get(randomNum));
        intent.putExtra("previousActivity", DrawingLotActivityMode);
        startActivity(intent);
    }

    protected void hideSoftKeyboard(View view) {
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
