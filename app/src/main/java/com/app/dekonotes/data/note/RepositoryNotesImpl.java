package com.app.dekonotes.data.note;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class RepositoryNotesImpl implements RepositoryNotes {

    private NoteDao noteDao;

    public RepositoryNotesImpl(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Override
    public Single<Note> getById(long idNoteBundle) {
        return noteDao.getNoteById(idNoteBundle)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable update(Note myNote) {
        return noteDao.update(myNote)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Note>> getAll() {
        return noteDao.getAll()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable delete(Note note) {
        return noteDao.delete(note)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Long> insert(Note myNote) {
        return noteDao.insertNote(myNote)
                .subscribeOn(Schedulers.io());
    }
}
