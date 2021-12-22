package com.crazyiter.nanonet;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.crazyiter.nanonet.db.Admin;
import com.crazyiter.nanonet.db.Provider;
import com.crazyiter.nanonet.db.ProviderManager;
import com.google.android.material.textfield.TextInputLayout;

public class AddProviderActivity extends AppCompatActivity {

    private TextInputLayout passwordTIL, nameTIL, emailTIL;
    private Provider provider;
    private ProviderManager providerManager;
    private TextInputLayout dateTIL;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_provider);

        loadingDialog = new LoadingDialog(this);
        providerManager = new ProviderManager(this);

        nameTIL = findViewById(R.id.nameTIL);
        emailTIL = findViewById(R.id.emailTIL);
        passwordTIL = findViewById(R.id.passwordTIL);
        Button saveBTN = findViewById(R.id.saveBTN);

        Spinner countSP = findViewById(R.id.countSP);
        Spinner typeSP = findViewById(R.id.typeSP);
        dateTIL = findViewById(R.id.dateTIL);

        String[] count = new String[30];
        for (int i = 0; i < 30; i++) {
            count[i] = String.valueOf(i + 1);
        }
        countSP.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, count));
        typeSP.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{getString(R.string.day), getString(R.string.month), getString(R.string.year)}));

        dateTIL.getEditText().setText(Statics.LocalDate.getCurrentDate());
        dateTIL.setStartIconOnClickListener(v -> Statics.LocalDate.getDate(this, date -> dateTIL.getEditText().setText(date)));

        try {
            provider = (Provider) getIntent().getSerializableExtra("item");
            nameTIL.getEditText().setText(provider.getName());
            emailTIL.getEditText().setText(provider.getEmail());
            passwordTIL.getEditText().setText(provider.getPassword());
            dateTIL.getEditText().setText(provider.getStartDate());
            typeSP.setSelection(provider.getType());
            countSP.setSelection(provider.getCount() - 1);
        } catch (Exception ignored) {
        }

        saveBTN.setOnClickListener(v -> {
            boolean b =
                    Statics.checkInput(nameTIL, getString(R.string.enter_name)) &&
                            Statics.checkInput(passwordTIL, getString(R.string.enter_password)) &&
                            Statics.checkInput(emailTIL, getString(R.string.enter_email), true) &&
                            Statics.checkInput(dateTIL, getString(R.string.choose_date));

            if (b) {
                String password = passwordTIL.getEditText().getText().toString().trim();
                String email = emailTIL.getEditText().getText().toString().trim();
                String name = nameTIL.getEditText().getText().toString().trim();
                String startDate = dateTIL.getEditText().getText().toString().trim();

                if (provider == null) {
                    Provider newTemp = new Provider("", name, password, email, startDate, typeSP.getSelectedItemPosition(), countSP.getSelectedItemPosition() + 1, Admin.getShared(this).getId());
                    if (ProviderManager.existCount(newTemp) == 0) {
                        loadingDialog.show();
                        providerManager.addProvider(newTemp, provider -> {
                            loadingDialog.dismiss();
                            finish();
                        });
                    } else {
                        Toast.makeText(this, getString(R.string.exist), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    provider.setName(name);
                    provider.setEmail(email);
                    provider.setPassword(password);
                    provider.setStartDate(startDate);
                    provider.setType(typeSP.getSelectedItemPosition());
                    provider.setCount(countSP.getSelectedItemPosition() + 1);
                    if (ProviderManager.existCount(provider) == 0) {
                        providerManager.updateProvider(provider);
                        finish();
                    } else {
                        Toast.makeText(this, getString(R.string.exist), Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
    }
}