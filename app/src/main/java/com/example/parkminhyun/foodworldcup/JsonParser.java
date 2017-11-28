package com.example.parkminhyun.foodworldcup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ParkMinHyun on 2017-11-28.
 */

public class JsonParser {

    private static JsonParser instance;

    private List<String> foodStoreNameList = new ArrayList<>();
    private List<String> foodStoreAddrList = new ArrayList<>();
    private List<String> foodStoreMapXList = new ArrayList<>();
    private List<String> foodStoreMapYList = new ArrayList<>();

    public static final int foodStoreName = 0;
    public static final int foodStoreAddr = 1;
    public static final int foodStoreMapX = 2;
    public static final int foodStoreMapY = 3;

    public static JsonParser getInstance(){
        if(instance == null) {
            instance = new JsonParser();
            return instance;
        }
        else
            return instance;
    }

    public JsonParser() {
    }


    // JSON Data 받기
    public Object ReceiveFoodInfoUsingJSON(String response) {

        foodStoreNameList.clear(); foodStoreAddrList.clear();
        foodStoreMapXList.clear(); foodStoreMapYList.clear();

        try {
            JSONObject jsonObject = new JSONObject(response.toString());   // JSONObject 생성
            String items = jsonObject.getString("items");

            JSONArray jarray = new JSONArray(items);   // JSONArray 생성
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출

                String reviseFoodstoreName = jObject.getString("title").replace("<b>", " ");
                foodStoreNameList.add(reviseFoodstoreName.replace("</b>", ""));
                foodStoreAddrList.add(jObject.getString("address"));
                foodStoreMapXList.add(jObject.getString("mapx"));
                foodStoreMapYList.add(jObject.getString("mapy"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<List<String>> foodstoreInfo = new ArrayList<List<String>>();
        foodstoreInfo.add(foodStoreNameList); foodstoreInfo.add(foodStoreAddrList);
        foodstoreInfo.add(foodStoreMapXList); foodstoreInfo.add(foodStoreMapYList);

        return foodstoreInfo;
    }

}
