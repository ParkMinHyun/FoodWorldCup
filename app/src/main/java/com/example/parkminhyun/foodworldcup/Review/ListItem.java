package com.example.parkminhyun.foodworldcup.Review;

/**
 * Created by codbs on 2017-11-29.
 */
public class ListItem {
    private String[] mData;
    public ListItem(String[] data){
        mData=data;
    }
    public ListItem(String ID,String check){
        mData=new String[2];
        mData[0]=ID;
        mData[1]=check;
    }

    //채윤 추가
    public ListItem(String ID,String Content,String Star,String Storename){
        mData=new String[4];
        mData[0]=ID;
        mData[1]=Content;
        mData[2]=Star;
        mData[3]=Storename;
    }
    public String[] getData(){
        return mData;
    }
    public String getData(int index){
        return mData[index];
    }
    public void setData(String[] data){
        mData=data;
    }
}