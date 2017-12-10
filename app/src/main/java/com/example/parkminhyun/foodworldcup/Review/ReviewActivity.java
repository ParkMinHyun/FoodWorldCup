package com.example.parkminhyun.foodworldcup.Review;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.parkminhyun.foodworldcup.ETC.SessionControl;
import com.example.parkminhyun.foodworldcup.R;

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


public class ReviewActivity extends AppCompatActivity {

    public static final int KEY_REQUEST_NEWREVIEW = 0 ;
    public String m_storename = "";
    String result ="";
    public int datalen =0;
    ListViewAdapter adapter;
    ListView listview ;
    ArrayList<ListItem> listitem = new ArrayList<ListItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        if(getIntent() != null) {
            Intent intent = getIntent();

            Bundle data = intent.getExtras();
            m_storename = data.getString("Store");
            Toast.makeText(this, "not null getIntent", Toast.LENGTH_SHORT).show();
        }

        // 리스트뷰 참조 및 Adapter달기

        listview = (ListView) findViewById(R.id.listView);
        adapter = new ListViewAdapter() ;
        infoDown();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == KEY_REQUEST_NEWREVIEW)
        {
            if(data != null)
            {
                adapter.addItem(this, data.getStringExtra("newID"),data.getStringExtra("newStar"),
                        data.getStringExtra("newContent")) ;
                listview.setAdapter(adapter);
            }
        }
    }


    public void onButton1Click(View view)
    {
        Intent intent = new Intent(this, PostReviewActivity.class);
        intent.putExtra("Store",m_storename);
        startActivityForResult(intent,KEY_REQUEST_NEWREVIEW);
    }


    private String infoDown(){
        class PostReqAsyncTask extends AsyncTask<String,Void,String> {
            @Override
            protected String doInBackground(String... params)
            {

                // 서버 연결
                StringBuilder stringBuilder = new StringBuilder();
                HttpClient httpClient = SessionControl.getHttpClient();

                HttpPost httpPost = new HttpPost("http://iove951221.dothome.co.kr/review.php");
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

                // 데이터 가져오는 곳
                String ID;
                String Content;
                String Star;
                String Storename;
                datalen =0;
                String info="";
                try{
                    JSONObject root = new JSONObject(str);
                    JSONArray ja = root.getJSONArray("result");
                    for(int i=0;i<ja.length();i++){
                        JSONObject jo = ja.getJSONObject(i);
                        ID = jo.getString("ID");
                        Content = jo.getString("Content");
                        Star = jo.getString("Star");
                        Storename = jo.getString("Storename");
                        //리스트에 넣고
                        listitem.add(new ListItem(ID,Content,Star,Storename));
                    }
                    datalen=ja.length();
                }catch(JSONException e){
                    e.printStackTrace();
                }

                int cnt =0;
                for(int i =0;i<datalen;i++){
                    //리스트에 있는 매장 명과 현재 가지고 있는 매장명이 같을때 뿌려주기
                    if(m_storename.equals(listitem.get(i).getData(3).toString().trim())) {

                        adapter.addItem(getApplicationContext(), listitem.get(i).getData(0), listitem.get(i).getData(2),
                                listitem.get(i).getData(1));

                        listview.setAdapter(adapter);
                        cnt++;
                   }
                }

                //리뷰가 없을 경우 Toast
                if(cnt == 0) {
                    Toast.makeText(getApplicationContext(),"현재 리뷰가 없습니다.",
                            Toast.LENGTH_SHORT).show();
                }

            }/// onPostExecute
        }
        ///////////////////////////////////
        // 이곳에서 로그인을 위한 웹문서를 비동기 백그라운드로 요청한다.
        PostReqAsyncTask Task = new PostReqAsyncTask();
        Task.execute();     // 비동기 방식 백그라운드로 받아 오기.....
        ///////////////////////////////////
        return result;
    }


}
