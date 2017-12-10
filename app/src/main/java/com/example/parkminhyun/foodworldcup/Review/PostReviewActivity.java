package com.example.parkminhyun.foodworldcup.Review;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.parkminhyun.foodworldcup.MainActivity;
import com.example.parkminhyun.foodworldcup.R;

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

class myReviewInfo
{
    String ID="";
    String Content="";
    String Star = "";
    String Storename = "";
}

public class PostReviewActivity extends AppCompatActivity {


    public static final int KEY_RESPONSE_NEWREVIEW = 0 ;
    private RatingBar ratingBar;
    private TextView txtRatingValue;
    private Button btnSubmit;
    private EditText editText;
    String num = "2.0";
    String result;
    myReviewInfo _reviewInfo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_review);

        addListenerOnRatingBar();
        addListenerOnButton();

        _reviewInfo = new myReviewInfo();

        if(getIntent() != null) {
            Intent intent = getIntent();

            Bundle data = intent.getExtras();
            _reviewInfo.Storename = data.getString("Store");
        }

    }

    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        editText = (EditText) findViewById(R.id.editText);
        //if rating is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                num = String.valueOf(rating);
            }
        });
    }

    public void addListenerOnButton() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            _reviewInfo.Content = editText.getText().toString();
            _reviewInfo.ID = MainActivity.sendID;
            _reviewInfo.Star = num;

            insertToDB(_reviewInfo.ID, _reviewInfo.Content,_reviewInfo.Star ,_reviewInfo.Storename);

            Intent intent = new Intent();
            intent.putExtra("newID",_reviewInfo.ID);
            intent.putExtra("newContent",_reviewInfo.Content);
            intent.putExtra("newStar",_reviewInfo.Star);
            setResult(KEY_RESPONSE_NEWREVIEW,intent);
            finish();
            }

        });
    }


        private String insertToDB(String ID, String Content ,String Star, String Storename){
            class PostReqAsyncTask extends AsyncTask<String,Void,String> {
                @Override
                protected String doInBackground(String... params)
                {
                    String ID = params[0];
                    String Content = params[1];
                    String Star = params[2];
                    String Storename = params[3];
                    //String result="";
                    StringBuilder stringBuilder = new StringBuilder();
                    HttpClient httpClient = SessionControl.getHttpClient();

                    HttpPost httpPost = new HttpPost("http://iove951221.dothome.co.kr/postreview2.php");
                    List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
                    nameValuePairList.add(new BasicNameValuePair("ID",ID));
                    nameValuePairList.add(new BasicNameValuePair("Content",Content));
                    nameValuePairList.add(new BasicNameValuePair("Star", Star));
                    nameValuePairList.add(new BasicNameValuePair("Storename",Storename));

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
                    } catch (IOException e) {
                        e.printStackTrace();
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
            Task.execute(ID, Content, Star, Storename);     // 비동기 방식 백그라운드로 받아 오기.....
            ///////////////////////////////////
            return result;
        }
}