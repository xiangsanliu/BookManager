package com.bookmanager.eidian.bookmanager.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.bookmanager.eidian.bookmanager.Activities.SearchBookActivity;
import com.bookmanager.eidian.bookmanager.Entities.Book;
import com.bookmanager.eidian.bookmanager.R;

import java.util.List;

/**
 * Created by clmiberf on 2016/9/21.
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.SearchBookViewHolder> {

    private List<Book> list;

    public BookAdapter(List<Book> objects) {
        this.list = objects;
    }


    @Override
    public SearchBookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        SearchBookViewHolder viewHolder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_view, parent, false);
        viewHolder = new SearchBookViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SearchBookViewHolder holder, int position) {
        Book book = list.get(position);
        holder.bName.setText(book.getBookName());
        holder.bAuthor.setText(book.getAuthor());
        holder.bPublisher.setText(book.getPublisher());
        holder.bPublishTime.setText(book.getPublishTime());
        holder.bCallNumber.setText(book.getCallNumber());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SearchBookViewHolder extends RecyclerView.ViewHolder{
        TextView bName;
        TextView bAuthor;
        TextView bPublisher;
        TextView bPublishTime;
        TextView bCallNumber;

        public SearchBookViewHolder(View itemView) {
            super(itemView);

            bName = (TextView) itemView.findViewById(R.id.tx_name);
            bAuthor = (TextView) itemView.findViewById(R.id.tx_author);
            bPublisher = (TextView) itemView.findViewById(R.id.tx_information);
            bPublishTime = (TextView) itemView.findViewById(R.id.tx_time);
            bCallNumber = (TextView) itemView.findViewById(R.id.tx_callNumber);
        }
    }
}
