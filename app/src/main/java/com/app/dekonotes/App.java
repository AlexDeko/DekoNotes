package com.app.dekonotes;

import android.app.Application;

import androidx.room.Room;

import com.app.dekonotes.data.AppDatabase;
import com.app.dekonotes.data.key.HashedKeyStore;
import com.app.dekonotes.data.key.KeyStore;
import com.app.dekonotes.data.lifedata.LifeData;
import com.app.dekonotes.data.lifedata.LifeDataImp;
import com.app.dekonotes.data.note.RepositoryNotes;
import com.app.dekonotes.data.note.RepositoryNotesImpl;

public class App extends Application {
    public static App instance;
    private AppDatabase database;
    private KeyStore keyStore;
    private RepositoryNotes repositoryNotes;
    private LifeData lifeData;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .build();
        keyStore = new HashedKeyStore(this);
        repositoryNotes = new RepositoryNotesImpl(database.noteDao());
        lifeData = new LifeDataImp();

    }

    public static App getInstance() {
        return instance;
    }

    public RepositoryNotes getRepositoryNotes() {
        return repositoryNotes;
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }

    public LifeData getLifeData() {
        return lifeData;
    }
}
