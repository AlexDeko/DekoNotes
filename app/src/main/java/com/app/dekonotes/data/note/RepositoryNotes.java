package com.app.dekonotes.data.note;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface RepositoryNotes {

    Single<Note> getById(long idNoteBundle);

    Completable update(Note myNote);

    Observable<List<Note>> getAll();

    Completable delete(Note note);

    Completable insert(Note myNote);
}
