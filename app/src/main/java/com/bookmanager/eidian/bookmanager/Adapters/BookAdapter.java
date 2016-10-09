package com.xiang.text4.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.xiang.text4.Entities.Book;
import com.xiang.text4.R;

import java.util.List;

/**
 * Created by clmiberf on 2016/9/21.
 */
public class BookAdapter extends ArrayAdapter<Book> {

    private int resourceId;

    public BookAdapter(Context context, int textViewResourceId, List<Book> objects) {
        super(context,textViewResourceId, objects);
        resourceId = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Book book = getItem(position);
        View view ;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.bName = (TextView) view.findViewById(R.id.tx_name);
            viewHolder.bAuthor = (TextView) view.findViewById(R.id.tx_author);
            viewHolder.bPublisher = (TextView) view.findViewById(R.id.tx_information);
            viewHolder.bPublishTime = (TextView) view.findViewById(R.id.tx_time);
            viewHolder.bCallNumber = (TextView) view.findViewById(R.id.tx_callNumber);
            view.setTag(viewHolder);            //将viewHolder存储在view内
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.bName.setText(book.getBookName());
        viewHolder.bAuthor.setText(book.getAuthor());
        viewHolder.bPublisher.setText(book.getPublisher());
        viewHolder.bPublishTime.setText(book.getPublishTime());
        viewHolder.bCallNumber.setText(book.getCallNumber());
        return view;
    }

    class ViewHolder{
        TextView bName;
        TextView bAuthor;
        TextView bPublisher;
        TextView bPublishTime;
        TextView bCallNumber;
    }
}
