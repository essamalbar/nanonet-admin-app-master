package com.crazyiter.nanonet.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.crazyiter.nanonet.R;

import java.util.HashMap;
import java.util.Map;

public class Admin {
    private final String id;
    private final String name;
    private final String username;
    private int orderBy;

    public Admin(String id, String name, String username, int orderBy) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.orderBy = orderBy;
    }

    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("username", username);
        map.put("orderBy", orderBy);
        return map;
    }

    public void saveShared(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit();
        editor.putString("id", id);
        editor.putString("name", name);
        editor.putString("username", username);
        editor.putInt("order", orderBy);
        editor.putInt("type", 2);
        editor.apply();
    }

    public static Admin getShared(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        String _id = sharedPreferences.getString("id", null);
        if (_id == null) {
            return null;
        }

        return new Admin(
                _id,
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("username", null),
                sharedPreferences.getInt("order", R.id.nameRB)
        );
    }

    public static void logout(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }
}
