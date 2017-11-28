package com.example.parkminhyun.foodworldcup;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.parkminhyun.foodworldcup.Data.ListItem;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    LoginButton btnLoginFacebook;
    CallbackManager callbackManager;
    EditText editText;
    EditText editText2;
    TextView textView3;
    Button btnLogin;
    public static int chk=0;
    int flag=0;
    int num=0;
    public static String result;
    ArrayList<ListItem> listitem = new ArrayList<ListItem>();

    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.parseColor("#E37FA8"));

        /*
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
               */
        FacebookSdk.sdkInitialize(this);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.login_bar);
        setContentView(R.layout.activity_login);

        ll = (LinearLayout) findViewById(R.id.ll_loginBackground);
        ll.setAlpha(0.85f);

//        editText = (EditText)findViewById(R.id.editText);
//        editText2 = (EditText)findViewById(R.id.editText2);
//        textView3 = (TextView)findViewById(R.id.textView3);
//        btnLogin = (Button)findViewById(R.id.btnLogin);
//        callbackManager = CallbackManager.Factory.create();
//        btnLoginFacebook = (LoginButton) findViewById(R.id.btnLoginFacebook);
//
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                /*
//                //이름받기 불가능
//                String name = Profile.getCurrentProfile().getFirstName();
//                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                intent.putExtra("name",name);
//                startActivity(intent);
//                */
//            }
//            @Override
//            public void onCancel() {
//
//            }
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });
//        setBtnLoginFacebook();
//        LoginManager.getInstance().logOut();
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /*String value;
//                value=sendPostRequest(editText.getText().toString(),editText2.getText().toString());
//                Toast.makeText(getApplicationContext(),"value * : "+value,Toast.LENGTH_LONG).show();
//                if(value=="1") {
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                    finish();
//                }*/
//                sendPostRequest(editText.getText().toString(),editText2.getText().toString());
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(),"result * : "+result, Toast.LENGTH_LONG).show();
//                        if(result.equals("1")){
//                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                        }
//                    }
//                },1000);
//            }
//        });

    }
//    private void setBtnLoginFacebook(){
//        if(AccessToken.getCurrentAccessToken()!=null){
//            btnLoginFacebook.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    if(flag==1) {
//                        LoginManager.getInstance().logOut();
//                        setBtnLoginFacebook();
//                    }
//                }
//            });
//        }
//        else if(AccessToken.getCurrentAccessToken()==null){
//            btnLoginFacebook.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    if(flag==0) {
//                        if(AccessToken.getCurrentAccessToken()!=null) {
//                            LoginManager.getInstance().logInWithReadPermissions(
//                                    LoginActivity.this, Arrays.asList("public_profile"));
//                            flag = 1;
//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            startActivity(intent);
//                        }
//                    }
//                }
//            });
//        }
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }
//    private String sendPostRequest(String ID, String Password){
//        class SendPostReqAsyncTask extends AsyncTask<String,Void,String> {
//            @Override
//            protected String doInBackground(String... params)
//            {
//                String ID = params[0];
//                String Password = params[1];
//                //String result="";
//                Log.v("TAG","백그라운드 작업   ID=" + ID + " Password=" + Password);
//                StringBuilder stringBuilder = new StringBuilder();
//                HttpClient httpClient = SessionControl.getHttpClient();
//
//                HttpPost httpPost = new HttpPost("http://iove951221.dothome.co.kr/login2.php");
//                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
//                nameValuePairList.add(new BasicNameValuePair("ID",ID));
//                nameValuePairList.add(new BasicNameValuePair("Password",Password));
//                try {
//                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList,"UTF-8");
//                    httpPost.setEntity(urlEncodedFormEntity);
//
//                    try {
//                        HttpResponse httpResponse = httpClient.execute(httpPost);
//
//                        InputStream inputStream = httpResponse.getEntity().getContent();
//                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                        String bufferedStrChunk = null;
//                        while((bufferedStrChunk = bufferedReader.readLine()) != null){
//                            stringBuilder.append(bufferedStrChunk);
//                        }
//
//                        Log.v("TAG","result1:"+stringBuilder.toString());/////<==  이곳에서   서버에서  보내온 메시지를 확인해 볼 수 있다..
//                        //////         성공/실패 여부에 따라 적절히  대응하자.
//                        result=stringBuilder.toString();
//                        Log.v("TAG","result2:"+result);
//                        return result;
//                    }
//                    catch (ClientProtocolException cpe) {
//                        Log.v("TAG","First Exception caz of HttpResponese :" + cpe);
//                        cpe.printStackTrace();
//                    } catch (IOException ioe) {
//                        Log.v("TAG","Second Exception caz of HttpResponse :" + ioe);
//                        ioe.printStackTrace();
//                    }
//
//                } catch (UnsupportedEncodingException uee) {
//                    Log.v("TAG","An Exception given because of UrlEncodedFormEntity argument :" + uee);
//                    uee.printStackTrace();
//                }
//                Log.v("TAG","result3:"+result);
//                return result;
//            }
//
//            protected void onPostExecute(String res)
//            {
//                super.onPostExecute(res);
//                Log.v("TAG","POST : "+res);
//                result=res;
//                Log.v("TAG","POST : "+result);
//            }/// onPostExecute
//
//        }
//        ///////////////////////////////////
//        // 이곳에서 로그인을 위한 웹문서를 비동기 백그라운드로 요청한다.
//        SendPostReqAsyncTask sendTask = new SendPostReqAsyncTask();
//        sendTask.execute(ID, Password);     // 비동기 방식 백그라운드로 받아 오기.....
//        ///////////////////////////////////
//        return result;
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//    public void onBtnJoinClicked(View v){
//        startActivity(new Intent(this,JoinActivity.class));
//    }
//    public void onButton1Clicked(View v){
//        startActivity(new Intent(this,MemberActivity.class));
//    }

}
