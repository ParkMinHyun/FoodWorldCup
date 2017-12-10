package com.example.parkminhyun.foodworldcup;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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
import com.example.parkminhyun.foodworldcup.ETC.SessionControl;
import com.google.common.collect.BiMap;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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

    public static String sendID;

    TextView myPageID;

    String result;
    char getResult;
    EditText PWEdit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent loginIntent = getIntent();
        final Bundle data = loginIntent.getExtras();

        menuInit();

        // 안태현
        // 안드로이드 statusBar Color 변경
        getWindow().setStatusBarColor(Color.parseColor("#E37FA8"));

        myPageID = (TextView) findViewById(R.id.txtView_nickName);
        Intent intent = getIntent();
        sendID=intent.getExtras().getString("ID");
        myPageID.setText(sendID);

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

        PWEdit = (EditText) findViewById(R.id.Edit_loginPW);

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
                String ID = data.getString("ID");
                String Password = inputPassWordEditText.getText().toString();
                sendPostRequest(ID, Password);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(getResult=='O'){
                            Toast.makeText(getApplicationContext(),"비밀번호가 정상적으로 변경되었습니다 :)", Toast.LENGTH_LONG).show();
                        }
                        else if(getResult=='X'){
                            Toast.makeText(getApplicationContext(),"비밀번호 변경 실패",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"result이상",Toast.LENGTH_LONG).show();
                        }
                    }
                }, 500);
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

        nickNameTextView.setText(data.getString("name"));

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

    // 비밀번호 변경하는 SendRequest
    private String sendPostRequest(String ID,String Password){
        class SendPostReqAsyncTask extends AsyncTask<String,Void,Character> {
            @Override
            protected Character doInBackground(String... params)
            {
                String ID = params[0];
                String Password = params[1];
                Log.v("TAG","백그라운드 작업   ID=" + ID + " Password=" + Password);
                StringBuilder stringBuilder = new StringBuilder();
                HttpClient httpClient = SessionControl.getHttpClient();

                HttpPost httpPost = new HttpPost("http://iove951221.dothome.co.kr/pwchange.php");
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(new BasicNameValuePair("ID",ID));
                nameValuePairList.add(new BasicNameValuePair("Password",Password));
                try {
                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList,"UTF-8");
                    httpPost.setEntity(urlEncodedFormEntity);

                    try {
                        HttpResponse httpResponse = httpClient.execute(httpPost);

                        InputStream inputStream = httpResponse.getEntity().getContent();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String bufferedStrChunk = null;
                        while((bufferedStrChunk = bufferedReader.readLine()) != null){
                            stringBuilder.append(bufferedStrChunk);
                        }

                        Log.v("TAG","result1:"+stringBuilder.toString());//이곳에서 서버에서 보내온 메시지를 확인해 볼 수 있다..
                        //////         성공/실패 여부에 따라 적절히  대응하자.
                        result=stringBuilder.toString();
                        Log.d("TAG","length"+result.length());
                        getResult=result.charAt(0);
                        Log.d("TAG","getResult"+getResult);
                        return getResult;
                    }
                    catch (ClientProtocolException cpe) {
                        Log.v("TAG","First Exception caz of HttpResponese :" + cpe);
                        cpe.printStackTrace();
                    } catch (IOException ioe) {
                        Log.v("TAG","Second Exception caz of HttpResponse :" + ioe);
                        ioe.printStackTrace();
                    }

                } catch (UnsupportedEncodingException uee) {
                    Log.v("TAG","An Exception given because of UrlEncodedFormEntity argument :" + uee);
                    uee.printStackTrace();
                }
                return getResult;
            }

            protected void onPostExecute(Character res)
            {
                super.onPostExecute(res);
                Log.v("TAG","POST : "+res);
            }/// onPostExecute

        }
        ///////////////////////////////////
        // 이곳에서 로그인을 위한 웹문서를 비동기 백그라운드로 요청한다.
        SendPostReqAsyncTask sendTask = new SendPostReqAsyncTask();
        sendTask.execute(ID, Password); // 비동기 방식 백그라운드로 받아 오기
        ///////////////////////////////////
        return result;
    }

}
