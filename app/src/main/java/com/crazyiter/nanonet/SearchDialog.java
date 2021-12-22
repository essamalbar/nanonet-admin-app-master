package com.crazyiter.nanonet;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

public class SearchDialog extends Dialog {

    private TextInputLayout searchTIL;
    private final SearchListener searchListener;
    private final String s;

    public SearchDialog(@NonNull Context context, String s, SearchListener searchListener) {
        super(context);
        this.searchListener = searchListener;
        this.s = s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_search);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        searchTIL = findViewById(R.id.searchTIL);
        searchTIL.getEditText().setText(s);

        Button searchBTN = findViewById(R.id.searchBTN);
        searchBTN.setOnClickListener(v -> {
            searchListener.onClickSearchButtonListener(searchTIL.getEditText().getText().toString().trim());
            dismiss();
        });
    }

    public interface SearchListener {
        void onClickSearchButtonListener(String s);
    }

}
