package com.example.parkminhyun.foodworldcup.Review;

import android.graphics.drawable.Drawable;

/**
 * Created by codbs on 2017-11-17.
 */
public class ListViewItem {
    private Drawable iconDrawable ;

    private String userId;
    private String titleStr ;
    private String contentStr ;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setUserId(String userid){
        this.userId=userid;
    }
    public void setContent(String desc) {
        contentStr = desc ;
    }

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getUserId() {
        return this.userId ;
    }
    public String getContent() {
        return this.contentStr ;
    }
}


