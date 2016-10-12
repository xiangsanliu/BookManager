package com.bookmanager.eidian.bookmanager.Entities;

/**
 * Created by xiang on 2016/10/12/012.
 */

public class MyLibraryBook {
    private String author;
    private String bookName;
    private String year;
    private String should_date;
    private String returned_date = " ";
    private String lib;
    private String url;
    private String book_url;


    public String getUrl() {

        return url;
    }


    public String getBook_url() {
        return book_url;
    }

    public MyLibraryBook(String author, String bookName, String year, String should_date, String returned_date, String lib, String url, String book_url) {
        this.author = author;
        this.bookName = bookName;
        this.year = year;

        this.should_date = should_date;
        this.returned_date = returned_date;
        this.lib = lib;
        this.url = url;
        this.book_url = book_url;
    }

    public String getAuthor() {
        return author;
    }

    public String getShould_date() {
        return should_date;
    }

    public String getReturned_date() {
        return returned_date;
    }

    public String getBookName() {
        return bookName;
    }

    public String getYear() {
        return year;
    }

    public MyLibraryBook(String author, String lib, String should_date, String year, String bookName) {
        this.author = author;
        this.lib = lib;
        this.should_date = should_date;
        this.year = year;
        this.bookName = bookName;
    }

    public String getLib() {
        return lib;
    }
}
