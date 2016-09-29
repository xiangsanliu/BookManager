package com.bookmanager.eidian.bookmanager.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.bookmanager.eidian.bookmanager.Entities.Activities;
import com.bookmanager.eidian.bookmanager.R;

import java.util.List;

/**
 * Created by xiang on 2016/9/24/024.
 */

public class ActivitiesAdapter extends ArrayAdapter<Activities> {

    private int resourceId;

    public ActivitiesAdapter(Context context, int resource, List<Activities> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Activities activities = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null ){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.newsTitle = (TextView) view.findViewById(R.id.activity_title);
            view.setTag(viewHolder); //将ViewHolder存储在View中
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); //重新获取ViewHolder
        }
        viewHolder.newsTitle.setText(activities.getTitle());
        return view;
    } //对控件的实例进行缓存
    class ViewHolder {
        TextView newsTitle;
    }
}
