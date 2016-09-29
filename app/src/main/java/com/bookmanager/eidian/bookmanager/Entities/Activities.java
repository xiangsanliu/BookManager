package com.bookmanager.eidian.bookmanager.Entities;

/**
 * Created by xiang on 2016/9/24/024.
 */

public class Activities {
    String title;
    String url;

    public Activities(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;

    }

    public String getUrl() {
        return url;
    }
}
