package com.example.parkminhyun.foodworldcup.ETC;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by ParkMinHyun on 2017-11-28.
 */

public class FavoritesStoreSharedPreferences {

    private static FavoritesStoreSharedPreferences instance;
    private boolean favoritesFlag;

    public static FavoritesStoreSharedPreferences getInstance() {
        if (instance == null) {
            instance = new FavoritesStoreSharedPreferences();
            return instance;
        } else
            return instance;
    }

    // 값 불러오기
    public boolean getPreferences(Context context, String storeURL) {
        SharedPreferences pref = context.getSharedPreferences("favoritesPref", Activity.MODE_PRIVATE);

        if ((pref != null) && (pref.contains(storeURL))) {
            favoritesFlag = pref.getBoolean(storeURL, Boolean.parseBoolean(""));
            Log.i("SharedPreferenc Status", String.valueOf(favoritesFlag));
            return favoritesFlag;
        }
        return false;
    }

    // 값 저장하기
    public void savePreferences(Context context, String storeURL, Boolean favoritesFlag) {
        SharedPreferences pref = context.getSharedPreferences("favoritesPref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(storeURL, favoritesFlag);
        this.favoritesFlag = favoritesFlag;
        Log.i("SharedPreferenc Status", String.valueOf(favoritesFlag) + "저장되었습니다.");
        editor.apply();
    }

    // 값(Key Data) 삭제하기
    public static void clearPreferences(Context context) {
        SharedPreferences pref = context.getSharedPreferences("favoritesPref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
