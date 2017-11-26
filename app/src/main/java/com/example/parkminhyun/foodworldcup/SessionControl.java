package com.example.parkminhyun.foodworldcup;

import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.List;

/**
 * Created by 황수정 on 2017-11-26.
 */

public class SessionControl {
    static public DefaultHttpClient httpClient = null;
    static public List<Cookie> cookies;
    public static HttpClient getHttpClient(){
        if(httpClient==null){
            SessionControl.setHttpClient(new DefaultHttpClient());
        }
        return httpClient;
    }

    public static void setHttpClient(DefaultHttpClient httpClient) {
        SessionControl.httpClient=httpClient;
    }
}
