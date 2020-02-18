package com.app.dekonotes.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.app.dekonotes.data.note.Note;
import com.app.dekonotes.data.note.NoteDao;



@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
}
