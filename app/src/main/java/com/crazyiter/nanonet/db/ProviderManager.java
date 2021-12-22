package com.crazyiter.nanonet.db;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class ProviderManager {

    private final Context context;
    private final String col = "providers";
    public static final ArrayList<Provider> providers = new ArrayList<>();

    public ProviderManager(Context context) {
        this.context = context;
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean isOffline() {
        return !isOnline();
    }

    public void getProviders(String adminId, ProviderListener providerListener) {
        FirebaseFirestore
                .getInstance()
                .collection(col)
                .whereEqualTo("adminId", adminId)
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        providers.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            Provider provider = new Provider(
                                    doc.getId(),
                                    doc.getString("name"),
                                    doc.getString("password"),
                                    doc.getString("email"),
                                    doc.getString("startDate"),
                                    Integer.parseInt(String.valueOf(doc.get("type"))),
                                    Integer.parseInt(String.valueOf(doc.get("count"))),
                                    doc.getBoolean("isOnline"),
                                    doc.getString("adminId")
                            );

                            provider.setActive(doc.getBoolean("isActive"));
                            providers.add(provider);
                        }
                        providerListener.onGetProviders(providers);
                    } else {
                        providerListener.onGetProviders(null);
                    }
                });
    }

    public void getProvider(Provider provider, SingleProviderListener providerListener) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(provider.getId())
                .addSnapshotListener((doc, error) -> {
                    try {
                        if (doc != null) {
                            Provider p = new Provider(
                                    doc.getId(),
                                    doc.getString("name"),
                                    doc.getString("password"),
                                    doc.getString("email"),
                                    doc.getString("startDate"),
                                    Integer.parseInt(String.valueOf(doc.get("type"))),
                                    Integer.parseInt(String.valueOf(doc.get("count"))),
                                    doc.getBoolean("isOnline"),
                                    doc.getString("adminId")
                            );
                            p.setActive(doc.getBoolean("isActive"));
                            providerListener.onGetProviderInfo(p);
                        } else {
                            providerListener.onGetProviderInfo(null);
                        }
                    } catch (Exception e) {
                        providerListener.onGetProviderInfo(null);
                    }
                });
    }

    public void addProvider(Provider provider, ProviderInsert insert) {
        FirebaseFirestore.getInstance().collection(col)
                .add(provider.getMap())
                .addOnSuccessListener(documentReference -> {
                    provider.setId(documentReference.getId());
                    insert.onInsert(provider);
                });
        if (isOffline()) {
            insert.onInsert(provider);
        }
    }

    public void deleteProvider(Provider provider) {
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(provider.getId())
                .delete();

        FirebaseFirestore.getInstance()
                .collection("customers")
                .whereEqualTo("providerId", provider.getId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        FirebaseFirestore.getInstance()
                                .collection("customers")
                                .document(doc.getId())
                                .delete();
                    }
                });
    }

    public void updateProvider(Provider provider) {
        Map<String, Object> map = provider.getMap();
        map.remove("isOnline");
        map.remove("renewAlert");
        map.remove("payAlert");
        map.remove("amountAlert");
        FirebaseFirestore.getInstance()
                .collection(col)
                .document(provider.getId())
                .update(map);
    }

    public static int existCount(Provider provider) {
        int s = 0;
        for (int i = 0; i < providers.size(); i++) {
            if (provider.getId().equals(providers.get(i).getId())) {
                continue;
            }

            if (provider.isEquals(providers.get(i))) {
                s++;
            }
        }
        return s;
    }

    public int[] getStatuesCount() {
        int[] difs = new int[]{0, 0, 0};
        for (int i = 0; i < providers.size(); i++) {
            int dif = providers.get(i).getDiffDays();

            if (dif > 3) {
                difs[0]++;
                continue;
            }

            if (dif > 0) {
                difs[1]++;
                continue;
            }

            difs[2]++;
        }
        return difs;
    }

    public interface ProviderListener {
        void onGetProviders(ArrayList<Provider> providers);
    }

    public interface SingleProviderListener {
        void onGetProviderInfo(Provider provider);
    }

    public interface ProviderInsert {
        void onInsert(Provider provider);
    }

    public interface ProviderUpdate {
        void onUpdate(Provider provider);
    }

    public interface ProviderDelete {
        void onDelete(boolean isDeleted);
    }

}
