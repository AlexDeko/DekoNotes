package com.homework1_3.dekonotes.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.homework1_3.dekonotes.note.Note;
import com.homework1_3.dekonotes.note.NoteDao;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
}
