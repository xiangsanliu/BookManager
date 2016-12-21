package com.bookmanager.eidian.bookmanager.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bookmanager.eidian.bookmanager.Helpers.InternetConnection;
import com.bookmanager.eidian.bookmanager.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BorrowingBookImformation extends AppCompatActivity implements View.OnClickListener{

    String renew_url = "";
    Button renew_button;
    TextView textView;
    BorrowingBookThread thread = new BorrowingBookThread();
    Handler handler = new BorrowingBookHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowing_book_imformation);
        renew_button = (Button) findViewById(R.id.renew);
        textView = (TextView) findViewById(R.id.borrowing_book_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        thread.setUrl(getIntent().getStringExtra("url"));
        thread.setBook_url(getIntent().getStringExtra("book_url"));
        thread.start();
        renew_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.renew:
                if (renew_url.length()>0) {

                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(BorrowingBookImformation.this);
                    builder.setMessage("确定要续借吗")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            InternetConnection internetConnection = new InternetConnection(renew_url, renew_url);
                                        }
                                    }).start();
                                    Toast.makeText(BorrowingBookImformation.this, "续借成功", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setTitle("提示")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.create().show();
                } else   {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(BorrowingBookImformation.this);
                    builder.setMessage("该书籍已达到最大续借次数")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            })
                            .setTitle("提示");
                    builder.create().show();
                }
                break;
            default:
        }
    }

    class BorrowingBookThread extends Thread {

        String url, book_url;

        public void setUrl(String url) {
            this.url = url;
        }

        public void setBook_url(String book_url) {
            this.book_url = book_url;
        }

        @Override
        public void run() {
            if (!url.equals("a")) {
                InternetConnection internetConnection = new InternetConnection(url, url);
                Document document = Jsoup.parse(internetConnection.getResponse());
                renew_url = document.select("td.td1").get(5).select("A").attr("HREF");

            } else {
                renew_button.setVisibility(View.GONE);
                renew_url = "";
            }
            InternetConnection internetConnection1 = new InternetConnection(book_url, book_url);
            String s = internetConnection1.getResponse();
            Document document1 = Jsoup.parse(s);
            Elements elements = document1.select("tr");
            String content = "";
            for (int i=2;i<elements.size()-2;i++){
                content += elements.get(i).text() +"\n";
            }

            Message message = new Message();
            message.obj = content;
            handler.sendMessage(message);
        }
    }
    class BorrowingBookHandler extends  Handler {
        @Override
        public void handleMessage(Message msg) {
            textView.setText((String) msg.obj);
        }
    }
}
