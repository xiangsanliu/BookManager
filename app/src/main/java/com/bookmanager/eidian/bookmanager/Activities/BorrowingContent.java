package com.bookmanager.eidian.bookmanager.Activities;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.bookmanager.eidian.bookmanager.Adapters.MyLibraryBookAdapter;
import com.bookmanager.eidian.bookmanager.Adapters.SpacesItemDecoration;
import com.bookmanager.eidian.bookmanager.Entities.MyLibraryBook;
import com.bookmanager.eidian.bookmanager.Helpers.BaseActivity;
import com.bookmanager.eidian.bookmanager.Helpers.InternetConnection;
import com.bookmanager.eidian.bookmanager.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class BorrowingContent extends BaseActivity {

    RecyclerView recyclerView;
    private MyLibraryBookAdapter adapter;
    List<MyLibraryBook> list;
    LoadBorrowingThread thread = new LoadBorrowingThread();
    Handler handler = new BorrowingHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowing);
        final String url = getIntent().getStringExtra("url");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.borrowing_content);
        recyclerView.addItemDecoration(new SpacesItemDecoration(1));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        thread.setUrl(url);
        thread.start();
    }

    class LoadBorrowingThread extends Thread {
        String url;
        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            InternetConnection internetConnection = new InternetConnection(url, url);
            String response = internetConnection.getResponse();
            Document document = Jsoup.parse(response);
            Elements elements = document.select("td.td1");
            list = new ArrayList<MyLibraryBook>();
            for (int i = 1 ;i<elements.size();i+=11){
                String url = elements.get(i).select("A").attr("HREF");
                String book_url = elements.get(i+3).select("a").attr("href");
                String author = elements.get(i+2).text();
                String bookName = elements.get(i+3).text();
                String bookYear = elements.get(i+4).text();
                String should_date = elements.get(i+5).text();
                String lib = elements.get(i+7).text();
                list.add(new MyLibraryBook(author, bookName, bookYear, should_date, "尚未归还", lib, url, book_url));
            }
            Message message = new Message();
            message.obj = list;
            handler.sendMessage(message);
        }
    }
    class BorrowingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<MyLibraryBook> libraryBooks = (List<MyLibraryBook>) msg.obj;
            adapter = new MyLibraryBookAdapter( BorrowingContent.this,libraryBooks);
            recyclerView.setAdapter(adapter);
        }
    }
}
