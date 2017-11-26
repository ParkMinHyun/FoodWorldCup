package com.example.parkminhyun.foodworldcup.NaverAPI;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by ParkMinHyun on 2017-11-26.
 */

public class NaverAPI_AsnycTask extends AsyncTask<Void, Void, String>{

    public AsyncResponse delegate = null;

    private String inputText;
    private String foodThumbnail;

    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(String output);
    }
    
    public NaverAPI_AsnycTask(AsyncResponse delegate, String inputText){
        this.delegate = delegate;
        this.inputText = inputText;
    }

    @Override
    protected String doInBackground(Void... params) {
        Log.i("JSON", "시작");
        String clientId = "kEZOwXlvRFPihiPU8fVJ";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "wbyTPTRhuT";//애플리케이션 클라이언트 시크릿값";
        try {
            String text = URLEncoder.encode(inputText, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/image.json?query=" + text + "&display=2&start=1&sort=sim"; // json 결과
            //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return ReceiveFoodInfoUsingJSON(response.toString());

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

    // JSON Data 받기
    private String ReceiveFoodInfoUsingJSON(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());   // JSONObject 생성
            String a = jsonObject.getString("items");

            JSONArray jarray = new JSONArray(a);   // JSONArray 생성
            for (int i = 0; i < 1; i++) {
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                foodThumbnail = jObject.getString("thumbnail");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return foodThumbnail;
    }
}
