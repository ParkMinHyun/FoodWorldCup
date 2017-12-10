package com.example.parkminhyun.foodworldcup.Review;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parkminhyun.foodworldcup.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;

    // ListViewAdapter의 생성자
    public ListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_custom, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView) ;
        TextView useridTextView = (TextView) convertView.findViewById(R.id.text_userid) ;
        TextView contentTextView = (TextView) convertView.findViewById(R.id.text_content) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageDrawable(listViewItem.getIcon());
        useridTextView.setText(listViewItem.getUserId());
        contentTextView.setText(listViewItem.getContent());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Context context, String userId ,String point, String content) {

        ListViewItem item = new ListViewItem();
        Drawable starimage;
        switch (point){
            case "1.0":
                starimage = ContextCompat.getDrawable(context, R.drawable.star1);
                break;
            case "2.0":
                starimage = ContextCompat.getDrawable(context, R.drawable.star2);
                break;
            case "3.0":
                starimage = ContextCompat.getDrawable(context, R.drawable.star3);
                break;
            case "4.0":
                starimage = ContextCompat.getDrawable(context, R.drawable.star4);
                break;
            case "5.0":
                starimage = ContextCompat.getDrawable(context, R.drawable.star5);
                break;
            default:
                starimage = ContextCompat.getDrawable(context, R.drawable.star1);
                break;

        }

        item.setIcon(starimage);
        item.setUserId(userId);
        item.setContent(content);

        listViewItemList.add(item);
    }

}

