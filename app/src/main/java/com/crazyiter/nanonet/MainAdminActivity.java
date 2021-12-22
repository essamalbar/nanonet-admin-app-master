package com.crazyiter.nanonet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyiter.nanonet.db.Admin;
import com.crazyiter.nanonet.db.AdminManager;
import com.crazyiter.nanonet.db.Provider;
import com.crazyiter.nanonet.db.ProviderManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MainAdminActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView providersRV;
    private TextView noTV;
    private TextInputLayout searchTIL;
    private ProviderManager providerManager;
    private DrawerLayout mainDL;
    private TextView activeTV;
    private TextView warningTV;
    private TextView expireTV;
    private RadioGroup orderRG;
    private Admin admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        providerManager = new ProviderManager(this);
        mainDL = findViewById(R.id.mainDL);
        NavigationView mainNV = findViewById(R.id.mainNV);
        View navHeaderView = mainNV.getHeaderView(0);

        noTV = findViewById(R.id.noTV);
        searchTIL = findViewById(R.id.searchTIL);

        TextView nameTV = navHeaderView.findViewById(R.id.nameTV);
        activeTV = navHeaderView.findViewById(R.id.activeTV);
        warningTV = navHeaderView.findViewById(R.id.warningTV);
        expireTV = navHeaderView.findViewById(R.id.expireTV);
        orderRG = navHeaderView.findViewById(R.id.orderRG);
        LinearLayout logoutLL = navHeaderView.findViewById(R.id.logoutLL);
        logoutLL.setOnClickListener(this);

        admin = Admin.getShared(this);
        nameTV.setText(admin.getName());
        orderRG.check(admin.getOrderBy());
        orderRG.setOnCheckedChangeListener((group, checkedId) -> {
            saveOrderByChecked(checkedId);
            mainDL.closeDrawer(GravityCompat.START);
            showData();
        });

        providersRV = findViewById(R.id.providersRV);
        providersRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ImageView searchIV = findViewById(R.id.searchIV);
        ImageView menuIV = findViewById(R.id.menuIV);
        FloatingActionButton addFAB = findViewById(R.id.mainFAB);
        searchIV.setOnClickListener(this);
        menuIV.setOnClickListener(this);
        addFAB.setOnClickListener(this);

        providersRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && addFAB.getVisibility() == View.VISIBLE) {
                    addFAB.hide();
                } else if (dy < 0 && addFAB.getVisibility() != View.VISIBLE) {
                    addFAB.show();
                }
            }
        });

        Objects.requireNonNull(searchTIL.getEditText()).setOnEditorActionListener((v, actionId, event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchTIL.getEditText().getWindowToken(), 0);
            showData();
            return false;
        });

        setupDate();
    }

    private void saveOrderByChecked(int checkedId) {
        admin.setOrderBy(checkedId);
        admin.saveShared(this);
        new AdminManager().updateAdmin(admin);
    }

    private void setAdapter(ArrayList<Provider> temp) {
        Collections.sort(temp, (o1, o2) -> {
            int i = orderRG.getCheckedRadioButtonId();
            if (i == R.id.nameRB) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }

            return String.valueOf(o1.getDiffDays()).compareTo(String.valueOf(o2.getDiffDays()));
        });

        providersRV.setAdapter(new ProvidersAdapter(this, temp));
        if (temp.isEmpty()) {
            noTV.setVisibility(View.VISIBLE);
        } else {
            noTV.setVisibility(View.GONE);
        }
    }

    private void setupDate() {
        providerManager.getProviders(admin.getId(), providers -> {
            try {
                if (searchTIL.getVisibility() == View.GONE) {
                    setAdapter(providers);
                }
                int[] dif = providerManager.getStatuesCount();
                activeTV.setText(String.valueOf(dif[0]));
                warningTV.setText(String.valueOf(dif[1]));
                expireTV.setText(String.valueOf(dif[2]));
            } catch (Exception ignored) {
            }
        });
    }

    private void showData() {
        String s = searchTIL.getEditText().getText().toString().trim();
        if (s.isEmpty()) {
            searchTIL.setVisibility(View.GONE);
        } else {
            searchTIL.setVisibility(View.VISIBLE);
        }

        ArrayList<Provider> temp = new ArrayList<>();
        for (Provider p : ProviderManager.providers) {
            if (p.search(s)) {
                temp.add(p);
            }
        }

        setAdapter(temp);
    }

    @Override
    public void onBackPressed() {
        if (mainDL.isDrawerOpen(GravityCompat.START)) {
            mainDL.closeDrawer(GravityCompat.START);
            return;
        }

        if (searchTIL.getVisibility() == View.VISIBLE) {
            searchTIL.getEditText().setText("");
            showData();
            return;
        }
        super.onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuIV:
                mainDL.openDrawer(GravityCompat.START);
                break;

            case R.id.mainFAB:
                startActivity(new Intent(this, AddProviderActivity.class));
                break;

            case R.id.searchIV:
                searchTIL.setVisibility(View.VISIBLE);
                searchTIL.getEditText().requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchTIL.getEditText(), InputMethodManager.SHOW_IMPLICIT);
                break;

            case R.id.logoutLL:
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.sure_logout))
                        .setNegativeButton(getString(R.string.cancel), null)
                        .setPositiveButton(getString(R.string.confirm), (dialog, which) -> {
                            Admin.logout(this);
                            FirebaseFirestore.getInstance().clearPersistence();
                            startActivity(new Intent(this, LoginActivity.class));
                            finish();
                        }).create().show();
                break;
        }
    }

}