package com.bookmanager.eidian.bookmanager.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.bookmanager.eidian.bookmanager.Helpers.JWGetter;
import com.bookmanager.eidian.bookmanager.Helpers.PEGetter;
import com.bookmanager.eidian.bookmanager.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CourseActivity extends AppCompatActivity {

    String account, password, code;
    Button button[][] = new Button[5][7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Intent intent = getIntent();
        account = intent.getStringExtra("account");
        password = intent.getStringExtra("password");
        code = intent.getStringExtra("code");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JWGetter.setCourse(account, password, code, CourseActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
