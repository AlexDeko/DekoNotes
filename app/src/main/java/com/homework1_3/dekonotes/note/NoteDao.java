package com.homework1_3.dekonotes.note;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface NoteDao {
   // Flowable
    @Query("SELECT * FROM note")
    Maybe<List<Note>> getAll();
    @Query("SELECT * FROM note WHERE id = :id")
    Single<Note> getNoteById(long id);
   // Completable
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertNote(Note note);
    @Update
    Completable update(Note note);
    @Delete
    Completable delete(Note note);
}

