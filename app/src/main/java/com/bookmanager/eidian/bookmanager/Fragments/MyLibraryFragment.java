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
import android.widget.TextView;


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

    private TextView showInfo;

    private List<String> contentList ;

    static  final  int SHOW_RESPONSE = 0;

    //当未获取到myLibrary信息时
    int al=0;
    private AlertDialog.Builder dialog;

    private Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case SHOW_RESPONSE:
                    String show = String.valueOf(message.obj);
                    if (show != null){
                        showInfo.setText(show);
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

        showInfo = (TextView) view.findViewById(R.id.show_mylibrary);
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
                Log.d("Library1", response.toString());
                if (response != null) {
                    Document document = Jsoup.parse(response);
                    //获取外借书籍欠款金额
                    Element element3 = document.select("td.td1").last();
//                        StringBuilder borrowingfine = new StringBuilder();
//                        borrowingfine.append("外借书籍当前欠款金额: "+element3.text());
                    contentList.add("外借书籍当前欠款金额: " + element3.text());
                    Log.d("Library8", element3.text());
                }else{
                    dialog.setMessage("未获取到欠款信息"+"\n");
                    al++;
                }

                //获取在借书籍
                String pathBorrow = str + "bor-loan&adm_library=HZA50";
                InternetConnection it = new InternetConnection(pathBorrow, myLibrary1);
                Log.d("debug_path", pathBorrow);

                Log.d("Library2", it.getResponse());
                if (!it.getResponse().isEmpty()) {
                    Document document1 = Jsoup.parse(it.getResponse());

                    Elements elements1 = document1.select("td.td1");
                    //获得elements1的size
                    StringBuilder sbl;         //存放每本书的信息
                    int totalBorrow = elements1.size();
                    int iBorrow = 1;
                    while (iBorrow < totalBorrow) {
                        String no = elements1.get(1).text();
                        String author = elements1.get(3).text();
                        String bName = elements1.get(4).text();
                        String year = elements1.get(5).text();
                        String deadline = elements1.get(6).text();
                        String fine = elements1.get(7).text();
                        String callNumber = elements1.get(9).text();
                        sbl = new StringBuilder();
                        sbl.append("\n")
                                .append("NO." + no).append("\n")
                                .append("作者: " + author).append("\n")
                                .append("书名: " + bName).append("\n")
                                .append("出版年: " + year).append("\n")
                                .append("应还时期: " + deadline).append("\n")
                                .append("罚款: " + fine).append("\n")
                                .append("索书号: " + callNumber).append("\n");
                        Log.d("Borrowing", sbl.toString());
                        contentList.add(sbl.toString());
                        iBorrow += 14;
                    }
                    Log.d("Library3", elements1.text());
                    contentList.add("当前在借书籍:");
                    contentList.add("NO.   著者   题名   出版年   应还日期   罚款   索书号");
                }else{
                    dialog.setMessage("未获取到当前借书信息"+"\n");
                    al++;
                }

                //获取借阅历史
                String pathBorrowed = str + "bor-history-loan&adm_library=HZA50";
                Log.d("Path", pathBorrowed);
                InternetConnection inc = new InternetConnection(pathBorrowed, myLibrary1);
                if (!inc.getResponse().isEmpty()) {
                    Document document2 = Jsoup.parse(inc.getResponse());
                    Elements elements2 = document2.getElementsByTag("table");
                    Log.d("Library", elements2.get(1).text());
                    Elements element = document2.select("td.td1");
                    Log.d("LibraryPathBorrowed", element.text());
                    StringBuilder sb;
                    Log.d("Math", String.valueOf(element.size()));
                    int total = element.size();
                    int i = 1;
                    while (i < total) {
                        String no = element.get(i).text();
                        String author = element.get(i + 1).text();
                        String bName = element.get(i + 2).text();
                        String year = element.get(i + 3).text();
                        String deadline = element.get(i + 4).text();
                        String shouldRetime = element.get(i + 5).text();
                        String reDate = element.get(i + 6).text();
                        String reTime = element.get(i + 7).text();
//                            String fine = element.get(i+8).text();
                        sb = new StringBuilder();
                        Log.d("Library4", String.valueOf(i));
                        sb.append("No." + no).append("\n")
                                .append("著者: " + author).append("\n")
                                .append("书名: " + bName).append("\n")
                                .append("出版年份: " + year).append("\n")
                                .append("应还时间: " + deadline + " " + shouldRetime).append("\n")
                                .append("归还时间: " + reDate + " " + reTime).append("\n");
                        Log.d("09u8798887uhhkkjh", sb.toString());
                        contentList.add(sb.toString());
                        Log.d("Library7", String.valueOf(contentList.size()));
                        i += 10;
                    }
                }else {
                    dialog.setMessage("未获取到历史借书信息"+"\n");
                    dialog.setCancelable(false);
                    al++;

                }
                Log.d("0999999090909090", "77777777777");
                Message message = new Message();
                message.what = SHOW_RESPONSE;
                message.obj = contentList.toString();
                handler.sendMessage(message);

            }
        }).start();


        return view;
    }

}
