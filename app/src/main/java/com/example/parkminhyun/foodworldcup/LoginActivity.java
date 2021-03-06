package com.example.parkminhyun.foodworldcup;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.parkminhyun.foodworldcup.Data.ListItem;
import com.example.parkminhyun.foodworldcup.ETC.SessionControl;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

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
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    public static int chk=0;
    int flag=0;
    int num=0;
    String result;
    ArrayList<ListItem> listitem = new ArrayList<ListItem>();

    VideoView videoView;

    Button loginStartButton;
    Button loginButton;
    Button joinButton;

    LoginButton facebookLoginButton;

    boolean isLoginPageOpen = false;
    boolean isStartButtonClicked = false;
    boolean isJoinPageOpen = false;
    boolean isJoinButtonClicked = false;

    LinearLayout loginLayout;
    LinearLayout joinLayout;

    Animation translateUpAnimation;
    Animation translateDownAnimation;

    TextView joinTextView;

    EditText loginIDEdit;
    EditText loginPWEdit;
    EditText joinIDEdit;
    EditText joinNameEdit;
    EditText joinPWEdit;
    EditText joinPWCHKEdit;

    CallbackManager callbackManager;

    String userName,
           userID,
           userPassword,
           userPasswordCheck,
           joinResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 안태현
        // 안드로이드 statusBar Color 변경
        getWindow().setStatusBarColor(Color.parseColor("#E37FA8"));

        loginLayout = (LinearLayout) findViewById(R.id.ll_loginLayout);
        joinLayout = (LinearLayout) findViewById(R.id.ll_joinLayout);

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.background_video);

        // VideoView 설정
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        videoView = (MyVideoView) findViewById(R.id.vv_loginBackground);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });

        // VideoView에서 기본적으로 재생/일시정지를 감추기 위한
        // MediaController 을 null 로 지정한다
        videoView.setVideoURI(uri);
        videoView.setMediaController(null);
        videoView.start();

        // 애니메이션 객체 추가
        translateUpAnimation = AnimationUtils.loadAnimation(this, R.anim.translate_up);
        translateDownAnimation = AnimationUtils.loadAnimation(this, R.anim.translate_down);

        // 슬라이딩 애니메이션에 리스너 추가
        SlidingAnimationListener animListener = new SlidingAnimationListener();
        translateUpAnimation.setAnimationListener(animListener);
        translateDownAnimation.setAnimationListener(animListener);

        loginStartButton = (Button) findViewById(R.id.btn_loginStart);
        loginStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStartButtonClicked = true;
                if (isLoginPageOpen) {
                    loginLayout.startAnimation(translateDownAnimation);
                } else {
                    loginLayout.setVisibility(View.VISIBLE);
                    loginLayout.startAnimation(translateUpAnimation);
                }
            }
        });

        FacebookSdk.sdkInitialize(this);

        // 각 버튼에 클릭 이벤트 추가
        loginIDEdit = (EditText) findViewById(R.id.edit_lgoinID);
        loginPWEdit = (EditText) findViewById(R.id.Edit_loginPW);
        loginButton = (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPostRequest(loginIDEdit.getText().toString(), loginPWEdit.getText().toString());
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("1")) {
                            Toast.makeText(LoginActivity.this, "로그인에 성공하였습니다 :)", Toast.LENGTH_SHORT).show();
                            Toast.makeText(LoginActivity.this, joinNameEdit.getText().toString(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("name", joinNameEdit.getText().toString());
                            intent.putExtra("ID", loginIDEdit.getText().toString());
                            intent.putExtra("password", loginPWEdit.getText().toString());
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "로그인에 실패하였습니다 :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 1000);
            }
        });

        callbackManager = CallbackManager.Factory.create();
        joinTextView = (TextView) findViewById(R.id.text_join);
        joinTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isJoinButtonClicked = true;
                if (isJoinPageOpen) {
                    joinLayout.startAnimation(translateDownAnimation);
                } else {
                    joinLayout.setVisibility(View.VISIBLE);
                    joinLayout.startAnimation(translateUpAnimation);
                }
            }
        });

        facebookLoginButton = (LoginButton) findViewById(R.id.btn_loginFaceBook);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
            @Override
            public void onCancel() {

            }
            @Override
            public void onError(FacebookException error) {

            }
        });
        setBtnLoginFacebook();
        LoginManager.getInstance().logOut();

        joinNameEdit = (EditText) findViewById(R.id.edit_joinName);
        joinIDEdit = (EditText) findViewById(R.id.edit_joinID);
        joinPWEdit = (EditText) findViewById(R.id.edit_joinPW);
        joinPWCHKEdit = (EditText) findViewById(R.id.edit_joinPWCHK);

        // 회원가입 버튼 이벤트 추가
        joinButton = (Button) findViewById(R.id.btn_JOIN);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userName = joinNameEdit.getText().toString();
                userID = joinIDEdit.getText().toString();
                userPassword = joinPWEdit.getText().toString();
                userPasswordCheck = joinPWCHKEdit.getText().toString();

                if(userPasswordCheck.equals(userPassword)) {
                    insertToDB(userID, userName, userPassword);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "회원가입에 성공하였습니다 :)", Toast.LENGTH_SHORT).show();
                            isJoinPageOpen = false;
                            joinLayout.setVisibility(View.GONE);
                        }
                    }, 2000);
                }
                else{
                    Toast.makeText(getApplicationContext(),"비밀번호 확인 해주십시오.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // 회원가입을 할 경우 DB에 해당 정보를 insert
    private String insertToDB(String ID, String Name, String Password){
        class PostReqAsyncTask extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... params)
            {
                String ID = params[0];
                String Name = params[1];
                String Password = params[2];
                //String result="";
                Log.v("TAG","백그라운드 작업   ID=" + ID +"Name="+Name+" Password=" + Password);
                StringBuilder stringBuilder = new StringBuilder();
                HttpClient httpClient = SessionControl.getHttpClient();

                HttpPost httpPost = new HttpPost("http://iove951221.dothome.co.kr/join3.php");
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(new BasicNameValuePair("ID",ID));
                nameValuePairList.add(new BasicNameValuePair("Name",Name));
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

                        Log.v("TAG","result1:"+stringBuilder.toString());/////<==  이곳에서   서버에서  보내온 메시지를 확인해 볼 수 있다..
                        //////         성공/실패 여부에 따라 적절히  대응하자.
                        result=stringBuilder.toString();
                        Log.v("TAG","result2:"+result);
                        return result;
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
                Log.v("TAG","result3:"+result);
                return result;
            }

            protected void onPostExecute(String res)
            {
                super.onPostExecute(res);
                Log.v("TAG","POST : "+res);
                result=res;
                Log.v("TAG","POST : "+result);
            }/// onPostExecute

        }
        ///////////////////////////////////
        // 이곳에서 로그인을 위한 웹문서를 비동기 백그라운드로 요청한다.
        PostReqAsyncTask Task = new PostReqAsyncTask();
        Task.execute(ID, Name, Password);     // 비동기 방식 백그라운드로 받아 오기.....
        ///////////////////////////////////
        return result;
    }

    // 슬라이딩 애니메이션 리스너 - 안태현
    private class SlidingAnimationListener implements Animation.AnimationListener {
        public void onAnimationEnd(Animation animation) {

            if (isLoginPageOpen && isStartButtonClicked) {
                loginLayout.setVisibility(View.INVISIBLE);
                isLoginPageOpen = false;
            } else if (isStartButtonClicked) {
                isLoginPageOpen = true;
            }

            if (isJoinPageOpen && isJoinButtonClicked) {
                joinLayout.setVisibility(View.INVISIBLE);
                isJoinPageOpen = false;
            } else if (isJoinButtonClicked) {
                isJoinPageOpen = true;
            }

            isStartButtonClicked = false;
            isJoinButtonClicked = false;
        }

        public void onAnimationRepeat(Animation animation) {}
        public void onAnimationStart(Animation animation) {}
    }

    private void setBtnLoginFacebook() {
        if(AccessToken.getCurrentAccessToken()!=null){
            facebookLoginButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(flag==1) {
                        LoginManager.getInstance().logOut();
                        setBtnLoginFacebook();
                    }
                }
            });
        }
        else if(AccessToken.getCurrentAccessToken()==null){
            facebookLoginButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(flag==0) {
                        if(AccessToken.getCurrentAccessToken()!=null) {
                            LoginManager.getInstance().logInWithReadPermissions(
                                    LoginActivity.this, Arrays.asList("public_profile"));
                            flag = 1;
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // 패스워드를 변경하기 위한 메소드
    // 서버와 HTTP 통신을 통해 비밀번호를 변경한다
    private String sendPostRequest(String ID, String Password){
        class SendPostReqAsyncTask extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... params)
            {
                String ID = params[0];
                String Password = params[1];
                //String result="";
                Log.v("TAG","백그라운드 작업   ID=" + ID + " Password=" + Password);
                StringBuilder stringBuilder = new StringBuilder();
                HttpClient httpClient = SessionControl.getHttpClient();

                HttpPost httpPost = new HttpPost("http://iove951221.dothome.co.kr/login2.php");
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

                        Log.v("TAG","result1:"+stringBuilder.toString());/////<==  이곳에서   서버에서  보내온 메시지를 확인해 볼 수 있다..
                        //////         성공/실패 여부에 따라 적절히  대응하자.
                        result=stringBuilder.toString();
                        Log.v("TAG","result2:"+result);
                        return result;
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
                Log.v("TAG","result3:"+result);
                return result;
            }

            protected void onPostExecute(String res)
            {
                super.onPostExecute(res);
                Log.v("TAG","POST : "+res);
                result=res;
                Log.v("TAG","POST : "+result);
            }/// onPostExecute

        }
        ///////////////////////////////////
        // 이곳에서 로그인을 위한 웹문서를 비동기 백그라운드로 요청한다.
        SendPostReqAsyncTask sendTask = new SendPostReqAsyncTask();
        sendTask.execute(ID, Password);     // 비동기 방식 백그라운드로 받아 오기.....
        ///////////////////////////////////
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void onBtnJoinClicked(View v) { }
    public void onButton1Clicked(View v){
        startActivity(new Intent(this,MemberActivity.class));
    }

}
