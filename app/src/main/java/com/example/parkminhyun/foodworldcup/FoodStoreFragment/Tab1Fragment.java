package com.example.parkminhyun.foodworldcup.FoodStoreFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.example.parkminhyun.foodworldcup.R;


public class Tab1Fragment  extends Fragment {
    private static final String TAG = "Tab1Fragment";

    private Button btnTEST;
    private WebView foodStoreWebview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);

        foodStoreWebview = (WebView)view.findViewById(R.id.foodStoreWebview);

        return view;
    }
}