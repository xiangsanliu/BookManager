package com.bookmanager.eidian.bookmanager.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bookmanager.eidian.bookmanager.Entities.Book;
import com.bookmanager.eidian.bookmanager.Entities.MyLibraryBook;
import com.bookmanager.eidian.bookmanager.R;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by xiang on 2016/10/12/012.
 */

public class MyLibraryBookAdapter extends RecyclerView.Adapter<MyLibraryBookAdapter.BookViewHolder> {

    private Activity activity;
    private List<MyLibraryBook> myLibraryBooks;

    public MyLibraryBookAdapter(Activity activity, List<MyLibraryBook> myLibraryBooks) {
        this.activity = activity;
        this.myLibraryBooks = myLibraryBooks;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(activity).inflate(R.layout.my_library_book_item, parent, false);
        BookViewHolder bookViewHolder = new BookViewHolder(view);
        return bookViewHolder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {

        if (myLibraryBooks.get(position).getReturned_date().equals(" ")) {
            holder.content.setText("作者：" + myLibraryBooks.get(position).getAuthor() + "\n" +
                    "书名：" + myLibraryBooks.get(position).getBookName() + "\n" +
                    "出版年份：" + myLibraryBooks.get(position).getYear() + "\n" +
                    "应还日期：" + myLibraryBooks.get(position).getShould_date() + "\n" +
                    "分馆：" + myLibraryBooks.get(position).getLib());
        } else {
            holder.content.setText("作者：" + myLibraryBooks.get(position).getAuthor() + "\n" +
                    "书名：" + myLibraryBooks.get(position).getBookName() + "\n" +
                    "出版年份：" + myLibraryBooks.get(position).getYear() + "\n" +
                    "应还日期：" + myLibraryBooks.get(position).getShould_date() + "\n" +
                    "归还日期：" + myLibraryBooks.get(position).getReturned_date() + "\n" +
                    "分馆：" + myLibraryBooks.get(position).getLib());
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return myLibraryBooks.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView content;

        public BookViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.my_library_book);
            content = (TextView) itemView.findViewById(R.id.my_library_text);
        }
    }
}
