package com.crazyiter.nanonet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.crazyiter.nanonet.db.Admin;
import com.crazyiter.nanonet.db.AdminManager;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout usernameTIL, passwordTIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Admin.getShared(this) != null) {
            startActivity(new Intent(this, MainAdminActivity.class));
            finish();
        }

        usernameTIL = findViewById(R.id.usernameTIL);
        passwordTIL = findViewById(R.id.passwordTIL);
        Button loginBTN = findViewById(R.id.loginBTN);

        loginBTN.setOnClickListener(v -> {
            String username = usernameTIL.getEditText().getText().toString().trim();
            String password = passwordTIL.getEditText().getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.enter_username_password_error), Toast.LENGTH_SHORT).show();
                return;
            }

            LoadingDialog loadingDialog = new LoadingDialog(this);
            loadingDialog.show();
            AdminManager.login(username, password, admin -> {
                loadingDialog.dismiss();
                if (admin == null) {
                    Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                } else {
                    admin.saveShared(this);
                    startActivity(new Intent(this, MainAdminActivity.class));
                    finish();
                }
            });
        });

    }
}