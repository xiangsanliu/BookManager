package com.bookmanager.eidian.bookmanager.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bookmanager.eidian.bookmanager.Adapters.BmobQueryBookAdapter;
import com.bookmanager.eidian.bookmanager.Entities.ReaderBook;
import com.bookmanager.eidian.bookmanager.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class BmobReaderQueryBookActivity extends AppCompatActivity {

    private Button addButton;

    private Button queryBookButton;

    private EditText bNameEdit;

    private List<ReaderBook> queryBookList;

    private ListView queryBookListView;

    private BmobQueryBookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bmob_reader_query_book);
        Bmob.initialize(this,"d882083bc95b77c5c072cbcc4264078d");
        addButton = (Button) findViewById(R.id.bmob_reader_query_add_button);
        queryBookButton = (Button) findViewById(R.id.bmob_query_book_button);
        bNameEdit = (EditText) findViewById(R.id.bmob_query_book_edit);
        queryBookListView = (ListView) findViewById(R.id.bmob_query_book_listview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        queryBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bName = bNameEdit.getText().toString();
                BmobQuery<ReaderBook> bmobQuery = new BmobQuery<ReaderBook>();
                //查询"bName"为bName的数据
                bmobQuery.addWhereEqualTo("bName",bName);
                //执行查询方法
                bmobQuery.findObjects(new FindListener<ReaderBook>() {
                    @Override
                    public void done(List<ReaderBook> list, BmobException e) {
                        //如果查询成功
                        if (e == null){
                            ReaderBook readerBook;
                            queryBookList = new ArrayList<ReaderBook>();
                            for (ReaderBook readerBook1 : list){
                                readerBook = new ReaderBook();
                                readerBook.setbName(readerBook1.getbName());
                                readerBook.setbAuthor(readerBook1.getbAuthor());
                                readerBook.setbOwner(readerBook1.getbOwner());
                                queryBookList.add(readerBook);
                            }
                            Log.d("BmobReaderQueryBook",queryBookList.toString());
                            adapter = new BmobQueryBookAdapter(BmobReaderQueryBookActivity.this,
                                    R.layout.item_bmob_query_book_view,queryBookList);
                            queryBookListView.setAdapter(adapter);
                        }else {
                            Toast.makeText(BmobReaderQueryBookActivity.this,"未搜索到书籍",Toast.LENGTH_SHORT).show();
                            Log.d("BomobReaderQueryBookEr",e.getMessage());
                        }
                    }
                });

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BmobReaderQueryBookActivity.this,BmobReaderAddBook.class));
            }
        });

    }
}
