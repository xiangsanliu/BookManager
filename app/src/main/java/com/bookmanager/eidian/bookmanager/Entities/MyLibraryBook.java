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

    public MyLibraryBook(String author, String bookName, String year, String should_date, String returned_date, String lib) {
        this.author = author;
        this.bookName = bookName;
        this.year = year;
        this.should_date = should_date;
        this.returned_date = returned_date;
        this.lib = lib;
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
