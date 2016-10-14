package com.bookmanager.eidian.bookmanager.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bookmanager.eidian.bookmanager.Adapters.MyViewPagerAdapter;
import com.bookmanager.eidian.bookmanager.DialogFragments.FeedBackDialogFragment;
import com.bookmanager.eidian.bookmanager.DialogFragments.InfoDialogFragment;
import com.bookmanager.eidian.bookmanager.Fragments.FindBookFragment;
import com.bookmanager.eidian.bookmanager.Fragments.HomePageFragments.ActivityForecastFragment;
import com.bookmanager.eidian.bookmanager.Fragments.HomePageFragments.NewsFragment;
import com.bookmanager.eidian.bookmanager.Fragments.HomePageFragments.NoticeFragment;
import com.bookmanager.eidian.bookmanager.Fragments.HotMessageFragment;
import com.bookmanager.eidian.bookmanager.Fragments.MyLibraryFragment;
import com.bookmanager.eidian.bookmanager.Fragments.RankFragment;
import com.bookmanager.eidian.bookmanager.Fragments.ReaderForumFragment;
import com.bookmanager.eidian.bookmanager.Fragments.Settings;
import com.bookmanager.eidian.bookmanager.Helpers.ActivityCollector;
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

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean isLogined = false;
    private boolean showSearchButton = false;
    private SharedPreferences.Editor editor;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private String search;
    private List<String> list;
    private String myLibrary1;
    private String hotMessage;
    private String str;
    private SharedPreferences pref;
    private LibraryList myLibrary = new LibraryList();
    private StringBuilder builder = new StringBuilder();
    static final int SHOW_RESPONSE = 0;
    int isSeccuss = 0;
    Boolean isAutoLogin = false;
    String name;
    private TextView textView;
    private TextView textView1;

    int i ;

    private Handler handler = new Handler(){

        public void handleMessage(Message msg){
            switch (msg.what){
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
//                    Intent intent = new Intent(Login.this,ShowActivity.class);
//                    intent.putExtra("data_extra",response);
//                    startActivity(intent);
                    if (!response.equals("-1")) {
//                        Log.d("1222222222334345", response);
//                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                        intent.putExtra("search", list.get(4));
//                        intent.putExtra("myLibrary", list.get(0));
//                        intent.putExtra("hotMeesage", list.get(1));
//                        intent.putExtra("reader", list.get(2));
//                        intent.putExtra("history", list.get(3));
//                        intent.putExtra("isLogined", true);
//                        startActivity(intent);
//                        finish();

                        search = list.get(4);
                        myLibrary1 = list.get(0);

                        i = search.length();
                        str = search.substring(0,i-8);

                        isLogined = true;
                        Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        textView.setText(name);
                        textView1.setText(pref.getString("account_lib", " "));

                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("哎呀~出错啦!");
                        builder.setMessage("请检查账号和密码后重试");
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                Toast.makeText(MainActivity.this, "请重新登陆", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                        builder.show();
                    }
            }
        }
    };

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        isLogined = intent.getBooleanExtra("isLogined", false);




        pref = PreferenceManager.getDefaultSharedPreferences(this);
        final String account = pref.getString("account_lib", " ");
        final String password = pref.getString("password_lib", " ");
        if ( (!account.equals(" ")) && (!password.equals(" ")) ){
            isAutoLogin = true;
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
                            Elements elements2 = document1.select("td.td2");
                            if (elements2.size()>1) {
                                name = elements2.get(1).text();
                            }
                            String isLogin = elements1.getElementsByTag("a").first().text();
                            if (isLogin.equals("退出")) {
                                //list用来存放各类url
                                list = new ArrayList<String>();
                                //获取各类信息网页
                                //我的图书馆的信息
                                myLibrary.setMyLibrary(elements1.childNode(5).attr("href"));
                                list.add(myLibrary.getMyLibrary());
//                       builder.append(myLibrary.getMyLibrary());
                                //热门推荐
                                myLibrary.setHotMessage(elements1.childNode(16).attr("href"));
                                list.add(myLibrary.getHotMessage());
//                       builder.append(myLibrary.getHotMessage());
                                //读者推荐
                                myLibrary.setReaderRecommend(elements1.childNode(11).attr("href"));
                                list.add(myLibrary.getReaderRecommend());
                                //搜索结果
                                myLibrary.setHistory(elements1.childNode(20).attr("href"));
                                list.add(myLibrary.getHistory());
                                //搜索
                                myLibrary.setSearch(elements1.childNode(3).attr("href"));
                                list.add(myLibrary.getSearch());
                                builder.append(myLibrary);
                                InternetConnection ic = new InternetConnection(myLibrary.getMyLibrary(), path);
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




        if (isLogined && !isAutoLogin) {
            search = intent.getStringExtra("search");
            myLibrary1 = intent.getStringExtra("myLibrary");
            hotMessage = intent.getStringExtra("hotMessage");
//        String reader = intent.getStringExtra("reader");
//        String history = intent.getStringExtra("history");
            //获取相同的参数
            i = search.length();
            str = search.substring(0,i-8);
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
        view.findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogined){
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("您已登陆，确定要退出吗")
                            .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                    finish();
                                }
                            })
                            .setTitle("提示")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.create().show();
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        //当点击搜索按钮，输入内容后，点击键盘上的回车键时，跳转到查询Activity
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
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

    //用来设置搜索图标的隐藏和显示：只有在馆藏查询的时候才显示搜索按钮
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (showSearchButton==true){
            menu.findItem(R.id.search).setVisible(true);
        } else{
            menu.findItem(R.id.search).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        ActionBar actionBar = getSupportActionBar();        //得到actionBar的对象，以更改 Toolbar上的Title
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 当导航栏上的各种按钮被点击后，处理对应的事务
        int id = item.getItemId();

        if (id == R.id.imageView){
        }

        else if (id == R.id.home_page) {
            findViewById(R.id.tabLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.viewPager).setVisibility(View.VISIBLE);
            showSearchButton = false;
            actionBar.setTitle("主页");
            Fragment fragment = fragmentManager.findFragmentByTag("FinishTag");
            if (fragment != null) {
                transaction.remove(fragment).commit();
            }
        }

        else if (id == R.id.book_search) {
            findViewById(R.id.tabLayout).setVisibility(View.GONE);
            findViewById(R.id.viewPager).setVisibility(View.GONE);
            actionBar.setTitle("馆藏查询");
            showSearchButton = true;
            Fragment findBookFragment = new FindBookFragment();
            if (isLogined) {
                Bundle bundle = new Bundle();
                bundle.putString("search_extra",search);
                findBookFragment.setArguments(bundle);
                transaction.replace(R.id.content, findBookFragment, "FinishTag").commit();
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                Toast.makeText(this, "请先登陆", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (id == R.id.south_lake) {
            startActivity(new Intent(MainActivity.this, SouthLakeActivity.class));
        }
        else if (id == R.id.my_library) {
            findViewById(R.id.tabLayout).setVisibility(View.GONE);
            findViewById(R.id.viewPager).setVisibility(View.GONE);
            showSearchButton = false;
            Fragment myLibraryFragment = new MyLibraryFragment();
            actionBar.setTitle("我的图书馆");
            if (isLogined) {
                Bundle bundle = new Bundle();
                bundle.putString("str",str);
                bundle.putString("myLibrary",myLibrary1);
                myLibraryFragment.setArguments(bundle);
                transaction.replace(R.id.content, myLibraryFragment, "FinishTag").commit();
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                Toast.makeText(this, "请先登陆", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (id == R.id.recommend) {
            findViewById(R.id.tabLayout).setVisibility(View.GONE);
            findViewById(R.id.viewPager).setVisibility(View.GONE);
            showSearchButton = false;
            actionBar.setTitle("新书推荐");
            Fragment hotMessageFragment = new HotMessageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("str",str);
            bundle.putString("myLibrary",hotMessage);
            hotMessageFragment.setArguments(bundle);
            transaction.replace(R.id.content, hotMessageFragment, "FinishTag").commit();
        }else  if (id == R.id.rank){
            findViewById(R.id.tabLayout).setVisibility(View.GONE);
            findViewById(R.id.viewPager).setVisibility(View.GONE);
            showSearchButton = false;
            actionBar.setTitle("借阅排行");
            Fragment rankFragment = new RankFragment();
            transaction.replace(R.id.content, rankFragment, "FinishTag").commit();
        } else if (id == R.id.info) {
            InfoDialogFragment infoDialogFragment = new InfoDialogFragment();
            infoDialogFragment.show(fragmentManager, "MainActivity");
        } else if (id == R.id.feed_back) {
            FeedBackDialogFragment feedBackDialogFragment = new FeedBackDialogFragment();
            feedBackDialogFragment.show(fragmentManager, "MainActivity");
        } else if (id == R.id.help_each_other){
            Toast.makeText(MainActivity.this, "功能正在开发中,敬请期待", Toast.LENGTH_SHORT).show();
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


}
