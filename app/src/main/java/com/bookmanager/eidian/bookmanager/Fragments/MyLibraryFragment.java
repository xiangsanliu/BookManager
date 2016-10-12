package com.bookmanager.eidian.bookmanager.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.bookmanager.eidian.bookmanager.Activities.BorrowingContent;
import com.bookmanager.eidian.bookmanager.Activities.MyLibraryContent;
import com.bookmanager.eidian.bookmanager.Helpers.InternetConnection;
import com.bookmanager.eidian.bookmanager.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyLibraryFragment extends Fragment {

    private TextView money;
    private Button borrowing_num;
    private Button history_num;
    private Button booked_num;
    private Button pre_num;

    private List<String> contentList ;

    private String[] content = new String[10];

    static  final  int SHOW_RESPONSE = 0;


    //当未获取到myLibrary信息时
    int al=0;
    private AlertDialog.Builder dialog;

    private Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case SHOW_RESPONSE:
                    String show[] ;
                    show = (String[]) message.obj;
                    if (show != null){
                        money.setText(show[0]);
                        borrowing_num.setText(show[1]);
                        history_num.setText(show[2]);
                        booked_num.setText(show[3]);
                        pre_num.setText(show[4]);
                        borrowing_num.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), BorrowingContent.class);
                                intent.putExtra("url", content[5]);
                                startActivity(intent);
                            }
                        });
                        history_num.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), MyLibraryContent.class);
                                intent.putExtra("url", content[6]);
                                startActivity(intent);
                            }
                        });
                        booked_num.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), MyLibraryContent.class);
                                intent.putExtra("url", content[7]);
                                startActivity(intent);
                            }
                        });
                        pre_num.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), MyLibraryContent.class);
                                intent.putExtra("url", content[8]);
                                startActivity(intent);
                            }
                        });


                    }else{
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        });
                        dialog.show();
                    }
            }
        }
    };


    public MyLibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_library, container, false);

        money = (TextView) view.findViewById(R.id.money);
        borrowing_num = (Button) view.findViewById(R.id.borrowing_num);
        history_num = (Button) view.findViewById(R.id.history_num);
        booked_num = (Button) view.findViewById(R.id.booked_num);
        pre_num = (Button) view.findViewById(R.id.pre_num);

        Bundle bundle = getArguments();
        final String myLibrary1 = bundle.getString("myLibrary");
        final String str = bundle.getString("str");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //使用AlertDialog当获取信息出错时弹出信息
                dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("哎呀!出错啦~");
                //存放myLibrary中的信息
                contentList = new ArrayList<String>();
                InternetConnection in = new InternetConnection(myLibrary1, myLibrary1);
                String response = in.getResponse();
                Log.d("Library1", myLibrary1);
                if (response != null) {
                    Document document = Jsoup.parse(response);
//                    Elements elements = document.select("td.td1");
//                    Elements elements1 = elements.get(0).getElementsByTag("td");
//                    Log.d("myLibrary", elements1.get(0).getElementsByTag("a").attr("href"));
//                    String s = document.select("td.td1").get(0).getElementsByTag("td").get(0).getElementsByTag("a").attr("href");
//                    s= s.substring(24, s.length()-3);
//                    Log.d("myLibrary", s);
                    String url;
                    content[0] = "当前过期外借欠款："+ document.select("td.td1").last().text();
                    content[1] = "当前在借书籍数：" + document.select("td.td1").get(0).text();
                    content[2] = "历史借阅书籍数：" + document.select("td.td1").get(1).text();
                    content[3] = "预约请求：" + document.select("td.td1").get(2).text();
                    content[4] = "预定请求：" + document.select("td.td1").get(3).text();
                    for (int i = 1;i<=4;i++) {
                        url = document.select("td.td1").get(i-1).getElementsByTag("td").get(0).getElementsByTag("a").attr("href");
                        content[4+i] = url.substring(24, url.length()-3);
                        Log.d("url", 4+i+" "+url.substring(24, url.length()-3));
                    }

                }else{
                    dialog.setMessage("我的图书馆内容获取失败"+"\n");
                    al++;
                }

                Message message = new Message();
                message.what = SHOW_RESPONSE;
                message.obj = content;
                handler.sendMessage(message);

            }
        }).start();


        return view;
    }

}
