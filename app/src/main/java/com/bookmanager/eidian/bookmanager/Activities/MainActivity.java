package com.bookmanager.eidian.bookmanager.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bookmanager.eidian.bookmanager.Adapters.MyViewPagerAdapter;
import com.bookmanager.eidian.bookmanager.DialogFragments.FeedBackDialogFragment;
import com.bookmanager.eidian.bookmanager.DialogFragments.InfoDialogFragment;
import com.bookmanager.eidian.bookmanager.DialogFragments.SouthLakeDialogFragment;
import com.bookmanager.eidian.bookmanager.Fragments.HomePageFragments.ActivityForecastFragment;
import com.bookmanager.eidian.bookmanager.Fragments.HomePageFragments.NewsFragment;
import com.bookmanager.eidian.bookmanager.Fragments.HomePageFragments.NoticeFragment;
import com.bookmanager.eidian.bookmanager.Helpers.ActivityCollector;
import com.bookmanager.eidian.bookmanager.Helpers.BaseActivity;
import com.bookmanager.eidian.bookmanager.Helpers.InternetConnection;
import com.bookmanager.eidian.bookmanager.Helpers.JWGetter;
import com.bookmanager.eidian.bookmanager.Helpers.LibraryGetter;
import com.bookmanager.eidian.bookmanager.Helpers.LibraryList;
import com.bookmanager.eidian.bookmanager.Helpers.PEGetter;
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

