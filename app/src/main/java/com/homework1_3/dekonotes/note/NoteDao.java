package com.homework1_3.dekonotes.note;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface NoteDao {
   // Flowable
    @Query("SELECT * FROM note")
    List<Note> getAll();
    @Query("SELECT * FROM note WHERE id = :id")
    Note getNoteById(long id);
   // Completable
    @Insert
    void insertNote(Note note);
    @Update
    void update(Note note);
    @Delete
    void delete(Note note);
}

