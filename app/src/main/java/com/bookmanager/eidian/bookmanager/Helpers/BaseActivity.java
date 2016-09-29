package com.bookmanager.eidian.bookmanager.Helpers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bookmanager.eidian.bookmanager.Helpers.ActivityCollector;

/**
 * Created by xiang on 2016/9/18/018.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
