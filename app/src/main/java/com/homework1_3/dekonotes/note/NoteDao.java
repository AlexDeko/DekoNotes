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

    @Query("SELECT * FROM note")
    Flowable<List<Note>> getAll();
    @Query("SELECT * FROM note WHERE id = :id")
    Flowable<Note> getNoteById(long id);
    @Insert
    Completable insertNote(Note note);
    @Update
    Completable update(Note note);
    @Delete
    Completable delete(Note note);
}

