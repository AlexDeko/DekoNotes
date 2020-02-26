package com.app.dekonotes;

import android.app.Application;

import androidx.room.Room;

import com.app.dekonotes.data.AppDatabase;
import com.app.dekonotes.data.key.HashedKeyStore;
import com.app.dekonotes.data.key.KeyStore;
import com.app.dekonotes.data.note.RepositoryNotes;
import com.app.dekonotes.data.note.RepositoryNotesImpl;

public class App extends Application {
    public static App instance;
    private KeyStore keyStore;
    private RepositoryNotes repositoryNotes;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .build();
        keyStore = new HashedKeyStore(this);
        repositoryNotes = new RepositoryNotesImpl(database.noteDao());

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
}
