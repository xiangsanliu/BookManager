package com.bookmanager.eidian.bookmanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bookmanager.eidian.bookmanager.Entities.Book;
import com.bookmanager.eidian.bookmanager.Fragments.HotMessageFragment;
import com.bookmanager.eidian.bookmanager.Helpers.HotMsg;
import com.bookmanager.eidian.bookmanager.R;

import java.util.List;


/**
 * Created by clmiberf on 2016/10/11.
 */

public class HotMessageAdapter extends ArrayAdapter<HotMsg>{
    private int resourceId;

    private TextView hotData;

    public HotMessageAdapter(Context context, int textViewResourceId, List<HotMsg> objects) {
        super(context,textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
//    public HotMessageAdapter(Context context, int resource, List<HotMsg> objects) {
//        super(context, resource, objects);
//    }


//    public HotMessageAdapter(HotMessageFragment context, int resource, List<HotMsg> objects) {
//        super(context, resource, objects);
//        resourceId = resource;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HotMsg msg = getItem(position);
        View view;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            hotData = (TextView) view.findViewById(R.id.hot_book_name);
            view.setTag(hotData);
        }else {
            view = convertView;
            hotData = (TextView) view.getTag();
        }
        hotData.setText(msg.getBookMsg());
        return view;
    }

}
