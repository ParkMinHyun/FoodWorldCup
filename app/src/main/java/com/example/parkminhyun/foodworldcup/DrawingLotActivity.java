package com.example.parkminhyun.foodworldcup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.parkminhyun.foodworldcup.NaverAPI.AsyncResponse;
import com.example.parkminhyun.foodworldcup.NaverAPI.NaverAPI_AsnycTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class DrawingLotActivity extends AppCompatActivity implements AsyncResponse, NaverAPI_AsnycTask.AsyncResponse {

    ImageView foodImage1,foodImage2,foodImage3,foodImage4,foodImage5,foodImage6 ;
    EditText inputText;

    private List<String> addedFoodName = new ArrayList<>();
    private int foodNum = 0;

    private static final int DrawingLotActivity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_lot);

        initProperty();
    }

    private void initProperty(){
        foodImage1 = (ImageView)findViewById(R.id.image1);
        foodImage2 = (ImageView)findViewById(R.id.image2);
        foodImage3 = (ImageView)findViewById(R.id.image3);
        foodImage4 = (ImageView)findViewById(R.id.image4);
        foodImage5 = (ImageView)findViewById(R.id.image5);
        foodImage6 = (ImageView)findViewById(R.id.image6);

        inputText = (EditText)findViewById(R.id.editFoodText);
    }

    // 추가 버튼 클릭시
    public void plusBtnClicked(View view) {
        switch (foodNum){
            case 0: foodImage1.setVisibility(View.VISIBLE); break;
            case 1: foodImage2.setVisibility(View.VISIBLE); break;
            case 2: foodImage3.setVisibility(View.VISIBLE); break;
            case 3: foodImage4.setVisibility(View.VISIBLE); break;
            case 4: foodImage5.setVisibility(View.VISIBLE); break;
            case 5: foodImage6.setVisibility(View.VISIBLE); break;
            default:
                Toast.makeText(getApplicationContext(),"더 이상 추가할 수 없습니다",Toast.LENGTH_SHORT).show(); return;
        }

        // 네이버 검색 API 어싱크로 동작시키기
        new NaverAPI_AsnycTask(this,inputText.getText().toString()).execute();
        foodNum++;
    }

    // 제비뽑기 버튼 클릭 시
    public void startDrawingLot(View view) {
        Random random = new Random();

        int randomNum = random.nextInt(foodNum);
        Intent intent = new Intent(getApplicationContext(), ResultFoodMapActivity.class);
        intent.putExtra("resultFood", addedFoodName.get(randomNum));
        intent.putExtra("previousActivity", DrawingLotActivity);
        startActivity(intent);
    }

    @Override
    public void processFinish(String foodThumbnail) {
        // 이미지 읽어 오기
        Picasso.with(getApplicationContext())
                .load(foodThumbnail)
                .into((ImageView)findViewById(getResources().getIdentifier("image"+foodNum, "id", getPackageName())));

        // 음식 이름 추가
        addedFoodName.add(inputText.getText().toString());
        inputText.setText("");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
