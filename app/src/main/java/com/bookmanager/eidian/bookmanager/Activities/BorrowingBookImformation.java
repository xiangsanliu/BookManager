package com.bookmanager.eidian.bookmanager.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class BorrowingBookImformation extends AppCompatActivity {

    String renew_url = "";
    Button renew_button;
    TextView textView;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String content = (String) msg.obj;
            textView.setText(content);

            if (renew_url.length()==0) {
            renew_button.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowing_book_imformation);
        renew_button = (Button) findViewById(R.id.renew);
        textView = (TextView) findViewById(R.id.borrowing_book_content);
        final String url = getIntent().getStringExtra("url");
        final String book_url = getIntent().getStringExtra("book_url");

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!url.equals("a")) {
                    InternetConnection internetConnection = new InternetConnection(url, url);
                    Document document = Jsoup.parse(internetConnection.getResponse());
                    renew_url = document.select("td.td1").get(5).select("A").attr("HREF");

                } else {
                    renew_url = "";
                }
                InternetConnection internetConnection1 = new InternetConnection(book_url, book_url);
                String s = internetConnection1.getResponse();
                Document document1 = Jsoup.parse(s);
                Elements elements = document1.select("tr");
                String content = "";
                for (int i=2;i<elements.size();i++){
                    content += elements.get(i).text() +"\n";
                }

                Message message = new Message();
                message.obj = content;
                handler.sendMessage(message);
            }
        }).start();



        renew_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    renew_button.setVisibility(View.GONE);
                }
            }
        });

    }
}