import okhttp3.OkHttpClient;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "MainActivity";
    private String search;
    private List<String> list;
    private String myLibraryUrl;
    private String str;
    private SharedPreferences pref;
    static final int SHOW_RESPONSE = 0;
    String name;
    private TextView textView;
    private TextView textView1;
    int i ;
    String account;
    private Handler handler = new MainHandler();

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        account = pref.getString("account", "");
        final String password = pref.getString("password_lib", "");
        if ( account.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("还未设置账号, 请先设置账号")
                    .setPositiveButton("设置账号", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MainActivity.this, SetAccountActivity.class));
                            finish();
                        }
                    });
            builder.create().show();
        } else {
            login(account, password);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //动态添加Navigation的头layout
        View view = navigationView.inflateHeaderView(R.layout.nav_header_main);
        textView = (TextView) view.findViewById(R.id.name);
        textView1 = (TextView) view.findViewById(R.id.studentNumber);
        initViewPagerHomePage();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_exit) {
            ActivityCollector.finishAll();//退出程序
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("查询中");
        progressDialog.create();
        final FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        int id = item.getItemId();
        switch (id) {
            case R.id.imageView:
                break;
            case R.id.book_search:
                onClickSearchbook();
                break;
            case R.id.south_lake:
                onClickSouthLake(progressDialog);
                break;
            case R.id.pe_score:;
                onClickPeScore(progressDialog);
                break;
            case R.id.my_library:
                onClickMyLibrary();
                break;
            case R.id.recommend:
                onClickRecomment(str);
                break;
            case R.id.rank:
                startActivity(new Intent(MainActivity.this, RankActivity.class));
                break;
            case R.id.info:
                InfoDialogFragment infoDialogFragment = new InfoDialogFragment();
                infoDialogFragment.show(fragmentManager, TAG);
                break;
            case R.id.feed_back:
                FeedBackDialogFragment feedBackDialogFragment = new FeedBackDialogFragment();
                feedBackDialogFragment.show(fragmentManager, TAG);
                break;
            case R.id.account_setting:
                startActivity(new Intent(MainActivity.this, SetAccountActivity.class));
                finish();
                break;
            case R.id.help_each_other:
                funDeveloping();
                //startActivity(new Intent(MainActivity.this,BmobReaderQueryBookActivity.class));
                break;
            case R.id.course:
                onClickCourse();
                break;
            case R.id.exam_query:
                funDeveloping();
                break;
        }
        invalidateOptionsMenu();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initViewPagerHomePage() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        MyViewPagerAdapter viewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(NewsFragment.newInstance(), "馆内新闻");
        viewPagerAdapter.addFragment(NoticeFragment.newInstance(), "通知公告");
        viewPagerAdapter.addFragment(ActivityForecastFragment.newInstance(), "活动预告");
        viewPager.setAdapter(viewPagerAdapter);//设置适配器
        tabLayout.setupWithViewPager(viewPager);
    }

    private void login(final String account, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    list = LibraryGetter.getLoginUrl(account, password);
                    if (list != null) {
                        name = list.get(6);
                        InternetConnection ic = new InternetConnection(list.get(0), list.get(5));
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


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    if (!response.equals("-1")) {
                        search = list.get(4);
                        myLibraryUrl = list.get(0);
                        i = search.length();
                        str = search.substring(0,i-8);
                        Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        textView.setText(name);
                        textView1.setText(pref.getString("account", ""));

                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("登陆图书馆失败");
                        builder.setMessage("请检查账号和密码后重试");
                        builder.setCancelable(false);
                        builder.setPositiveButton("检查账号", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(MainActivity.this, SetAccountActivity.class));
                                Toast.makeText(MainActivity.this, "请检查账号和密码", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                        builder.show();
                    }
            }
        }
    }
    private void onClickSearchbook() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("请选择需要搜索的方式")
                .setPositiveButton("中文搜索", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {Intent intent = new Intent(MainActivity.this, SearchBookActivity.class);
                        intent.putExtra("search_extra", search);
                        intent.putExtra("isChinese", true);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("英文搜索", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {Intent intent = new Intent(MainActivity.this, SearchBookActivity.class);
                        intent.putExtra("search_extra", search);
                        intent.putExtra("isChinese", false);
                        startActivity(intent);
                    }
                });
        builder.create().show();
    }
    private void onClickCourse() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap bitmap = JWGetter.getCodeBitmap();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            View view = LayoutInflater.from(MainActivity.this)
                                    .inflate(R.layout.code_dialog, null);
                            final EditText editText = (EditText) view.findViewById(R.id.code_edit);
                            ImageView imageView = (ImageView) view.findViewById(R.id.code_image);
                            imageView.setImageBitmap(bitmap);
                            AlertDialog.Builder jwCode = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("请输入验证码")
                                    .setView(view);
                            jwCode.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            jwCode.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final String password = pref.getString("password_jw", "");
                                    final String code = editText.getText().toString();
                                    Intent intent = new Intent(MainActivity.this, CourseActivity.class);
                                    intent.putExtra("account", account);
                                    intent.putExtra("password", password);
                                    intent.putExtra("code", code);
                                    startActivity(intent);
                                }
                            });
                            jwCode.create().show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void onClickSouthLake(final ProgressDialog progressDialog) {
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String pePassword = pref.getString("password_pe", "");
                    final String result = PEGetter.getSouthLake(account, pePassword);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            if (result.equals("fail")) {
                                builder.setMessage("查询失败, 请检查账号、密码或网络连接")
                                        .setTitle("查询失败")
                                        .setPositiveButton("设置账号", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(MainActivity.this, SetAccountActivity.class));
                                                finish();
                                            }
                                        });
                            } else {
                                builder.setMessage(result)
                                        .setTitle("查询成功")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                            }
                            builder.create().show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void onClickPeScore(final ProgressDialog progressDialog) {
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String pePassword = pref.getString("password_pe", "");
                    final String result = PEGetter.getPEScore(account, pePassword);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            if (result.equals("fail")) {
                                builder.setMessage("查询失败, 请检查账号、密码或网络连接")
                                        .setTitle("查询失败")
                                        .setPositiveButton("设置账号", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(MainActivity.this, SetAccountActivity.class));
                                                finish();
                                            }
                                        });
                            } else {
                                builder.setMessage(result)
                                        .setTitle("查询成功")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                            }
                            builder.create().show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void funDeveloping() {
        AlertDialog.Builder helpEachOtherDialog = new AlertDialog.Builder(this);
        helpEachOtherDialog.setTitle("提示")
                .setMessage("模块正在加紧开发中，敬请期待！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        helpEachOtherDialog.show();
    }
    private void onClickMyLibrary() {
        Intent myLibraryIntent = new Intent(MainActivity.this, MyLibraryActivity.class);
        myLibraryIntent.putExtra("str", str);
        myLibraryIntent.putExtra("myLibrary", list.get(0));
        startActivity(myLibraryIntent);
    }
    private void onClickRecomment(String str) {
        Intent recommectIntent = new Intent(MainActivity.this, RecommendActivity.class);
        recommectIntent.putExtra("str", str);
        startActivity(recommectIntent);
    }
}
