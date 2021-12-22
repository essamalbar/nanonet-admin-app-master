package com.crazyiter.nanonet.db;

import android.util.Log;

import com.crazyiter.nanonet.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AdminManager {

    public static void login(String username, String password, AdminListener adminListener) {
        Query query = FirebaseFirestore.getInstance().collection("admins");

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                for (QueryDocumentSnapshot doc : task.getResult()) {

                    boolean u = username.equals(doc.getString("username"));
                    boolean p = password.equals(doc.getString("password"));

                    if (u && p) {
                        int order;
                        try {
                            order = Integer.parseInt(String.valueOf(doc.get("orderBy")));
                        } catch (Exception e) {
                            Log.e("order", e.getMessage());
                            order = R.id.nameRB;
                        }
                        adminListener.login(new Admin(
                                doc.getId(),
                                doc.getString("name"),
                                doc.getString("username"),
                                order
                        ));
                        return;
                    }
                }
            }
            adminListener.login(null);
        });
    }

    public void updateAdmin(Admin admin) {
        FirebaseFirestore.getInstance()
                .collection("admins")
                .document(admin.getId())
                .update(admin.getMap());
    }

    public interface AdminListener {
        void login(Admin admin);
    }

}

