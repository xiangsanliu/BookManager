package com.bookmanager.eidian.bookmanager.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bookmanager.eidian.bookmanager.Helpers.HotMsg;
import com.bookmanager.eidian.bookmanager.R;

import java.util.List;


/**
 * Created by clmiberf on 2016/10/11.
 */

public class HotMessageAdapter extends RecyclerView.Adapter<HotMessageAdapter.RankViewHolder> {

    private TextView hotData;

    private List<HotMsg> list;


    public HotMessageAdapter(List<HotMsg> list) {
        this.list = list;
    }

    @Override
    public RankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot_message_view, parent, false);
        RankViewHolder viewHolder = new RankViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RankViewHolder holder, int position) {
        holder.textView.setText(list.get(position).getBookMsg());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RankViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public RankViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.hot_book_name);
        }
    }

}
