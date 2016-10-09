package com.bookmanager.eidian.bookmanager.Fragments.HomePageFragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookmanager.eidian.bookmanager.Adapters.NewsAdapter;
import com.bookmanager.eidian.bookmanager.Adapters.SpacesItemDecoration;
import com.bookmanager.eidian.bookmanager.Entities.News;
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
public class NewsFragment extends Fragment {

    RecyclerView recyclerView;
    NewsAdapter adapter;

    List<News> newsList;

    private static final int SHOW_RESPONSE = 0;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_RESPONSE:
                    List<News> object = (List<News>) msg.obj;
                    adapter=new NewsAdapter(object,getActivity());
                    recyclerView.setAdapter(adapter);
            }
        }
    };

    private static NewsFragment instance=null;
    public static NewsFragment newInstance() {
        if(instance==null){
            instance= new NewsFragment();
        }
        return instance;
    }


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);


        //设置item间距
        recyclerView.addItemDecoration(new SpacesItemDecoration(1));
        recyclerView.setLayoutManager(layoutManager);

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
                    Elements elements = document.select("div.carousel-inner");
                    Elements titleElements = elements.get(0).getElementsByAttributeValue("class", "carousel-caption font20");
                    Elements urlElements = elements.get(0).getElementsByTag("a");
                    Elements photoElements = elements.get(0).getElementsByTag("img");
                    newsList = new ArrayList<News>();
                    for (int i=0;i<titleElements.size();i++) {
                        String Url = urlElements.get(i).attr("href");
                        String Title = titleElements.get(i).text();
                        String photoUrl = "http://lib.hzau.edu.cn"+photoElements.get(i).attr("src");
                        newsList.add(new News(Title, Url, photoUrl));
                    }
                    /**************************/
                    Message message = new Message();
                    message.what = SHOW_RESPONSE;
                    message.obj = newsList;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return view;
    }

}
