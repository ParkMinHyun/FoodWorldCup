package com.example.parkminhyun.foodworldcup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parkminhyun.foodworldcup.Data.FoodInfomationVO;
import com.example.parkminhyun.foodworldcup.ETC.JsonParser;
import com.example.parkminhyun.foodworldcup.GPS.GPSInfo;
import com.example.parkminhyun.foodworldcup.GPS.GeoPoint;
import com.example.parkminhyun.foodworldcup.GPS.GeoTrans;
import com.example.parkminhyun.foodworldcup.JSoup.JSoup_AsnynTask;
import com.example.parkminhyun.foodworldcup.JSoup.JsoupAsyncResponse;
import com.example.parkminhyun.foodworldcup.NaverAPI.NaverAsyncResponse;
import com.example.parkminhyun.foodworldcup.NaverAPI.NaverAPI_AsnycTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.collect.BiMap;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ResultFoodMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener, NaverAsyncResponse, NaverAPI_AsnycTask.NaverAsyncResponse,
        JsoupAsyncResponse, JSoup_AsnynTask.JsoupAsyncResponse {

    private GPSInfo gpsInfo;
    private FoodInfomationVO foodInfomationVO;

    private EditText searchEditText;

    private BiMap<String, String> foodMap;
    private GoogleMap gMap;
    private GeoPoint convertedGeoPoint;

    private double latitude, longitude;
    private LatLng currentPos;
    private Address currentDong;
    private List<Marker> markers;

    private String searchText;
    private String cityName, resultFoodName;
    private String homepageUrl, storeName;

    private int previousActivity;
    private static final int ResultFoodInfoActivityMode = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_food_map);

        propertyInit();
    }

    private void propertyInit() {

        // Binding
        foodInfomationVO = FoodInfomationVO.getInstance();
        searchEditText = (EditText) findViewById(R.id.search);
        foodMap = foodInfomationVO.getMap();

        // putExtra 값 받기
        resultFoodName = getIntent().getExtras().getString("resultFood");
        previousActivity = getIntent().getExtras().getInt("previousActivity");

        // EditText 내용 초기화
        searchEditText.setText((foodMap.get(resultFoodName) != null) ? foodMap.get(resultFoodName) : resultFoodName);

        // list 초기화
        markers = new ArrayList<Marker>();
        gpsInfo = new GPSInfo(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setOnInfoWindowClickListener(this);

        // 현재 위치 성절 및 이동
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
//        searchText = currentDong.getSubLocality() + ' ' + currentDong.getThoroughfare() + ' ' + "짜장면";
//        cityName = currentDong.getThoroughfare();
//        searchText = currentDong.getSubLocality() + ' ' + "백반";

        if (currentDong.getSubLocality() == null)
            cityName = currentDong.getThoroughfare();
        else
            cityName = currentDong.getSubLocality() + ' ' + currentDong.getThoroughfare();

        searchText = (previousActivity == MenuWorldCupActivity.MenuWorldCupActivityMode)
                ? cityName + ' ' + foodMap.get(resultFoodName)
                : cityName + ' ' + resultFoodName;

        // 네이버 검색 API 어싱크로 동작시키기
        new NaverAPI_AsnycTask(this, searchText, ResultFoodInfoActivityMode).execute();
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        try {
            storeName = URLEncoder.encode(marker.getTitle().toString(), "UTF-8");

            homepageUrl = "https://m.search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=" + storeName;

            // 네이버 검색 API 어싱크로 동작시키기
            new JSoup_AsnynTask(this, homepageUrl, storeName).execute();
            Toast.makeText(getApplicationContext(), marker.getTitle().toString(), Toast.LENGTH_SHORT).show();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processOfJsoupAsyncFinish(String storeURL) {
        Intent intent = new Intent(getApplicationContext(), ResultFoodInfoActivity.class);
        intent.putExtra("StoreURL", storeURL);
        startActivity(intent);
    }

    @Override
    public void processOfNaverAsyncFinish(String response) {
        JsonParser jsonParser = JsonParser.getInstance();

        List<List<String>> foodstoreInfoList = (List<List<String>>) jsonParser.ReceiveFoodStoreInfoUsingJSON(response);
        // Marker 생성
        for (int i = 0; i < foodstoreInfoList.get(0).size(); i++) {

            GeoPoint geoPoint = new GeoPoint(
                    Double.parseDouble(foodstoreInfoList.get(JsonParser.foodStoreMapX).get(i)),
                    Double.parseDouble(foodstoreInfoList.get(JsonParser.foodStoreMapY).get(i)));

            convertedGeoPoint = GeoTrans.convert(1, 0, geoPoint);

            // marker 생성 완료
            markers.add(gMap.addMarker(new MarkerOptions().position(new LatLng(convertedGeoPoint.y, convertedGeoPoint.x))
                    .title(foodstoreInfoList.get(JsonParser.foodStoreName).get(i))
                    .snippet(foodstoreInfoList.get(JsonParser.foodStoreAddr).get(i))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("foodstore_pin", 120, 130)))));
        }
    }

    public void findFoodStoreBtnClicked(View v) {
        // 키보드 내리기
        hideSoftKeyboard(v);

        // 검색하기
        if (searchEditText.getText().length() != 0)
            searchText = cityName + ' ' + searchEditText.getText();

        // Map Clear
        gMap.clear();
        markers.clear();

        addCurrentPosionMarker(currentPos);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 15));

        // 네이버 검색 API 어싱크로 동작시키기
        new NaverAPI_AsnycTask(this, searchText, ResultFoodInfoActivityMode).execute();
    }

    private void addCurrentPosionMarker(LatLng currentPos) {
        gMap.addMarker(new MarkerOptions().position(currentPos)
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("currentposition_pin", 120, 130))));
    }

    protected void hideSoftKeyboard(View view) {
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void deleteBtnClicked(View v) {
        searchEditText.setText("");
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

}
