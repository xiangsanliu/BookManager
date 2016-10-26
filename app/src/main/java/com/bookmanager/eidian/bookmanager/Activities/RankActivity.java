package com.bookmanager.eidian.bookmanager.Activities;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.bookmanager.eidian.bookmanager.Adapters.HotMessageAdapter;
import com.bookmanager.eidian.bookmanager.Adapters.MyViewPagerAdapter;
import com.bookmanager.eidian.bookmanager.Fragments.HomePageFragments.ActivityForecastFragment;
import com.bookmanager.eidian.bookmanager.Fragments.HomePageFragments.NewsFragment;
import com.bookmanager.eidian.bookmanager.Fragments.HomePageFragments.NoticeFragment;
import com.bookmanager.eidian.bookmanager.Fragments.RankFragment;
import com.bookmanager.eidian.bookmanager.Helpers.HotMsg;
import com.bookmanager.eidian.bookmanager.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViewPagerHomePage();
    }

    private void initViewPagerHomePage() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        MyViewPagerAdapter viewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(RankFragment.newInstance(1), "一年排行");
        viewPagerAdapter.addFragment(RankFragment.newInstance(4), "三月排行");
        viewPagerAdapter.addFragment(RankFragment.newInstance(2), "一周排行");
        viewPagerAdapter.addFragment(RankFragment.newInstance(3), "一月排行");
        viewPager.setAdapter(viewPagerAdapter);//设置适配器
        tabLayout.setupWithViewPager(viewPager);
    }
}
