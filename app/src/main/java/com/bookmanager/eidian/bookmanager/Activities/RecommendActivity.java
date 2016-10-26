package com.bookmanager.eidian.bookmanager.Activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bookmanager.eidian.bookmanager.Adapters.HotMessageAdapter;
import com.bookmanager.eidian.bookmanager.Adapters.SpacesItemDecoration;
import com.bookmanager.eidian.bookmanager.Helpers.HotMsg;
import com.bookmanager.eidian.bookmanager.R;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecommendActivity extends AppCompatActivity {

    static final int SHOW_NEW_RESPONSE = 0;

    private List<HotMsg> hotMsgList = new ArrayList<HotMsg>();

    private RecyclerView hotmsgView;

    private MaterialRefreshLayout materialRefreshLayout;

    HotMessageAdapter hAdapter;

    //newBook页面记载
    int page = 0;

    int bookNumber = 0;

    String str;         //接受传过来的参数str

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_NEW_RESPONSE:
                    Object responseNew = msg.obj;
                    hAdapter = new HotMessageAdapter((List<HotMsg>) responseNew);
                    hotmsgView.setAdapter(hAdapter);
//                    hotmsgView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                            HotMsg hotMsg = hotMsgList.get(i);
//                            String path = str+"find-b&find_code=SYS&local_base=HZA01&request="+
//                                    hotMsg.getBookMessageUrl()+"&con_lng=chi";
//                            Intent intent = new Intent(RecommendActivity.this,Show_new_book_message.class);
//                            intent.putExtra("pathUrl",path);
//
//                            startActivity(intent);
//                        }
//                    });
                    break;
                case 2:
                    hAdapter.notifyDataSetChanged();
                default:
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        hotmsgView = (RecyclerView) findViewById(R.id.show_hotmessage);

        hotmsgView.setLayoutManager(new LinearLayoutManager(this));
        hotmsgView.addItemDecoration(new SpacesItemDecoration(1));
        materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.swipe_to_refresh);
        Intent intent = getIntent();
        str = intent.getStringExtra("str");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    page = 1;
                    bookNumber = (page-1)*20;
                    Document document = Jsoup.connect("http://211.69.140.4:8991/cgi-bin" +
                            "/newbook.cgi?total=5410&base=01&date=180&cls=ALL&page="+page).get();
                    Log.d("HotMessageNewBook",document.text());
                    String data = document.text().substring(8,document.text().length()-54);
                    StringBuilder builder = new StringBuilder();
                    for (int i =1;i<data.length();i++) {

                        HotMsg hotMsg = new HotMsg();
                        if ((data.charAt(i-1)=='{'&&data.charAt(i)<58&&data.charAt(i)>47)
                                ||(data.charAt(i-1)==','&&data.charAt(i)<58&&data.charAt(i)>47)){
                            StringBuilder number = new StringBuilder();
                            int j = i;
                            while(data.charAt(j)!=':'){
                                number.append(data.charAt(j)+"");
                                j++;
                            }
                            builder = number;
                        }

                        if (data.charAt(i) == '"' && data.charAt(i - 1) == ':' && data.charAt(i-2)=='t') {
                            StringBuilder builder1 = new StringBuilder();
                            int t = i + 1;
                            while (data.charAt(t) != '"') {

                                builder1.append(data.charAt(t) + "");
                                t++;
                            }
                            hotMsg.setBookMessageUrl(builder.toString());
                            hotMsg.setBookMsg(++bookNumber+""+". "+builder1.toString());
                            i = t-1;
                            hotMsgList.add(hotMsg);
                        }

                    }

                    Message message = new Message();
                    message.what = SHOW_NEW_RESPONSE;
                    message.obj = hotMsgList;
                    handler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        materialRefreshLayout.setLoadMore(true);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {

            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            page = 1;
                            bookNumber = (page-1)*20;
                            Document document = Jsoup.connect("http://211.69.140.4:8991/cgi-bin" +
                                    "/newbook.cgi?total=5410&base=01&date=180&cls=ALL&page="+page).get();
                            String data = document.text().substring(8,document.text().length()-54);
                            StringBuilder builder = new StringBuilder();
                            for (int i =1;i<data.length();i++) {

                                HotMsg hotMsg = new HotMsg();
                                if ((data.charAt(i-1)=='{'&&data.charAt(i)<58&&data.charAt(i)>47)
                                        ||(data.charAt(i-1)==','&&data.charAt(i)<58&&data.charAt(i)>47)){
                                    StringBuilder number = new StringBuilder();
                                    int j = i;
                                    while(data.charAt(j)!=':'){
                                        number.append(data.charAt(j)+"");
                                        j++;
                                    }
                                    Log.d("HotMessageBookNumber",number.toString());
                                    builder = number;
                                }

                                if (data.charAt(i) == '"' && data.charAt(i - 1) == ':' && data.charAt(i-2)=='t') {
                                    StringBuilder builder1 = new StringBuilder();
                                    int t = i + 1;
                                    while (data.charAt(t) != '"') {

                                        builder1.append(data.charAt(t) + "");
                                        t++;
                                    }
                                    hotMsg.setBookMessageUrl(builder.toString());
                                    hotMsg.setBookMsg(++bookNumber+""+". "+builder1.toString());
                                    i = t-1;
                                    hotMsgList.add(hotMsg);
                                }

                            }


                            Message message = new Message();
                            message.what = SHOW_NEW_RESPONSE;
                            message.obj = hotMsgList;
                            handler.sendMessage(message);
                            materialRefreshLayout.finishRefresh();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            page++;
                            bookNumber = (page-1)*20;
                            Document document = Jsoup.connect("http://211.69.140.4:8991/cgi-bin" +
                                    "/newbook.cgi?total=5410&base=01&date=180&cls=ALL&page=" + page).get();
                            String data = document.text().substring(8, document.text().length() - 54);
                            StringBuilder builder = new StringBuilder();
                            for (int i = 1; i < data.length(); i++) {

                                HotMsg hotMsg = new HotMsg();
                                if ((data.charAt(i - 1) == '{' && data.charAt(i) < 58 && data.charAt(i) > 47)
                                        || (data.charAt(i - 1) == ',' && data.charAt(i) < 58 && data.charAt(i) > 47)) {
                                    StringBuilder number = new StringBuilder();
                                    int j = i;
                                    while (data.charAt(j) != ':') {
                                        number.append(data.charAt(j) + "");
                                        j++;
                                    }
                                    builder = number;
                                }
//                                hotMsgList = new ArrayList<HotMsg>();
                                if (data.charAt(i) == '"' && data.charAt(i - 1) == ':' && data.charAt(i - 2) == 't') {
                                    StringBuilder builder1 = new StringBuilder();
                                    int t = i + 1;
                                    while (data.charAt(t) != '"') {

                                        builder1.append(data.charAt(t) + "");
                                        t++;
                                    }
                                    hotMsg.setBookMessageUrl(builder.toString());
                                    hotMsg.setBookMsg(++bookNumber+""+". "+builder1.toString());
                                    i = t - 1;
                                    hotMsgList.add(hotMsg);
                                }

                            }


                            Message message = new Message();
                            message.what = 2;
//                            message.obj = hotMsgList;
                            handler.sendMessage(message);
                            materialRefreshLayout.finishRefreshLoadMore();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }
}
