package com.crazyiter.nanonet;

import androidx.multidex.MultiDexApplication;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseFirestoreSettings firebaseFirestoreSettings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();

        FirebaseFirestore.getInstance().setFirestoreSettings(firebaseFirestoreSettings);

    }
}