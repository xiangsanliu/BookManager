package com.bookmanager.eidian.bookmanager.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookmanager.eidian.bookmanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchBookFragment extends Fragment {


    public SearchBookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_book, container, false);
    }

}
