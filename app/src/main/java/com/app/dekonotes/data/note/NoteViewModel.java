package com.app.dekonotes.data.note;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class NoteViewModel extends ViewModel {
    private MutableLiveData<Long> noteId;
    public LiveData<Long> getNoteId() {
        if (noteId == null) {
            noteId = new MutableLiveData<>();

            loadNoteId();
        }
        return noteId;
    }
    private void loadNoteId() {
       // noteId;
    }
}
