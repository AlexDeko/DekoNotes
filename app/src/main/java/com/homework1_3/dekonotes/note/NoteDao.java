package com.homework1_3.dekonotes.note;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note")
    List<Note> getAll();
    @Query("SELECT * FROM note WHERE id = :id")
    Note getNoteById(long id);
    @Insert
    void insertNote(Note note);
    @Update
    void update(Note note);
    @Delete
    void delete(Note note);
}

