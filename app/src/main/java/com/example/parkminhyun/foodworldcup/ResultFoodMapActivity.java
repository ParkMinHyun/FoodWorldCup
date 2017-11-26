package com.example.parkminhyun.foodworldcup;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parkminhyun.foodworldcup.GPS.GPSInfo;
import com.example.parkminhyun.foodworldcup.GPS.GeoPoint;
import com.example.parkminhyun.foodworldcup.GPS.GeoTrans;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.collect.BiMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ResultFoodMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GPSInfo gpsInfo;
    private FoodInfomation foodInfomation;

    private EditText searchEditText;

    private BiMap<String, String> foodMap;
    private GoogleMap gMap;
    private GeoPoint convertedGeoPoint;


    private int previousActivity;
    private double latitude, longitude;
    private LatLng currentPos;
    private Address currentDong;

    private String searchText;
    private String cityName, resultFoodName;
    private String homepageUrl, storeName;

    private List<String> foodStoreName, foodStoreAddr, foodStoreMapX, foodStoreMapY;
    private List<Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_food_map);


        foodInfomation = FoodInfomation.getInstance();
        foodMap = foodInfomation.getMap();
        searchEditText = (EditText) findViewById(R.id.search);

//        resultFoodName = getIntent().getExtras().getString("resultFood");
//        previousActivity = getIntent().getExtras().getInt("previousActivity");

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
        gMap.setOnInfoWindowClickListener(this);

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
        searchText = currentDong.getSubLocality() + ' ' + currentDong.getThoroughfare() + ' ' + "짜장면";
        Log.i("음식점", currentDong.getSubLocality());
//        cityName = currentDong.getThoroughfare();

//        searchText = currentDong.getSubLocality() + ' ' + "백반";
//        if(currentDong.getSubLocality() == null)
//            cityName = currentDong.getThoroughfare();
//        else
//            cityName = currentDong.getSubLocality() + ' ' + currentDong.getThoroughfare();

//
//        searchText =
//                (previousActivity == MenuWorldCupActivity.MenuWorldCupActivity)
//                ? cityName + ' ' + foodMap.get(resultFoodName)
//                : cityName + ' ' + resultFoodName;

        // 네이버 검색 API 어싱크로 동작시키기
        ResultFoodMapActivity.NaverSearchAPIAsyncTask naverSearchAPIAsyncTask = new ResultFoodMapActivity.NaverSearchAPIAsyncTask();
        naverSearchAPIAsyncTask.execute();

//        a.getAdminArea()+" "+a.getLocality()+" "+a.getThoroughfare();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        try {
            storeName = URLEncoder.encode(marker.getTitle().toString(), "UTF-8");

            homepageUrl = "https://m.search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=" + storeName;
            JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
            jsoupAsyncTask.execute();
            Toast.makeText(getApplicationContext(), marker.getTitle().toString(), Toast.LENGTH_SHORT).show();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private class JsoupAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(homepageUrl).get();
                Elements link = doc.select(".biz_name");
                String relHref = link.attr("href");

                StringBuilder relHrefStringBuilder = new StringBuilder(relHref);
                String baseURL = relHrefStringBuilder.substring(0, relHrefStringBuilder.indexOf("query=") + "query=".length());

                return baseURL + storeName;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(getApplicationContext(), ResultFoodInfoActivity.class);
            intent.putExtra("StoreURL", result);
            startActivity(intent);
        }
    }

    private class NaverSearchAPIAsyncTask extends AsyncTask<Void, Void, Void> {

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

                String reviseFoodstoreName = jObject.getString("title").replace("<b>", " ");
                foodStoreName.add(reviseFoodstoreName.replace("</b>", ""));
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
            searchText = cityName + ' ' + searchEditText.getText();

        gMap.clear();
        markers.clear();
        foodStoreName.clear();
        foodStoreAddr.clear();
        foodStoreMapX.clear();
        foodStoreMapY.clear();

        addCurrentPosionMarker(currentPos);

        // 네이버 검색 API 어싱크로 동작시키기
        ResultFoodMapActivity.NaverSearchAPIAsyncTask naverSearchAPIAsyncTask = new ResultFoodMapActivity.NaverSearchAPIAsyncTask();
        naverSearchAPIAsyncTask.execute();

    }

    private void addCurrentPosionMarker(LatLng currentPos) {
        gMap.addMarker(new MarkerOptions().position(currentPos)
                .title("현재 위치")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }

    protected void hideSoftKeyboard(View view) {
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void deleteBtnClicked(View v) {
        searchEditText.setText("");
    }
}
