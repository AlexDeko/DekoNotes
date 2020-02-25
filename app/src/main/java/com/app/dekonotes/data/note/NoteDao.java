package com.app.dekonotes.data.note;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note ORDER BY contains_deadline DESC, deadline ASC, last_change DESC")
    Observable<List<Note>> getAll();

    @Query("SELECT * FROM note WHERE id = :id")
    Single<Note> getNoteById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insertNote(Note note);

    @Update
    Completable update(Note note);

    @Delete
    Completable delete(Note note);
}