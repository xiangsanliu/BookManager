package com.bookmanager.eidian.bookmanager.Fragments.HomePageFragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bookmanager.eidian.bookmanager.Activities.ActivityContentActivity;
import com.bookmanager.eidian.bookmanager.Adapters.NoticeAdapter;
import com.bookmanager.eidian.bookmanager.Adapters.SpacesItemDecoration;
import com.bookmanager.eidian.bookmanager.Entities.Notice;
import com.bookmanager.eidian.bookmanager.Helpers.Connection;
import com.bookmanager.eidian.bookmanager.R;

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

    private RecyclerView recyclerView ;

    private List<Notice> noticeList;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Object newsResponse = msg.obj;
                    final NoticeAdapter noticeAdapter = new NoticeAdapter((List<Notice>) newsResponse );
                    recyclerView.setAdapter(noticeAdapter);
                    swipeRefreshLayout.setRefreshing(!swipeRefreshLayout.isRefreshing());
                    noticeAdapter.setOnItemClickListenner(new NoticeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
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
                    }) ;
                    Toast.makeText(getActivity(), "数据刷新成功", Toast.LENGTH_SHORT).show();


            }
        }
    };

    private static NoticeFragment instance=null;
    public static NoticeFragment newInstance() {
        if(instance==null){
            instance= new NoticeFragment();
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
        recyclerView = (RecyclerView) view.findViewById(R.id.notice_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpacesItemDecoration(1));
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh);

        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
            }
        };

        swipeRefreshLayout.setRefreshing(true);
        onRefreshListener.onRefresh();

        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        return view;
    }

}
