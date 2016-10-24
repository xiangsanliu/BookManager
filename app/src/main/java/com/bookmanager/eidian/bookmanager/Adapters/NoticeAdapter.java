package com.bookmanager.eidian.bookmanager.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bookmanager.eidian.bookmanager.Entities.Notice;
import com.bookmanager.eidian.bookmanager.R;

import java.util.List;

/**
 * Created by lune on 2016/9/24/024.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.MyViewHolder> {

    private List<Notice> list;

    public NoticeAdapter(List<Notice> objects) {
        this.list = objects;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListenner(OnItemClickListener onItemClickListenner){
        this.onItemClickListener = onItemClickListenner;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item, parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.noticeTitle.setText(list.get(position).getTitle());
        if (onItemClickListener!= null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView noticeTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            noticeTitle = (TextView) itemView.findViewById(R.id.notice_title);
        }
    }
}
