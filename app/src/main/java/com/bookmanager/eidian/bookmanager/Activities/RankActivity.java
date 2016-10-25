package com.bookmanager.eidian.bookmanager.Activities;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.bookmanager.eidian.bookmanager.Adapters.HotMessageAdapter;
import com.bookmanager.eidian.bookmanager.Helpers.HotMsg;
import com.bookmanager.eidian.bookmanager.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RankActivity extends AppCompatActivity {

    private Button year;
    private Button threeMonth;
    private Button month;
    private Button week;
    private List<HotMsg> hotMsgList = new ArrayList<HotMsg>();
    int page = 0;
    int bookNumber = 0;
    String str;         //接受传过来的参数str
    private ListView rank;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            List<HotMsg> response = (List<HotMsg>) msg.obj;
            HotMessageAdapter adapter = new HotMessageAdapter(RankActivity.this,
                    R.layout.item_hot_message_view, response);
            rank.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        year = (Button) findViewById(R.id.year);
        threeMonth = (Button) findViewById(R.id.three_month);
        month = (Button) findViewById(R.id.month);
        week = (Button) findViewById(R.id.week);
        rank = (ListView) findViewById(R.id.rank_list);


        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rankYear();
            }
        });
        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rankWeek();
            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rankMonth();
            }
        });
        threeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rankThreeMonth();
            }
        });
    }

    public void rankYear() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                page = 0;    //将新书推荐的页面重新置为0
                Document document = null;
                try {
                    document = Jsoup.connect("http://211.69.140.4:8991/opac_lcl" +
                            "_chi/loan_top_ten/loan.ALL.ALL.y").get();
                    Elements elements = document.getElementsByTag("li");
                    Elements elements1 = document.getElementsByTag("a");
                    StringBuilder builder = new StringBuilder();

                    hotMsgList = new ArrayList<HotMsg>();
                    for (int i=0;i<elements.size();i++){
                        HotMsg hotMsg = new HotMsg();
                        hotMsg.setBookMessageUrl(elements1.get(i).attr("url"));
                        hotMsg.setBookMsg(elements.get(i).text());
                        hotMsgList.add(hotMsg);
                    }

                    Message message = new Message();
                    message.obj = hotMsgList;
                    handler.sendMessage(message);
                    Log.d("HotMessage",document.text());

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    public void rankWeek() {
        page=0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://211.69.140.4:8991/" +
                            "opac_lcl_chi/loan_top_ten/loan.ALL.ALL.w").get();
                    Elements elements = document.getElementsByTag("li");
                    hotMsgList = new ArrayList<HotMsg>();
                    Elements elements1 = document.getElementsByTag("a");

                    for (int i=0;i<elements.size();i++){
                        HotMsg hotMsg = new HotMsg();
                        hotMsg.setBookMessageUrl(elements1.get(i).attr("url"));
                        hotMsg.setBookMsg(elements.get(i).text());
                        hotMsgList.add(hotMsg);
                    }

                    Message message = new Message();
                    message.obj = hotMsgList;
                    handler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void rankMonth() {
        page = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://211.69.140.4:8991/" +
                            "opac_lcl_chi/loan_top_ten/loan.ALL.ALL.m").get();
                    Elements elements = document.getElementsByTag("li");
                    hotMsgList = new ArrayList<HotMsg>();
                    Elements elements1 = document.getElementsByTag("a");

                    for (int i=0;i<elements.size();i++){
                        HotMsg hotMsg = new HotMsg();
                        hotMsg.setBookMessageUrl(elements1.get(i).attr("url"));
                        hotMsg.setBookMsg(elements.get(i).text());
                        hotMsgList.add(hotMsg);
                    }

                    Message message = new Message();
                    message.obj = hotMsgList;
                    handler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void rankThreeMonth() {
        page = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("http://211.69.140.4:8991/" +
                            "opac_lcl_chi/loan_top_ten/loan.ALL.ALL.s").get();
                    Elements elements = document.getElementsByTag("li");
                    Log.d("HotMessageRankThreeMB",elements.text());
                    hotMsgList = new ArrayList<HotMsg>();
                    Elements elements1 = document.getElementsByTag("a");

                    for (int i=0;i<elements.size();i++){
                        HotMsg hotMsg = new HotMsg();
                        hotMsg.setBookMessageUrl(elements1.get(i).attr("url"));
                        hotMsg.setBookMsg(elements.get(i).text());
                        hotMsgList.add(hotMsg);
                    }
                    Message message = new Message();
                    message.obj = hotMsgList;
                    handler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
