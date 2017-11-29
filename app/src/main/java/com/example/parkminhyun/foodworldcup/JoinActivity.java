package com.example.parkminhyun.foodworldcup;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parkminhyun.foodworldcup.ETC.SessionControl;

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

public class JoinActivity extends AppCompatActivity {
    EditText nameText;
    EditText idText;
    EditText pwText;
    EditText pwText2;
    Button idCheck;
    Button btnJoin;

    String user_Name,user_ID,user_Password,PWcheck;
    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        nameText = (EditText) findViewById(R.id.nameText);
        idText = (EditText) findViewById(R.id.idText);
        pwText = (EditText) findViewById(R.id.pwText);
        pwText2 = (EditText) findViewById(R.id.pwText2);
        idCheck = (Button) findViewById(R.id.idCheck);
        btnJoin = (Button) findViewById(R.id.btnJoin);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_Name=nameText.getText().toString();
                user_ID=idText.getText().toString();
                user_Password=pwText.getText().toString();
                PWcheck = pwText2.getText().toString();
                Toast.makeText(getApplicationContext(),"ID : "+user_ID+"Name : "+user_Name+"PW : "+user_Password, Toast.LENGTH_LONG).show();
                if(PWcheck.equals(user_Password)) {
                    insertToDB(user_ID,user_Name,user_Password);
                    //insertToDB(idText.getText().toString(), nameText.getText().toString(), pwText.getText().toString());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "result * : " + result, Toast.LENGTH_LONG).show();
                            if (result.equals("1")) {
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                        }
                    }, 2000);
                }
                else{
                    Toast.makeText(getApplicationContext(),"비밀번호 확인 해주십시오.", Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(getApplicationContext(),"ID : "+ID+"Name : "+Name+"PW : "+Password,Toast.LENGTH_LONG).show();
            }
        });
    }
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

                HttpPost httpPost = new HttpPost("http://iove951221.dothome.co.kr/join2.php");
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
    public void onBtnCheckClicked(View v){

    }
}