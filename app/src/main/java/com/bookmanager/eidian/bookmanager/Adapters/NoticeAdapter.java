package com.bookmanager.eidian.bookmanager.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xiang.text4.Entities.Notice;
import com.xiang.text4.R;

import java.util.List;

/**
 * Created by lune on 2016/9/24/024.
 */

public class NoticeAdapter extends ArrayAdapter<Notice> {

    private int resourceId;

    public NoticeAdapter(Context context, int resource, List<Notice> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        Notice notice = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null ){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.noticeTitle = (TextView) view.findViewById(R.id.notice_title);
            view.setTag(viewHolder); //将ViewHolder存储在View中
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); //重新获取ViewHolder
        }
        viewHolder.noticeTitle.setText(notice.getTitle());
        return view;
    }
    class ViewHolder{
        TextView noticeTitle;
    }
}
