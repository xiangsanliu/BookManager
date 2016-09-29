package com.xiang.text4.Fragments.HomePageFragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xiang.text4.Activities.ActivityContentActivity;
import com.xiang.text4.Adapters.NoticeAdapter;
import com.xiang.text4.Connection;
import com.xiang.text4.Entities.Notice;
import com.xiang.text4.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeFragment extends Fragment {

    private ListView listView ;

    private List<Notice> noticeList;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Object newsResponse = msg.obj;
                    final NoticeAdapter noticeAdapter = new NoticeAdapter(getActivity(), R.layout.notice_item, (List<Notice>) newsResponse );
                    listView.setAdapter(noticeAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String flag = "NoticeFragment";
                            try {
                                Intent intent = new Intent(getActivity(), ActivityContentActivity.class);
                                intent.putExtra("url", noticeList.get(position).getUrl());
                                intent.putExtra("fragmentCode", flag);
                                intent.putExtra("newsTitle", noticeList.get(position).getTitle());
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

            }
        }
    };

    private static com.xiang.text4.Fragments.HomePageFragments.NoticeFragment instance=null;
    public static com.xiang.text4.Fragments.HomePageFragments.NoticeFragment newInstance() {
        if(instance==null){
            instance= new com.xiang.text4.Fragments.HomePageFragments.NoticeFragment();
        }
        return instance;
    }


    public NoticeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notice, container, false);
        listView = (ListView) view.findViewById(R.id.notice_list);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection  ;
                try {
                    connection = Connection.getConnectionToHZAUlib("http://lib.hzau.edu.cn/");
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    String datas = response.toString();
                    /****/
                    Document document = Jsoup.parse(datas);
                    Elements elements = document.select("ul.list-unstyled");
                    Elements element = elements.get(0).getElementsByTag("li");
                    noticeList = new ArrayList<Notice>();
                    for (int i=0;i<element.size();i++) {
                        String Title = element.get(i).getElementsByTag("a").text();
                        noticeList.add(new Notice(Title, element.get(i).getElementsByTag("a").attr("href")));
                    }
                    /**************************/
                    Message message = new Message();
                    message.what = 0;
                    //将服务器返回的数据存放到Message中
                    message.obj = noticeList;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return view;
    }

}
