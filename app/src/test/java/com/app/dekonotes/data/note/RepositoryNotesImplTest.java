package com.app.dekonotes.data.note;

import com.app.dekonotes.ImmediateRxSchedulersOverrideRule;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

// Классы для тестов, которые не используют классы андроида создаём в test. Инструментальные тесты кладём в androidTest
// (они запускаются на девайсе в отличие от обычных юнит тестов)
public class RepositoryNotesImplTest {

    // Это нужно, чтобы все операции в Rx выполнялись на шлавном потоке
    @Rule
    public ImmediateRxSchedulersOverrideRule immediateRxSchedulersOverrideRule =
            new ImmediateRxSchedulersOverrideRule();

    // Создаём чучело, чтобы проверять вызвался ли метод и моделируем поведение
    private NoteDao dao = mock(NoteDao.class);
    // В конструктор передали чучело
    private RepositoryNotesImpl repository = new RepositoryNotesImpl(dao);



    // Это вызовется после каждого метода с аннотацией @Test
    @After
    public void tearDown() {
        verifyNoMoreInteractions(
                //Здесь пишем список наших моков. Если не вызвать verify, а метод был всё-таки вызван, то здесь выбросится исключение и тесты провалятся
                dao
        );
    }

    // Слева от EXPECT пишем что произошло, справа то, что ожидается
    @Test
    public void get_by_id_EXPECT_note_from_dao() {
        // Моделируем поведение
        when(dao.getNoteById(TestData.TEST_NOTE.getId()))
                .thenReturn(Single.just(TestData.TEST_NOTE));

        repository.getById(TestData.TEST_NOTE.getId())
                // Вызываем метод у Rx для тестов. Можно проверить в каком состоянии Single
                .test()
                // Ожидаем заметку. В случае ошибки можно вызвать assertError
                .assertValue(TestData.TEST_NOTE);

        // Проверяем, что вызвали метод у чучела
        verify(dao).getNoteById(TestData.TEST_NOTE.getId());



    }

    @Test
    public void get_all_EXPECT_note_in_repository() {
        List<Note> testData = Arrays.asList(TestData.TEST_NOTE, TestData.TEST_NOTE, TestData.TEST_NOTE);
        // Моделируем поведение
        when(dao.getAll())
                .thenReturn(Observable.just((testData)));

        repository.getAll()
                // Вызываем метод у Rx для тестов. Можно проверить в каком состоянии Observable
                .test()
                // Ожидаем добавленее новой заметки. В случае ошибки можно вызвать assertError
                .assertValue(testData);
        verify(dao).getAll();
    }

    @Test
    public void insert_EXPECT_note_in_dao() {
        when(dao.insertNote(TestData.TEST_NOTE))
                .thenReturn(Single.just(TestData.TEST_NOTE.getId()));

        repository.insert(TestData.TEST_NOTE)
                // Вызываем метод у Rx для тестов. Можно проверить в каком состоянии Completable
                .test()
                // Ожидаем добавленее новой заметки и вызов complete. В случае ошибки можно вызвать assertError
                .assertComplete();
        verify(dao).insertNote(TestData.TEST_NOTE);
    }

    @Test
    public void delete_EXPECT_note_from_dao() {
        when(dao.delete(TestData.TEST_NOTE))
                .thenReturn(Completable.complete());

        repository.delete(TestData.TEST_NOTE)
                // Вызываем метод у Rx для тестов. Можно проверить в каком состоянии Completable
                .test()
                // Ожидаем добавленее новой заметки и вызов complete. В случае ошибки можно вызвать assertError
                .assertComplete();
        verify(dao).delete(TestData.TEST_NOTE);
    }

    @Test
    public void update_EXPECT_note_in_dao() {
        when(dao.update(TestData.TEST_NOTE))
                .thenReturn(Completable.complete());

        repository.update(TestData.TEST_NOTE)
                // Вызываем метод у Rx для тестов. Можно проверить в каком состоянии Completable
                .test()
                // Ожидаем добавленее новой заметки и вызов complete. В случае ошибки можно вызвать assertError
                .assertComplete();
        verify(dao).update(TestData.TEST_NOTE);
    }
}