package com.app.dekonotes.data;

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

    AppDatabase appDatabase = App.getInstance().getDatabase();


    private Note getById(long idNoteBundle){
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

    private void update(long idNoteBundle, String titleNote, String textNote,
                        boolean checkDeadline, long date, long lastChange, int containsDeadline){
        try {
            Note myNote = new Note(idNoteBundle, titleNote, textNote, checkDeadline,
                    date, lastChange, containsDeadline);
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

    private List<Note> getAll(){
        final List<Note>[] notes = new List[]{};
        appDatabase.noteDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Note>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // Disposable представляет собой интерфейс для работы с подпиской. Через него можно отписаться
                        //compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<Note> note) {
                        notes[0] = note;

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
        return notes[0];
    }

    private void delete(Note note){
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

    private void insert(Note myNote){
        appDatabase.noteDao().insertNote(myNote)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                      //  Log.i(TAG, "Новая заметка");
                        //Toast.makeText(this,
                         //       getString(R.string.toast_database_save),
                        //        Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }
}
