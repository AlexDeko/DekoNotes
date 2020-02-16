package com.app.dekonotes;

import android.app.Application;

import androidx.room.Room;

import com.app.dekonotes.data.AppDatabase;
import com.app.dekonotes.key.HashedKeyStore;
import com.app.dekonotes.key.KeyStore;

public class App extends Application {
    public static App instance;
    private AppDatabase database;
    private KeyStore keyStore;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .build();
        keyStore = new HashedKeyStore(this);
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }
}
