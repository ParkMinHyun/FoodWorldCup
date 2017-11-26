package com.example.parkminhyun.foodworldcup;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResultFoodMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GPSInfo gpsInfo;
    private FoodInfomation foodInfomation;
    public HashMap<String, String> map;

    private GoogleMap gMap;
    private EditText searchEditText;

    private int previousActivity;
    private double latitude;
    private double longitude;
    LatLng currentPos;
    private Address currentDong;

    private String searchText;
    private String cityName;
    private String resultFoodName;

    GeoPoint convertedGeoPoint;
    private List<String> foodStoreName;
    private List<String> foodStoreAddr;
    private List<String> foodStoreMapX;
    private List<String> foodStoreMapY;

    private List<Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_food_map);

        foodInfomation = FoodInfomation.getInstance();
        map = foodInfomation.getMap();
        searchEditText = (EditText) findViewById(R.id.search);

        resultFoodName = getIntent().getExtras().getString("resultFood");
        previousActivity = getIntent().getExtras().getInt("previousActivity");

        markers = new ArrayList<Marker>();
        foodStoreName = new ArrayList<>();
        foodStoreAddr = new ArrayList<>();
        foodStoreMapX = new ArrayList<String>();
        foodStoreMapY = new ArrayList<String>();

        gpsInfo = new GPSInfo(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        // 현재 위치 이동
        currentPos = new LatLng(gpsInfo.getLatitude(), gpsInfo.getLongitude());

        addCurrentPosionMarker(currentPos);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 15));

        latitude = gpsInfo.getLatitude();
        longitude = gpsInfo.getLongitude();

        Geocoder gCoder = new Geocoder(getApplicationContext());
        List<Address> addr = null;
        try {
            addr = gCoder.getFromLocation(latitude, longitude, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (addr.get(0).getSubLocality() == null || addr.get(0).getThoroughfare() == null)
            currentDong = addr.get(1);
        else
            currentDong = addr.get(0);

        // 검색 문구 생성
//        searchText = currentDong.getThoroughfare() + ' ' + "백반";
//        cityName = currentDong.getThoroughfare();

//        searchText = currentDong.getSubLocality() + ' ' + currentDong.getThoroughfare() + ' ' + "백반";
        if(currentDong.getSubLocality() == null)
            cityName = currentDong.getThoroughfare();
        else
            cityName = currentDong.getSubLocality() + ' ' + currentDong.getThoroughfare();


        searchText = (previousActivity==MenuWorldCupActivity.MenuWorldCupActivity) ? cityName + ' ' + map.get(resultFoodName) : cityName + ' ' + resultFoodName;

        // 네이버 검색 API 어싱크로 동작시키기
        ResultFoodMapActivity.JsoupAsyncTask jsoupAsyncTask = new ResultFoodMapActivity.JsoupAsyncTask();
        jsoupAsyncTask.execute();

//        a.getAdminArea()+" "+a.getLocality()+" "+a.getThoroughfare();
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // 네이버 지도 검색 API 이용하는 함수
        @Override
        protected Void doInBackground(Void... params) {
            Log.i("JSON", "시작");
            String clientId = "kEZOwXlvRFPihiPU8fVJ";//애플리케이션 클라이언트 아이디값";
            String clientSecret = "wbyTPTRhuT";//애플리케이션 클라이언트 시크릿값";
            try {
                String text = URLEncoder.encode(searchText, "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/search/local.json?query=" + text; // json 결과
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

                // JSON 파싱 변환하기
                ReceiveFoodInfoUsingJSON(response.toString());

            } catch (Exception e) {
                System.out.println(e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Marker 생성
            for (int i = 0; i < foodStoreName.size(); i++) {

                GeoPoint geoPoint = new GeoPoint(Double.parseDouble(foodStoreMapX.get(i)), Double.parseDouble(foodStoreMapY.get(i)));
                convertedGeoPoint = GeoTrans.convert(1, 0, geoPoint);

                // marker 생성 완료
                markers.add(gMap.addMarker(new MarkerOptions().position(new LatLng(convertedGeoPoint.y, convertedGeoPoint.x))
                        .title(foodStoreName.get(i))
                        .snippet(foodStoreAddr.get(i))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))));
//                        .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("store_pin",100,120)))));
            }
        }
    }

    // JSON Data 받기
    private void ReceiveFoodInfoUsingJSON(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());   // JSONObject 생성
            String a = jsonObject.getString("items");

            JSONArray jarray = new JSONArray(a);   // JSONArray 생성
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출

                String reviseFoodstoreName = jObject.getString("title").replace("<b>"," ");
                foodStoreName.add(reviseFoodstoreName.replace("</b>",""));
                foodStoreAddr.add(jObject.getString("address"));
                foodStoreMapX.add(jObject.getString("mapx"));
                foodStoreMapY.add(jObject.getString("mapy"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void findAnotherFoodStoreImageClicked(View v) {

        // 키보드 내리기
        hideSoftKeyboard(v);

        // 검색하기
        if (searchEditText.getText().length() != 0)
            searchText = cityName + ' ' +  searchEditText.getText();

        gMap.clear();
        markers.clear();
        foodStoreName.clear();
        foodStoreAddr.clear();
        foodStoreMapX.clear();
        foodStoreMapY.clear();

        addCurrentPosionMarker(currentPos);

        // 네이버 검색 API 어싱크로 동작시키기
        ResultFoodMapActivity.JsoupAsyncTask jsoupAsyncTask = new ResultFoodMapActivity.JsoupAsyncTask();
        jsoupAsyncTask.execute();

    }

    private void addCurrentPosionMarker(LatLng currentPos){

        gMap.addMarker(new MarkerOptions().position(currentPos)
                .title("현재 위치")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }


    protected void hideSoftKeyboard(View view) {
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void deleteBtnClicked(View v){
        searchEditText.setText("");
    }
}
