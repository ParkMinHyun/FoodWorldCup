package com.example.parkminhyun.foodworldcup.NaverAPI;

import android.os.AsyncTask;
import android.util.Log;
import com.example.parkminhyun.foodworldcup.DrawingLotActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by ParkMinHyun on 2017-11-26.
 */

public class NaverAPI_AsnycTask extends AsyncTask<Void, Void, String> {

    public NaverAsyncResponse delegate = null;

    private String inputText;
    private int mode;

    public interface NaverAsyncResponse {
        void processOfNaverAsyncFinish(String response);
    }

    public NaverAPI_AsnycTask(NaverAsyncResponse delegate, String inputText, int mode) {
        this.delegate = delegate;
        this.inputText = inputText;
        this.mode = mode;
    }

    @Override
    protected String doInBackground(Void... params) {
        Log.i("JSON", "시작");
        String clientId = "kEZOwXlvRFPihiPU8fVJ";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "wbyTPTRhuT";//애플리케이션 클라이언트 시크릿값";
        try {
            String text = URLEncoder.encode(inputText, "UTF-8");

            // Activity 별 api 모드 변경
            String apiURL = (mode == DrawingLotActivity.DrawingLotActivityMode)
                    ? "https://openapi.naver.com/v1/search/image.json?query=" + text + "&display=1&filter=large"
                    : "https://openapi.naver.com/v1/search/local.json?query=" + text; // json 결과

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

            return response.toString();

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processOfNaverAsyncFinish(result);
    }

}
