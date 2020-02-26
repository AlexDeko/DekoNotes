package com.app.dekonotes.data.note;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.app.dekonotes.App;
import com.app.dekonotes.data.lifedata.LifeData;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Iterator;
import java.util.Observable;

import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NoteViewModel extends ViewModel {

    private LifeData lifeData = App.getInstance().getLifeData();
    private MutableLiveData<Long> noteId;
    private Flowable<Long> longFlowable;
    private Long aLong;

    public LiveData<Long> getNoteId() {
        if (noteId == null) {
            noteId = new MutableLiveData<>();

            loadNoteId();
        }
        return noteId;
    }

    private void loadNoteId() {

        longFlowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new FlowableSubscriber<Long>() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(Long aLong) {
                noteId.setValue(lifeData.getValue());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public NoteViewModel() {
        super();
    }
}
