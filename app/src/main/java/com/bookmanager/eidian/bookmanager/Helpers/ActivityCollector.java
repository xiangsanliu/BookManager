package com.bookmanager.eidian.bookmanager.Helpers;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiang on 2016/9/18/018.
 */
public class ActivityCollector {
    public static List<AppCompatActivity> activities = new ArrayList();
    public static void addActivity(AppCompatActivity activity) {
        activities.add(activity);
    }
    public static void removeActivity(AppCompatActivity activity) {
        activities.remove(activity);
    }
    public static void finishAll() {
        for(AppCompatActivity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
