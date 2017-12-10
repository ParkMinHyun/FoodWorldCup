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

/*
페이스북으로 로그인을 해도 리뷰를 남기고 저장할 수 있게하기 위해 페이스북 로그인 시 받은 메일을 아이디로, 이름을 이름으로,
고유값을 비밀번호로 설정하여 자체 디비에 저장하는 액티비티
 */
public class FBJoinActivity extends AppCompatActivity {
    String result="";
    char getResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String Name,ID,Password;
        Intent intent = getIntent();
        Name=intent.getExtras().getString("Name");
        ID=intent.getExtras().getString("ID");
        Password=intent.getExtras().getString("Password");
        Log.v("TAG","인텐트값   ID=" + ID +"Name="+Name+" Password=" + Password);
        if(ID.equals(null)){
            insertToDB(Password,Name,Password);
        }
        else {
            insertToDB(ID, Name, Password);
        }
        try{Thread.sleep(500);}
        catch(Exception e){
            e.printStackTrace();
        }
        Log.v("TAG","getResult : "+getResult);
        Log.v("TAG","result : "+result);
        if(getResult=='O'){
            Toast.makeText(getApplicationContext(),"아이디 저장 성공",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"이미 저장된 아이디",Toast.LENGTH_LONG).show();
        }
        // 메인액티비티로 화면전환시 ID값을 넘겨주어 마이페이지에서 정보 확인 가능
        Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);
        intent2.putExtra("ID",ID);
        startActivity(intent2);
    }

    // DB에 페이스북 로그인 정보를 저장
    public String insertToDB(String ID, String Name, String Password){
        class PostReqAsyncTask extends AsyncTask<String,Void,Character>{
            @Override
            protected Character doInBackground(String... params)
            {
                String ID = params[0];
                String Name = params[1];
                String Password = params[2];
                Log.v("TAG","백그라운드 작업   ID=" + ID +"Name="+Name+" Password=" + Password);
                StringBuilder stringBuilder = new StringBuilder();
                HttpClient httpClient = SessionControl.getHttpClient();
                HttpPost httpPost = new HttpPost("http://iove951221.dothome.co.kr/join3.php");//서버주소
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                nameValuePairList.add(new BasicNameValuePair("ID",ID)); //포스트로 보내는 변수
                nameValuePairList.add(new BasicNameValuePair("Name",Name)); // 포스트로 보내는 변수
                nameValuePairList.add(new BasicNameValuePair("Password",Password)); //포스트로 보내는 변수
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

                        Log.v("TAG","result1:"+stringBuilder.toString()); // 서버에서 보내온 메시지를 확인해 볼 수 있다..
                        result=stringBuilder.toString();
                        getResult=result.charAt(1);
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
            @Override
            protected void onPostExecute(Character res)
            {
                super.onPostExecute(res);
                Log.v("TAG","POST : "+res);
            }/// onPostExecute

        }
        ///////////////////////////////////
        // 로그인을 위한 웹문서를 비동기 백그라운드로 요청한다.
        PostReqAsyncTask Task = new PostReqAsyncTask();
        Task.execute(ID, Name, Password);   // 비동기 방식 백그라운드로 받아 오기
        ///////////////////////////////////

        return result;
    }
}
