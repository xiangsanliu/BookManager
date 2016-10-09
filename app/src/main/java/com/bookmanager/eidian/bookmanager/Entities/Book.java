package com.xiang.text4.Entities;

/**
 * Created by clmiberf on 2016/9/21.
 */
public class Book {

    private String bookName;

    private String author;

    private String publisher;

    private String publishTime;

    private String callNumber;

    private String titleUrl;

    private String BookImag;

    public String getBookImag() {
        return BookImag;
    }

    public void setBookImag(String bookImag) {
        BookImag = bookImag;
    }

    public String getTitleUrl() {
        return titleUrl;
    }

    public void setTitleUrl(String titleUrl) {
        this.titleUrl = titleUrl;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }
}
