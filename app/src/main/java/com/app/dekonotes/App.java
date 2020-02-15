package com.app.dekonotes;

import android.app.Application;

import androidx.room.Room;

import com.app.dekonotes.data.AppDatabase;
import com.app.dekonotes.data.AppSharedPreferences;
import com.app.dekonotes.key.KeyStore;

public class App extends Application {
    public static App instance;
    private AppDatabase database;
    private AppDatabase databaseKey;
    private AppSharedPreferences appSharedPreferences;
    private KeyStore keyDao;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .build();

        databaseKey = Room.databaseBuilder(this, AppDatabase.class, "databaseKey")
                .build();
//        appSharedPreferences
//        appSharedPreferences  = new AppSharedPreferences() {
//            @Override
//            public KeyStore keyDao() {
//                return null;
//            }

        keyDao = new AppSharedPreferences();

//        appSharedPreferences = new KeyStore() {
//            @Override
//            public boolean hasPin() {
//                return false;
//            }
//
//            @Override
//            public boolean checkPin(String pin) {
//                return false;
//            }
//
//            @Override
//            public void saveNew(String pin) {
//
//            }
//        };

    }





        //keyDao = new KeyStore() {
//            @Override
//            public boolean hasPin() {
//                return false;
//            }
//
//            @Override
//            public boolean checkPin(String pin) {
//                return false;
//            }
//
//            @Override
//            public Comparable saveNew(String pin) {
//                return null;
//            }
//        };
      //  keyDao = getSharedDatabaseKey();

  //  }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public AppDatabase getDatabaseKey() {
        return databaseKey;
    }

    public AppSharedPreferences getSharedDatabaseKey() {
        return appSharedPreferences;
    }

//    public KeyStore getSharedDatabaseKey() {
 //       return keyDao;
  //  }
}
