package com.app.dekonotes.note;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface NoteDao {
   // Flowable
    @Query("SELECT * FROM note")
    Observable<List<Note>> getAll();
    @Query("SELECT * FROM note WHERE id = :id")
    Observable<Note> getNoteById(long id);
   // Completable
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertNote(Note note);
    @Update
    Completable update(Note note);
    @Delete
    Completable delete(Note note);
}

