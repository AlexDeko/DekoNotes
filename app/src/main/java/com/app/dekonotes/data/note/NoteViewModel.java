package com.app.dekonotes.data.note;

import com.app.dekonotes.R;
import com.app.dekonotes.lifecycle.FinishEvent;
import com.app.dekonotes.lifecycle.MutableSingleLiveEvent;
import com.app.dekonotes.lifecycle.SingleLiveEvent;

import java.util.Date;

import androidx.annotation.Nullable;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class NoteViewModel extends ViewModel {

    private final static String ID_KEY = "NOTE_ID";

    private RepositoryNotes repositoryNotes;
    private SavedStateHandle savedStateHandle;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    // Поскольку нет доступа к активити, таким способом сообщаем ей о событиях
    private MutableSingleLiveEvent<FinishEvent> finishEvent = new MutableSingleLiveEvent<>();
    // Поскольку нет доступа к активити, таким способом сообщаем ей о событиях
    private MutableSingleLiveEvent<Integer> messageEvent = new MutableSingleLiveEvent<>();
    private Consumer<Long> saveObserver = new Consumer<Long>() {
        @Override
        public void accept(Long id) {
            savedStateHandle.set(ID_KEY, id);
            messageEvent.sendEvent(R.string.toast_database_save);
        }
    };
    private Action updateObserver = new Action() {
        @Override
        public void run() {
            messageEvent.sendEvent(R.string.toast_database_save);
        }
    };
    private Consumer<Long> saveFinishObserver = new Consumer<Long>() {
        @Override
        public void accept(Long id) {
            messageEvent.sendEvent(R.string.toast_database_save);
            finishEvent.sendEvent(FinishEvent.getInstance());
        }
    };
    private Action updateFinishObserver = new Action() {
        @Override
        public void run() {
            finishEvent.sendEvent(FinishEvent.getInstance());
        }
    };

    // Возвращаю неизменяемый интерфейс, чтобы подписчик не мог записать ничего
    public SingleLiveEvent<FinishEvent> getFinishEvent() {
        return finishEvent;
    }

    // Возвращаю неизменяемый интерфейс, чтобы подписчик не мог записать ничего
    public SingleLiveEvent<Integer> getMessageEvent() {
        return messageEvent;
    }

    @Nullable
    private Long defaultId;

    // SavedStateHandle для доступа к Bundle из вьюмодели https://developer.android.com/topic/libraries/architecture/viewmodel-savedstate
    public NoteViewModel(SavedStateHandle savedStateHandle, RepositoryNotes repositoryNotes, @Nullable Long defaultId) {
        this.savedStateHandle = savedStateHandle;
        this.repositoryNotes = repositoryNotes;
        this.defaultId = defaultId;
    }

    public void saveNote(
            String title,
            String text,
            boolean checkDeadline,
            Date deadlineDate
    ) {
        saveNote(title, text, checkDeadline, deadlineDate, saveObserver, updateObserver);
    }

    public void saveNoteAndFinish(
            String title,
            String text,
            boolean checkDeadline,
            Date deadlineDate
    ) {
        saveNote(title, text, checkDeadline, deadlineDate, saveFinishObserver, updateFinishObserver);
    }

    private void saveNote(
            String title,
            String text,
            boolean checkDeadline,
            Date deadlineDate,
            Consumer<Long> onSuccess,
            Action onUpdate
    ) {

        Note note = buildNote(title, text, checkDeadline, deadlineDate);

        if (note.getId() != 0) {
            update(note, onUpdate);
            return;
        }

        compositeDisposable.add(
                repositoryNotes.insert(note)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(onSuccess)
        );
    }

    private Note buildNote(
            String title,
            String text,
            boolean checkDeadline,
            Date deadlineDate
    ) {
        Long savedId = savedStateHandle.get(ID_KEY);

        // На котлине можно намного красивее
        long id;
        if (savedId == null) {
            if (defaultId != null) {
                id = defaultId;
            } else {
                id = 0;
            }
        } else {
            id = savedId;
        }

        return CreatorNotes.createNote(id, title, text, checkDeadline, deadlineDate);
    }

    private void update(Note note, Action onComplete) {
        compositeDisposable.add(
                repositoryNotes.update(note)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(onComplete)
        );
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
    }
}
