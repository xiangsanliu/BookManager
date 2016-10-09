package com.bookmanager.eidian.bookmanager.Fragments.HomePageFragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bookmanager.eidian.bookmanager.Activities.ActivityContentActivity;
import com.bookmanager.eidian.bookmanager.Adapters.ActivitiesAdapter;
import com.bookmanager.eidian.bookmanager.Entities.Activities;
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

public class ActivityForecastFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    private static ActivityForecastFragment instance=null;

    private List<Activities> activitiesList;
    ListView listView ;

    private static final int SHOW_RESPONSE = 0;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_RESPONSE:
                    Object newsResponse = msg.obj;
                    final ActivitiesAdapter activitiesAdapter = new ActivitiesAdapter(getActivity(), R.layout.activities_item, (List<Activities>) newsResponse );
                    listView.setAdapter(activitiesAdapter);
                    swipeRefreshLayout.setRefreshing(!swipeRefreshLayout.isRefreshing());
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String flag = "ActivityForecastFragment";
                            try {
                                Intent intent = new Intent(getActivity(), ActivityContentActivity.class);
                                intent.putExtra("url", activitiesList.get(position).getUrl());
                                intent.putExtra("fragmentCode", "ActivityForecastFragment");
                                intent.putExtra("newsTitle", activitiesList.get(position).getTitle());
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
            }
        }
    };
    public static ActivityForecastFragment newInstance() {
        if(instance==null){
            instance= new ActivityForecastFragment();
        }
        return instance;
    }

    public ActivityForecastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_forecast, container, false);
        listView = (ListView) view.findViewById(R.id.list_view);
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
                            Elements element = elements.get(1).getElementsByTag("li");
                            activitiesList = new ArrayList<Activities>();
                            for (int i=0;i<element.size();i++) {
                                String Title = element.get(i).getElementsByTag("a").text();
                                activitiesList.add(new Activities(Title, element.get(i).getElementsByTag("a").attr("href")));
                            }
                            /**************************/
                            Message message = new Message();
                            message.what = SHOW_RESPONSE;
                            //将服务器返回的数据存放到Message中
                            message.obj = activitiesList;
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
