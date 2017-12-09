package com.example.parkminhyun.foodworldcup.Data;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ParkMinHyun on 2017-11-26.
 */

public class FoodInfomationVO {

    private static FoodInfomationVO instance;

    private List<String> food_menuList = new ArrayList<>();
    public BiMap<String, String> map = HashBiMap.create();
    public BiMap reverseMap;

    public static FoodInfomationVO getInstance() {
        if (instance == null) {
            instance = new FoodInfomationVO();
            return instance;
        } else
            return instance;
    }

    public FoodInfomationVO() {
        initMap();
        initList();
        reverseMap = map.inverse();
    }

    private void initList() {
        food_menuList.add("baekban");
        food_menuList.add("bibimbab");
        food_menuList.add("bread");
        food_menuList.add("cake");
        food_menuList.add("chicken");
        food_menuList.add("dduckppoki");
        food_menuList.add("donggas");
        food_menuList.add("doughnut");
        food_menuList.add("gimbab");
        food_menuList.add("hamburger");
        food_menuList.add("ramen");
        food_menuList.add("sandwich");
        food_menuList.add("spagetti");
        food_menuList.add("steak");
        food_menuList.add("sushi");
        food_menuList.add("zazang");

        shufflingFoodList();
    }


    private void initMap() {
        map.put("baekban", "백반");
        map.put("bibimbab", "비빔밥");
        map.put("bread", "빵");
        map.put("cake", "케이크");
        map.put("chicken", "치킨");
        map.put("dduckppoki", "떡볶이");
        map.put("donggas", "돈가스");
        map.put("doughnut", "도넛");
        map.put("gimbab", "김밥");
        map.put("hamburger", "햄버거");
        map.put("ramen", "라멘");
        map.put("sandwich", "샌드위치");
        map.put("spagetti", "스파게티");
        map.put("steak", "스테이크");
        map.put("sushi", "스시");
        map.put("zazang", "짜장면");
    }

    public BiMap<String, String> getMap() {
        return map;
    }

    public BiMap getReverseMap() {
        return reverseMap;
    }

    public List<String> getFood_menuList() {
        return food_menuList;
    }

    // List 셔플링
    private void shufflingFoodList() {
        int foodCount = 16;
        Random rand = new Random();

        for (int start = 0; start < foodCount; start++) {
            int dest = rand.nextInt(16) + 1;

            String temp = foodTournerment_menuList.get(start);
            foodTournerment_menuList.set(start, foodTournerment_menuList.get(dest));
            foodTournerment_menuList.set(dest, temp);
        }
    }
}
