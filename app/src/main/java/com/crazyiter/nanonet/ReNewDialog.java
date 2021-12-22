package com.crazyiter.nanonet;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.crazyiter.nanonet.db.Provider;
import com.crazyiter.nanonet.db.ProviderManager;
import com.google.android.material.textfield.TextInputLayout;

public class ReNewDialog extends Dialog {

    private Spinner countSP, typeSP;
    private TextInputLayout dateTIL;
    private final Provider provider;

    public ReNewDialog(@NonNull Context context, Provider provider) {
        super(context);
        this.provider = provider;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_renew);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        countSP = findViewById(R.id.countSP);
        typeSP = findViewById(R.id.typeSP);
        dateTIL = findViewById(R.id.dateTIL);
        Button saveBTN = findViewById(R.id.saveBTN);

        String[] count = new String[30];
        for (int i = 0; i < 30; i++) {
            count[i] = String.valueOf(i + 1);
        }
        countSP.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, count));
        typeSP.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, new String[]{getContext().getString(R.string.day), getContext().getString(R.string.month), getContext().getString(R.string.year)}));

        dateTIL.setStartIconOnClickListener(v -> Statics.LocalDate.getDate(getContext(), date -> dateTIL.getEditText().setText(date)));
        dateTIL.getEditText().setText(provider.getStartDate());
        typeSP.setSelection(provider.getType());
        countSP.setSelection(provider.getCount() - 1);

        saveBTN.setOnClickListener(v -> {
            String date = dateTIL.getEditText().getText().toString().trim();
            if (date.isEmpty()) {
                dateTIL.setError(getContext().getString(R.string.choose_date));
                return;
            }
            dateTIL.setError("");
            provider.setStartDate(date);
            provider.setType(typeSP.getSelectedItemPosition());
            provider.setCount(countSP.getSelectedItemPosition() + 1);
            provider.setActive(true);
            new ProviderManager(getContext()).updateProvider(provider);
            dismiss();
        });

    }

}
