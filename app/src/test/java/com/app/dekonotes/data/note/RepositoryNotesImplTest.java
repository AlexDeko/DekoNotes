package com.app.dekonotes.data.note;

import com.app.dekonotes.ImmediateRxSchedulersOverrideRule;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import io.reactivex.Single;

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


        // Моделируем поведение
        when(dao.insertNote(TestData.TEST_NOTE.insert()))
                .thenReturn(Completable.just(TestData.TEST_NOTE));

        repository.insertNote(TestData.TEST_NOTE.insert())
                // Вызываем метод у Rx для тестов. Можно проверить в каком состоянии Completable
                .test()
                // Ожидаем все заметки. В случае ошибки можно вызвать assertError
                .assertValue(TestData.TEST_NOTE);

        // Проверяем, что вызвали метод у чучела
        verify(dao).getAll(TestData.TEST_NOTE.getAll());
    }
}