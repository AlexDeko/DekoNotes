package com.app.dekonotes.data;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.dekonotes.App;
import com.app.dekonotes.R;
import com.app.dekonotes.activity.MainActivity;
import com.app.dekonotes.activity.NotesActivity;
import com.app.dekonotes.note.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class RepositoryNotes {

    private AppDatabase appDatabase = App.getInstance().getDatabase();

    public RepositoryNotes()  {

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

    public List<Note> getAll(){
//        final List<Note>[] notes = new List[]{};
//        try {
//            appDatabase.noteDao().getAll()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<List<Note>>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                            // Disposable представляет собой интерфейс для работы с подпиской. Через него можно отписаться
//                            //compositeDisposable.add(d);
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
        return null;
    }

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
