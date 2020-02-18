package com.app.dekonotes.data.note;

import android.provider.ContactsContract;

import com.app.dekonotes.App;
import com.app.dekonotes.data.AppDatabase;
import com.app.dekonotes.data.note.Note;


import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class RepositoryNotesImplEx {

    private AppDatabase appDatabase = App.getInstance().getDatabase();

    public RepositoryNotesImplEx()  {

    }

    public Note getById(long idNoteBundle){
        final Note[] myNote = new Note[1];
        appDatabase.noteDao().getNoteById(idNoteBundle)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Note>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Note note) {
                        myNote[0] = note;

                    }

                    @Override
                    public void onError(Throwable t) {

                    }
                });
        return  myNote[0];
    }

    public void update(Note myNote){
        try {
            appDatabase.noteDao().update(myNote)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {

                        }
                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

//    public List<Note> getAll(Disposable disposable){
//        final List<Note>[] notes = new List[]{};
//        try {
//            appDatabase.noteDao().getAll()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<List<Note>>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                            // Disposable представляет собой интерфейс для работы с подпиской. Через него можно отписаться
//                            compositeDisposable.add(d);
//                        }
//
//                        @Override
//                        public void onNext(List<Note> note) {
//                            notes[0] = note;
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                        }
//
//                        @Override
//                        public void onComplete() {
//                        }
//                    });
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    public void delete(Note note){
        appDatabase.noteDao().delete(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                      //  Log.i(LOG, "Удалена заметка");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public void insert(Note myNote){
        appDatabase.noteDao().insertNote(myNote)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }
}
