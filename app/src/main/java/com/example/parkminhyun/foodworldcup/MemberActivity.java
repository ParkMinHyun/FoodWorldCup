package com.example.parkminhyun.foodworldcup;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.parkminhyun.foodworldcup.Data.ListItem;
import com.example.parkminhyun.foodworldcup.ETC.SessionControl;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MemberActivity extends AppCompatActivity {
    TextView textView2;
    ArrayList<ListItem> listitem = new ArrayList<ListItem>();
    ListItem Item;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        textView2 = (TextView)findViewById(R.id.textView2);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.setHorizontalScrollBarEnabled(true);
        infoDown();
    }
    private String infoDown(){
        class PostReqAsyncTask extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... params)
            {
                //String result="";

                StringBuilder stringBuilder = new StringBuilder();
                HttpClient httpClient = SessionControl.getHttpClient();

                HttpPost httpPost = new HttpPost("http://iove951221.dothome.co.kr/member.php");
                List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
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

            protected void onPostExecute(String str)
            {
                String ID;
                String Name;
                String Password;
                int num=0;
                String info="";
                try{
                    JSONObject root = new JSONObject(str);
                    JSONArray ja = root.getJSONArray("result");
                    for(int i=0;i<ja.length();i++){
                        JSONObject jo = ja.getJSONObject(i);
                        ID = jo.getString("ID");
                        Name = jo.getString("Name");
                        Password = jo.getString("Password");
                        listitem.add(new ListItem(ID,Name,Password));
                    }
                    num=ja.length();
                }catch(JSONException e){
                    e.printStackTrace();
                }
                for(int i=0;i<num;i++) {
                    info += "ID :" + listitem.get(i).getData(0) +
                            "\nName:" + listitem.get(i).getData(1) +
                            "\nPassword:" + listitem.get(i).getData(2)+"\n";
                }
                textView2.setText(info);
            }/// onPostExecute
        }
        ///////////////////////////////////
        // 이곳에서 로그인을 위한 웹문서를 비동기 백그라운드로 요청한다.
        PostReqAsyncTask Task = new PostReqAsyncTask();
        Task.execute();     // 비동기 방식 백그라운드로 받아 오기.....
        ///////////////////////////////////
        return result;
    }


    /*private class phpDown extends AsyncTask<String,Integer,String>{
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            try{
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if(conn!=null){
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for(;;){
                            String line = br.readLine();
                            if(line==null) break;
                            jsonHtml.append(line+"\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return jsonHtml.toString();
        }
        protected void onPostExecute(String str){
            String ID;
            String Name;
            String Password;
            int num=0;
            String info="";
            try{
                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("result");
                for(int i=0;i<ja.length();i++){
                    JSONObject jo = ja.getJSONObject(i);
                    ID = jo.getString("ID");
                    Name = jo.getString("Name");
                    Password = jo.getString("Password");
                    listitem.add(new ListItem(ID,Name,Password));
                }
                num=ja.length();
            }catch(JSONException e){
                e.printStackTrace();
            }
            for(int i=0;i<num;i++) {
                 info += "ID :" + listitem.get(i).getData(0) +
                        "\nName:" + listitem.get(i).getData(1) +
                        "\nPassword:" + listitem.get(i).getData(2)+"\n";
            }
            textView2.setText(info);
        }
    }*/

}
