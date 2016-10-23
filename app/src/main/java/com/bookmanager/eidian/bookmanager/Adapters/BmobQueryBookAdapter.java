package com.bookmanager.eidian.bookmanager.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bookmanager.eidian.bookmanager.Entities.ReaderBook;
import com.bookmanager.eidian.bookmanager.R;

import java.util.List;

/**
 * Created by clmiberf on 2016/10/23.
 */

public class BmobQueryBookAdapter extends ArrayAdapter<ReaderBook> {

    private int resourceId;
    public BmobQueryBookAdapter(Context context, int resource, List<ReaderBook> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReaderBook readerBook = getItem(position);
        View view ;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.bName = (TextView) view.findViewById(R.id.bmob_query_bname);
            viewHolder.bAuthor = (TextView) view.findViewById(R.id.bmob_query_bauthor);
            viewHolder.bOwner = (TextView) view.findViewById(R.id.bmob_query_bowner);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.bName.setText(readerBook.getbName());
        viewHolder.bAuthor.setText(readerBook.getbAuthor());
        viewHolder.bOwner.setText(readerBook.getbOwner());

        return view;
    }

    class ViewHolder{
        TextView bName;
        TextView bAuthor;
        TextView bOwner;


    }
}
