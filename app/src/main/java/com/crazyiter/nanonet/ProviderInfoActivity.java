package com.crazyiter.nanonet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.crazyiter.nanonet.db.Provider;
import com.crazyiter.nanonet.db.ProviderManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProviderInfoActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView nameTV, emailTV, startTV, endTV;
    private Provider provider;
    private ProviderManager providerManager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_info);

        providerManager = new ProviderManager(this);

        nameTV = findViewById(R.id.nameTV);
        emailTV = findViewById(R.id.emailTV);
        startTV = findViewById(R.id.startDateTV);
        endTV = findViewById(R.id.endDateTV);

        LinearLayout emailLL = findViewById(R.id.emailLL);
        emailLL.setOnClickListener(v -> Statics.sendEmail(this, provider.getEmail()));

        bottomNavigationView = findViewById(R.id.providerBNV);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        setupData();
    }

    @SuppressLint("SetTextI18n")
    private void setupData() {
        provider = (Provider) getIntent().getSerializableExtra("item");
        assert provider != null;
        providerManager.getProvider(provider, p -> {
            if (p == null) {
                finish();
            } else {
                provider = p;
            }

            nameTV.setText(provider.getName());
            emailTV.setText(provider.getEmail());
            startTV.setText(provider.getStartDate());

            MenuItem item = bottomNavigationView.getMenu().findItem(R.id.stop);
            if (provider.isActive()) {
                item.setTitle("تعطيل");
                item.setIcon(R.drawable.ic_round_pause);
            } else {
                item.setTitle("تفعيل");
                item.setIcon(R.drawable.ic_round_play_arrow);
            }

            int dif = provider.getDiffDaysColor();
            endTV.setTextColor(getResources().getColor(dif));
            if (provider.getDiffDays() == 0 && provider.getRemainsHours() == 0) {
                endTV.setText(getString(R.string.expired));
            } else {
                endTV.setText(getString(R.string.remain) + "\n" + provider.getDiffDays() + " يوم " + provider.getRemainsHours() + " ساعة");
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                startActivity(new Intent(this, AddProviderActivity.class)
                        .putExtra("item", provider));
                return true;

            case R.id.renew:
                new ReNewDialog(this, provider).show();
                return true;

            case R.id.stop:
                String msg;
                if (provider.isActive()) msg = getString(R.string.sure_stop);
                else msg = getString(R.string.sure_active);

                new AlertDialog.Builder(this)
                        .setMessage(msg)
                        .setNegativeButton(getString(R.string.cancel), null)
                        .setPositiveButton(getString(R.string.confirm), (dialog, which) -> {
                            provider.setActive(!provider.isActive());
                            providerManager.updateProvider(provider);
                        }).create().show();
                return true;

            case R.id.delete:
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.sure_delete))
                        .setNegativeButton(getString(R.string.cancel), null)
                        .setPositiveButton(getString(R.string.confirm), (dialog, which) -> {
                            providerManager.deleteProvider(provider);
                            finish();
                        }).create().show();
                return true;
        }
        return false;
    }
}