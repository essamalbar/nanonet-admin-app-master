package com.crazyiter.nanonet;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

public class LoadingDialog extends Dialog {

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

}
