package com.example.parkminhyun.foodworldcup;

/**
 * Created by 황수정 on 2017-11-24.
 */

public class ListItem {
    private String[] mData;
    public ListItem(String[] data){
        mData=data;
    }
    public ListItem(String ID, String check){
        mData=new String[2];
        mData[0]=ID;
        mData[1]=check;
    }
    public ListItem(String ID, String Name, String Password){
        mData=new String[3];
        mData[0]=ID;
        mData[1]=Name;
        mData[2]=Password;
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
