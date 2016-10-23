package com.bookmanager.eidian.bookmanager.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.bookmanager.eidian.bookmanager.Helpers.BaseActivity;
import com.bookmanager.eidian.bookmanager.Helpers.InternetConnection;
import com.bookmanager.eidian.bookmanager.Helpers.LibraryList;
import com.bookmanager.eidian.bookmanager.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;

/**
 * Created by clmiberf on 2016/9/19.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    static final int SHOW_RESPONSE = 0;

    private SharedPreferences.Editor editor;


    private List<String> list;

    private SharedPreferences pref;

    private LibraryList myLibrary = new LibraryList();

    private CircleButton login;

    private EditText accountEdit;

    private EditText passwordEdit;

    private CheckBox rememberPass;

    private StringBuilder builder = new StringBuilder();


    //判断是否登陆成功或者是否获取到Library的URL
    int isSeccuss = 0;

    private Handler handler = new Handler(){

        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
//                    Intent intent = new Intent(Login.this,ShowActivity.class);
//                    intent.putExtra("data_extra",response);
//                    startActivity(intent);
                    Log.d("Login", String.valueOf(isSeccuss));
                    if (!response.equals("-1")) {
                        Log.d("1222222222334345", response);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("search", list.get(4));
                        intent.putExtra("myLibrary", list.get(0));
                        intent.putExtra("hotMessage", list.get(1));
                        intent.putExtra("reader", list.get(2));
                        intent.putExtra("history", list.get(3));
                        intent.putExtra("isLogined", true);
                        startActivity(intent);
                        finish();
                    }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("哎呀~出错啦!");
                    builder.setMessage("请检查账号和密码后重试");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    });
                    builder.show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        login = (CircleButton) findViewById(R.id.login_button);
        accountEdit = (EditText) findViewById(R.id.account);
        rememberPass = (CheckBox) findViewById(R.id.remember_passward);
        passwordEdit = (EditText) findViewById(R.id.password);
        login.setOnClickListener(this);

        boolean isRemember = pref.getBoolean("remember_password_lib",false);
        if (isRemember){
            //将账号和密码都设置到文本框中
            String account = pref.getString("account_lib","");
            String password = pref.getString("password_lib","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(passwordEdit.getWindowToken(), 0);

        final String account = accountEdit.getText().toString();
        final String password = passwordEdit.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document document = null;
                try {
                    document =  Jsoup.connect("http://211.69.140.4:8991/F").get();

                    Elements elements = document.getElementsByTag("a");
                    Node mNode = elements.first();

                    String path = mNode.attr("href");

                    myLibrary.setMyLogin(elements.first().attr("href"));



                    URL url1 = new URL(path);
                    HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                    connection1.setConnectTimeout(8000);
                    connection1.setRequestProperty("Accept", "text/html," +
                                "application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                    connection1.setRequestProperty("Referer", path);
                    connection1.setRequestProperty("User-Agent", "Mozilla/5.0 " +
                                "(Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");
                    connection1.setRequestProperty("Host", "211.69.140.4:8991");
                    connection1.setRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
                    connection1.setRequestProperty("Upgrade-Insecure-Requests", "1");
                    connection1.setRequestMethod("POST");
                    //设置请求体
                    StringBuilder sb = new StringBuilder();
                    sb.append("func=login-session")
                            .append("&login_source=bor-info")
                            .append("&bor_id=" + URLEncoder.encode(account, "UTF-8"))
                            .append("&bor_verification=" + URLEncoder.encode(password, "UTF-8"))
                            .append("&bor_library=HZA50");

                    String parame = sb.toString();
                    byte[] postParame = parame.getBytes();
                    Log.d("sdoeiwoewweew", parame);
                    //提交请求体
                    DataOutputStream out = new DataOutputStream(connection1.getOutputStream());
                    out.write(postParame);
                    out.flush();
                    out.close();
                    String cookieVal = null;
                    String cookie = null;
                    String key = null;
                    int n = 0;
                    if (connection1.getResponseCode() == 200) {
                        Log.d("111111111111", "111111111111");
                        InputStream in = connection1.getInputStream();
                        //获取流中的数据
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }

                        Document document1 = Jsoup.parse(response.toString());
                        Element elements1 = document1.getElementById("header");
                        String isLogin = elements1.getElementsByTag("a").first().text();
                        Log.d("LoginActivity",isLogin);
                        if (isLogin.equals("退出")) {
                            //list用来存放各类url
                            list = new ArrayList<String>();
                            //获取各类信息网页
                            //我的图书馆的信息
                            myLibrary.setMyLibrary(elements1.childNode(5).attr("href"));
                            Log.d("1111111111MyLibrary", myLibrary.getMyLibrary());
                            list.add(myLibrary.getMyLibrary());
//                       builder.append(myLibrary.getMyLibrary());
                            //热门推荐
                            myLibrary.setHotMessage(elements1.childNode(16).attr("href"));
                            Log.d("222222222HotMessage", myLibrary.getHotMessage());
                            list.add(myLibrary.getHotMessage());
//                       builder.append(myLibrary.getHotMessage());
                            //读者推荐
                            myLibrary.setReaderRecommend(elements1.childNode(11).attr("href"));
                            Log.d("333333333reader", myLibrary.getReaderRecommend());
                            list.add(myLibrary.getReaderRecommend());
                            //搜索结果
                            myLibrary.setHistory(elements1.childNode(20).attr("href"));
                            Log.d("44444444History", myLibrary.getHistory());
                            list.add(myLibrary.getHistory());
                            //搜索
                            myLibrary.setSearch(elements1.childNode(3).attr("href"));
                            Log.d("55555555search", myLibrary.getSearch());
                            list.add(myLibrary.getSearch());
                            builder.append(myLibrary);
                            Log.d("12345667890", myLibrary.getMyLibrary());

                            InternetConnection ic = new InternetConnection(myLibrary.getMyLibrary(), path);
                            Log.d("000000000000", ic.getResponse());
                            editor = pref.edit();
                            if (rememberPass.isChecked()){
                                editor.putBoolean("remember_password_lib",true);
                                editor.putString("account_lib",account);
                                editor.putString("password_lib",password);
                            }else {
                                editor.clear();
                            }
                            editor.commit();
                            //使用异步消息处理机制传输数据
                            Message message = new Message();

                            message.what = SHOW_RESPONSE;
                            message.obj = ic.getResponse();
                            handler.sendMessage(message);
                        }else {
                            //使用异步消息处理机制传输数据
                            Message message = new Message();

                            message.what = SHOW_RESPONSE;
                            message.obj = "-1";
                            handler.sendMessage(message);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }
}
