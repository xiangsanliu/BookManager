package com.bookmanager.eidian.bookmanager.Entities;

//import cn.bmob.v3.BmobObject;

/**
 *
 *存放书籍添加者的信息
 * Created by clmiberf on 2016/10/23.
 */

public class ReaderBook /*extends BmobObject*/{

    private String bName;

    private String bAuthor;

    private String bOwner;

    private String bDecription;

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getbAuthor() {
        return bAuthor;
    }

    public void setbAuthor(String bAuthor) {
        this.bAuthor = bAuthor;
    }

    public String getbOwner() {
        return bOwner;
    }

    public void setbOwner(String bOwner) {
        this.bOwner = bOwner;
    }

    public String getbDecription() {
        return bDecription;
    }

    public void setbDecription(String bDecription) {
        this.bDecription = bDecription;
    }
}
