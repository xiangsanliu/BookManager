package com.bookmanager.eidian.bookmanager.DialogFragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

import okhttp3.OkHttpClient;

/**
 * Created by xiang on 2017/1/17.
 */

public class SouthLakeDialogFragment extends DialogFragment {

    String message;

    SharedPreferences sharedPreferences;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
        String meesage = bundle.getString("message");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(meesage);
        return  builder.create();
    }

    private class SouthLakeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
//            switch ()
        }
    }
}
