package com.bookmanager.eidian.bookmanager.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.bookmanager.eidian.bookmanager.Entities.Activities;
import com.bookmanager.eidian.bookmanager.Entities.Notice;
import com.bookmanager.eidian.bookmanager.R;

import java.util.List;

/**
 * Created by xiang on 2016/9/24/024.
 */

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.AViewHolder> {

    private List<Activities> list;

    public ActivitiesAdapter(List<Activities> objects) {
        this.list = objects;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private NoticeAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListenner(NoticeAdapter.OnItemClickListener onItemClickListenner){
        this.onItemClickListener = onItemClickListenner;
    }


    @Override
    public ActivitiesAdapter.AViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item, parent,false);
        ActivitiesAdapter.AViewHolder viewHolder = new ActivitiesAdapter.AViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AViewHolder holder, int position) {
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

    class AViewHolder extends RecyclerView.ViewHolder{
        TextView noticeTitle;
        public AViewHolder(View itemView) {
            super(itemView);
            noticeTitle = (TextView) itemView.findViewById(R.id.notice_title);
        }
    }
}
