package com.bookmanager.eidian.bookmanager.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.bookmanager.eidian.bookmanager.Helpers.BaseActivity;
import com.bookmanager.eidian.bookmanager.Helpers.InternetConnection;
import com.bookmanager.eidian.bookmanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by clmiberf on 2016/9/26.
 */
public class Show_rank_book_message extends BaseActivity {

    private TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_rank_book_msg);
        Intent intent = getIntent();
        final String path = intent.getStringExtra("path");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                InternetConnection in = new InternetConnection(path,path);
                parseJSONWithJSONObject(in.getResponse());


            }
        }).start();
    }

    //JSONObject解析json
    private void parseJSONWithJSONObject(String jsonData){
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.d("Show_rank_book_message",jsonObject.toString());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
