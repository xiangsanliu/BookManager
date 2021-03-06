package com.bookmanager.eidian.bookmanager.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
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

import static java.security.AccessController.getContext;

/**
 * Created by lune on 2016/9/22.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{


    private List<News> newses;
    private Activity activity;

    public NewsAdapter(List<News> newses, Activity activity) {
        this.newses = newses;
        this.activity=activity;
    }


    //自定义ViewHolder类
    static class NewsViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView news_title;
        ImageView news_enter;

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
        View v= LayoutInflater.from(activity).inflate(R.layout.news_item,viewGroup,false);
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
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）
        int height = metric.heightPixels;
        personViewHolder.news_title.setText(newses.get(position).getTitle());
        Picasso.with(personViewHolder.news_enter.getContext()).load(newses.get(position)
                .getPhotoUrl()).resize(width,height/4).centerCrop().into(personViewHolder.news_enter);

        //为btn_share btn_readMore cardView设置点击事件
        personViewHolder.cardView.setMinimumHeight(height/4);
        personViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            String flag = "NewsFragment";
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity,ActivityContentActivity.class);
                intent.putExtra("url",newses.get(position).getUrl()); //当前点击的新闻传递给新闻显示界面
                intent.putExtra("photoUrl", newses.get(position).getPhotoUrl());
                intent.putExtra("fragmentCode", flag);
                intent.putExtra("newsTitle", newses.get(position).getTitle());
                activity.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return newses.size();
    }
}
