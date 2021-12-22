package com.crazyiter.nanonet.db;

import com.crazyiter.nanonet.R;
import com.crazyiter.nanonet.Statics;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Provider implements Serializable {
    private String id;
    private String name;
    private String password;
    private String email;
    private String startDate;
    private int type;
    private int count;
    private boolean isOnline;
    private String adminId;

    private boolean isActive = true;

    private boolean renewAlert = false;
    private boolean payAlert = false;
    private boolean amountAlert = false;

    public Provider(String id, String name, String password, String email, String startDate, int type, int count, String adminId) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.startDate = startDate;
        this.type = type;
        this.count = count;
        this.adminId = adminId;
    }

    public Provider(String id, String name, String password, String email, String startDate, int type, int count, boolean isOnline, String adminId) {
        this(id, name, password, email, startDate, type, count, adminId);
        this.isOnline = isOnline;
    }

    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("password", password);
        map.put("email", email);
        map.put("startDate", startDate);
        map.put("type", type);
        map.put("count", count);
        map.put("isOnline", isOnline);
        map.put("adminId", adminId);

        map.put("isActive", isActive);
        map.put("renewAlert", renewAlert);
        map.put("payAlert", payAlert);
        map.put("amountAlert", amountAlert);

        return map;
    }

    public int getDiffDays() {
        return Statics.LocalDate.getDifferenceDays(Statics.LocalDate.getCurrentDate(), getEndDate());
    }

    public int getRemainsHours() {
        String d = Statics.LocalDate.getCurrentDate();
        String d2 = getEndDate();

        return Statics.LocalDate.getDifferenceHours(d, d2);
    }

    public int getDiffDaysColor() {
        int dif = getDiffDays();
        if (dif > 3) {
            return R.color.colorGreen;
        }

        if (dif > 0) {
            return R.color.colorYellow;
        }

        return R.color.colorRed;
    }

    public boolean search(String s) {
        return name.contains(s);
    }

    public boolean isEquals(Provider p) {
        return name.equals(p.getName()) || email.equals(p.getEmail());
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getEndDate() {
        return Statics.LocalDate.getEndDate(startDate, type, count);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
