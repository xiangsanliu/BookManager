package com.bookmanager.eidian.bookmanager.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookmanager.eidian.bookmanager.Activities.ActivityContentActivity;
import com.bookmanager.eidian.bookmanager.Entities.News;
import com.bookmanager.eidian.bookmanager.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lune on 2016/9/22.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{


    private List<News> newses;
    private Context context;

    public NewsAdapter(List<News> newses, Context context) {
        this.newses = newses;
        this.context=context;
    }


    //自定义ViewHolder类
    static class NewsViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView news_title;
        ImageView news_enter;
        Button share;
        Button readMore;

        public NewsViewHolder(final View itemView) {
            super(itemView);
            cardView= (CardView) itemView.findViewById(R.id.card_view);
            news_title= (TextView) itemView.findViewById(R.id.news_title);
            news_enter = (ImageView) itemView.findViewById(R.id.news_pic);
            //设置TextView背景为半透明
            //news_title.setBackgroundColor(Color.argb(20, 0, 0, 0));

        }


    }
    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v= LayoutInflater.from(context).inflate(R.layout.news_item,viewGroup,false);
        NewsViewHolder nvh=new NewsViewHolder(v);
        return nvh;
    }

    /**
     * 此方法在onCreateViewHolder方法返回自定义的holder之前，为hold里面的控件进行内容初始化
     * @param personViewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(NewsViewHolder personViewHolder, final int position) {
        final int j=position;

        personViewHolder.news_title.setText(newses.get(position).getTitle());
        Picasso.with(personViewHolder.news_enter.getContext()).load(newses.get(position)
                .getPhotoUrl()).resize(720,300).centerCrop().into(personViewHolder.news_enter);

        //为btn_share btn_readMore cardView设置点击事件
        personViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            String flag = "NewsFragment";
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ActivityContentActivity.class);
                intent.putExtra("url",newses.get(position).getUrl()); //当前点击的新闻传递给新闻显示界面
                intent.putExtra("photoUrl", newses.get(position).getPhotoUrl());
                intent.putExtra("fragmentCode", flag);
                intent.putExtra("newsTitle", newses.get(position).getTitle());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return newses.size();
    }
}
