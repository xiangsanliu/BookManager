package com.bookmanager.eidian.bookmanager.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.bookmanager.eidian.bookmanager.Activities.Show_new_book_message;
import com.bookmanager.eidian.bookmanager.Adapters.HotMessageAdapter;
import com.bookmanager.eidian.bookmanager.Helpers.HotMsg;
import com.bookmanager.eidian.bookmanager.Helpers.NewBook;
import com.bookmanager.eidian.bookmanager.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by clmiberf on 2016/10/11.
 */

public class HotMessageFragment extends Fragment{
    static final int SHOW_NEW_RESPONSE = 0;

    static  final int SHOW_Rank_RESPONSE = 1;

    private Button previousBtn;

    private Button nextBtn;

    private Button rankYearBtn;

    private Button rankThMBtn;

    private Button rankOMBtn;

    private Button rankOWBtn;

    private Button newBookBtn;

    private List<HotMsg> hotMsgList;

    private ListView hotmsgView;

    private List<NewBook> newHotBookList;

    //newBook页面记载
    int page = 0;

    int bookNumber = 0;

    String str;         //接受传过来的参数str

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_NEW_RESPONSE:
                    Object responseNew = msg.obj;
                    HotMessageAdapter hAdapter = new HotMessageAdapter(getActivity(),
                            R.layout.item_hot_message_view, (List<HotMsg>) responseNew);
//                    hotmsgView = (ListView) findViewById(R.id.show_hot_message);
                    hotmsgView.setAdapter(hAdapter);
                    hotmsgView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            HotMsg hotMsg = hotMsgList.get(i);
                            Log.d("hotmsgView",hotMsg.getBookMessageUrl());
                            String path = str+"find-b&find_code=SYS&local_base=HZA01&request="+
                                    hotMsg.getBookMessageUrl()+"&con_lng=chi";
                            Intent intent = new Intent(getActivity(),Show_new_book_message.class);
                            intent.putExtra("pathUrl",path);

                            Log.d("00000000000HotMeesage",path);
                            startActivity(intent);
                        }
                    });
                    break;
                case SHOW_Rank_RESPONSE:
                    Object response =  msg.obj;
                    HotMessageAdapter adapter = new HotMessageAdapter(getActivity(),
                            R.layout.item_hot_message_view, (List<HotMsg>) response);
//                    hotmsgView = (ListView) view.findViewById(R.id.show_hot_message);
                    hotmsgView.setAdapter(adapter);
//                  hotmsgView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                      @Override
//                      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                          HotMsg hotMsg = hotMsgList.get(i);
//                          String path = str.substring(0,str.length()-6)+hotMsg;
//                          Intent intent = new Intent(HotMessage.this,Show_new_book_message.class);
//                          intent.putExtra("path",path);
//                          startActivity(intent);
//                      }
//                  });
//                  break;
                default:
                    break;

            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_hot_message_layout,container,false);
        rankYearBtn = (Button) view.findViewById(R.id.hot_rank_year_button);
        rankThMBtn = (Button) view.findViewById(R.id.hot_rank_three_month);
        rankOMBtn = (Button) view.findViewById(R.id.hot_rank_one_month);
        rankOWBtn = (Button) view.findViewById(R.id.hot_rank_one_week);
        newBookBtn = (Button) view.findViewById(R.id.new_book_button);
        nextBtn = (Button) view.findViewById(R.id.next);
        previousBtn = (Button) view.findViewById(R.id.previous);
        hotmsgView = (ListView) view.findViewById(R.id.show_hot_message);
        Bundle bundle = getArguments();
        str = bundle.getString("str");

        rankYearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 0;    //将新书推荐的页面重新置为0
                new Thread(new Runnable() {
                    @Override
                    public void run() {
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
                            message.what = SHOW_Rank_RESPONSE;
                            message.obj = hotMsgList;
                            handler.sendMessage(message);
                            Log.d("HotMessage",document.text());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });

        rankThMBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            message.what = SHOW_Rank_RESPONSE;
                            message.obj = hotMsgList;
                            handler.sendMessage(message);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });

        rankOMBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            message.what = SHOW_Rank_RESPONSE;
                            message.obj = hotMsgList;
                            handler.sendMessage(message);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        rankOWBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            message.what = SHOW_Rank_RESPONSE;
                            message.obj = hotMsgList;
                            handler.sendMessage(message);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        newBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            hotMsgList = new ArrayList<HotMsg>();
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
                                    Log.d("HotMessage4", builder1.toString());
                                    hotMsg.setBookMessageUrl(builder.toString());
                                    hotMsg.setBookMsg(++bookNumber+""+". "+builder1.toString());
                                    i = t-1;
                                    Log.d("kkkkk",hotMsg.getBookMessageUrl());
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
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page != 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                page++;
                                bookNumber = (page-1)*20;
                                Document document = Jsoup.connect("http://211.69.140.4:8991/cgi-bin" +
                                        "/newbook.cgi?total=5410&base=01&date=180&cls=ALL&page=" + page).get();
                                Log.d("HotMessageNewBook", document.text());
                                String data = document.text().substring(8, document.text().length() - 54);
                                StringBuilder builder = new StringBuilder();
                                hotMsgList = new ArrayList<HotMsg>();
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
                                        Log.d("HotMessageBookNumber", number.toString());
                                        builder = number;
                                    }

                                    if (data.charAt(i) == '"' && data.charAt(i - 1) == ':' && data.charAt(i - 2) == 't') {
                                        StringBuilder builder1 = new StringBuilder();
                                        int t = i + 1;
                                        while (data.charAt(t) != '"') {

                                            builder1.append(data.charAt(t) + "");
                                            t++;
                                        }
                                        Log.d("HotMessage4", builder1.toString());
                                        hotMsg.setBookMessageUrl(builder.toString());
                                        hotMsg.setBookMsg(++bookNumber+""+". "+builder1.toString());
                                        i = t - 1;
                                        Log.d("kkkkk", hotMsg.getBookMessageUrl());
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
                }
            }
        });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page > 1) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                page--;
                                bookNumber = (page-1)*20;
                                Document document = Jsoup.connect("http://211.69.140.4:8991/cgi-bin" +
                                        "/newbook.cgi?total=5410&base=01&date=180&cls=ALL&page=" + page).get();
                                Log.d("HotMessageNewBook", document.text());
                                String data = document.text().substring(8, document.text().length() - 54);
                                StringBuilder builder = new StringBuilder();
                                hotMsgList = new ArrayList<HotMsg>();
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
                                        Log.d("HotMessageBookNumber", number.toString());
                                        builder = number;
                                    }

                                    if (data.charAt(i) == '"' && data.charAt(i - 1) == ':' && data.charAt(i - 2) == 't') {
                                        StringBuilder builder1 = new StringBuilder();
                                        int t = i + 1;
                                        while (data.charAt(t) != '"') {

                                            builder1.append(data.charAt(t) + "");
                                            t++;
                                        }
                                        Log.d("HotMessage4", builder1.toString());
                                        hotMsg.setBookMessageUrl(builder.toString());
                                        hotMsg.setBookMsg(++bookNumber+""+". "+builder1.toString());
                                        i = t - 1;
                                        Log.d("kkkkk", hotMsg.getBookMessageUrl());
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
                }else if (page == 1){
                    Toast.makeText(getActivity(),"没有上一页啦~",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;

    }
}
