package com.example.parkminhyun.foodworldcup.ETC;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class FoodInfomationVO {

    private static FoodInfomationVO instance;

    public BiMap<String, String> map = HashBiMap.create();
    public BiMap reverseMap;

    public static FoodInfomationVO getInstance(){
        if(instance==null)
        {
            instance = new FoodInfomationVO();
            return instance;
        }
        else
            return instance;
    }

    public FoodInfomationVO(){
        initMap();
        reverseMap = map.inverse();
    }

    private void initMap() {
        map.put("baekban","백반");
        map.put("bibimbab","비빔밥");
        map.put("bread","빵");
        map.put("cake","케이크");
        map.put("chicken","치킨");
        map.put("dduckppoki","떡볶이");
        map.put("donggas","돈가스");
        map.put("doughnut","도넛");
        map.put("gimbab","김밥");
        map.put("hamburger","햄버거");
        map.put("ramen","라멘");
        map.put("sandwich","샌드위치");
        map.put("spagetti","스파게티");
        map.put("steak","스테이크");
        map.put("sushi","스시");
        map.put("zazang","짜장면");
    }

    public BiMap<String, String> getMap() {
        return map;
    }

    public BiMap getReverseMap() {
        return reverseMap;
    }
}
