package com.example.parkminhyun.foodworldcup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

/**
 * Created by hyeon on 2017. 11. 28..
 */

public class MyVideoView extends VideoView {

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    } // Constructor

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        DisplayMetrics displayMetrics = new DisplayMetrics();

        Display dis = ((WindowManager)getContext().
                        getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;

//        setMeasuredDimension(dis.getWidth(), dis.getHeight());
        setMeasuredDimension(deviceWidth, deviceHeight);
    }
}
