package com.example.parkminhyun.foodworldcup;


/**
 * Created by ParkMinHyun on 2017-11-28.
 */

public class FavoritesStoreSharedPreferences {

    private static FavoritesStoreSharedPreferences instance;

    public static FavoritesStoreSharedPreferences getInstance(){
        if(instance==null)
        {
            instance = new FavoritesStoreSharedPreferences();
            return instance;
        }
        else
            return instance;
    }

}
