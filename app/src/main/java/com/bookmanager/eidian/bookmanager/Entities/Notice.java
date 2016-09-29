package com.bookmanager.eidian.bookmanager.Entities;

/**
 * Created by xiang on 2016/9/24/024.
 */

public class Notice {
    String title ;
    String url;

    public Notice(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
