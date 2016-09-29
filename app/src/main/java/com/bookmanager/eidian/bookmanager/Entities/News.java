package com.xiang.text4.Entities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import com.xiang.text4.Connection;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;


/**
 * Created by nizijun2014 on 2016/9/24.
 */
public class News implements Serializable {
    String title;
    String url;
    String photoUrl;
    Bitmap bitmap;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        new Thread(new Runnable() {
            HttpURLConnection connection;
            @Override
            public void run() {
                try {
                    connection = Connection.getConnectionToHZAUlib(photoUrl);

                    InputStream is = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();




        return bitmap;
    }

    public News(String title, String url, String photoUrl) {
        this.title = title;
        this.url = url;
        this.photoUrl = photoUrl;
    }

    public News(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc(){
        String desc = "11";
        return desc;
    }
    public Bitmap getPhoto(){
        Bitmap photo = null;
        return photo;
    }
}
