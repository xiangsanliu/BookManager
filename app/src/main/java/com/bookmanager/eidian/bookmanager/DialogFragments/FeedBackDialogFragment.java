package com.bookmanager.eidian.bookmanager.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by xiang on 2016/9/18/018.
 */
public class FeedBackDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("如果您有什么建议，欢迎反馈给我们！")
                .setNegativeButton("好的", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setPositiveButton("发送反馈信息", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        // i.setType("text/plain"); //模拟器请使用这行
                        i.setType("message/rfc822"); // 真机上使用这行
                        i.putExtra(Intent.EXTRA_EMAIL,
                                new String[] { "xiangjianjian@hotmail.com" });
                        i.putExtra(Intent.EXTRA_SUBJECT, "来自华农图书馆的反馈建议");
                        startActivity(Intent.createChooser(i,
                                "Select email application."));
                    }
                })
                .setTitle("反馈");
        return builder.create();
    }
}
