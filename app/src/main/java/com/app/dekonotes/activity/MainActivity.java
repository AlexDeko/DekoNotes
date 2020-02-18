package com.app.dekonotes.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.app.dekonotes.App;
import com.app.dekonotes.adapter.ListAdapterNotes;
import com.app.dekonotes.data.AppDatabase;
import com.app.dekonotes.data.note.RepositoryNotesImpl;
import com.app.dekonotes.data.note.RepositoryNotesImplEx;
import com.app.dekonotes.data.note.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.app.dekonotes.R;


import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;

public class MainActivity extends AppCompatActivity {

    private final static String LOG = "MAIN";
    private ListView list;
    private FloatingActionButton addNewNote;
    List<Note> myList;
    // Контейнер для подписок. См. onDestroy()
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    AppDatabase appDatabase = App.getInstance().getDatabase();
    ListAdapterNotes listAdapterNotes;
    RepositoryNotesImpl repositoryNotes = new RepositoryNotesImpl(appDatabase.noteDao());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        initViews();
        subscribe();
        listAdapterNotes = new ListAdapterNotes(this);
        listAdapterNotes.notifyDataSetChanged();
        list.setAdapter(listAdapterNotes);
        listAdapterNotes.notifyDataSetChanged();
        btnAddNote();
        setItemClicks();
        setSwipe();

    }

    private void setSwipe() {
        final SwipeRefreshLayout swipeLayout = findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            // Будет вызван, когда пользователь потянет список вниз
            @Override
            public void onRefresh() {
                subscribe();
                swipeLayout.setRefreshing(false);
            }
        });
    }

    private void subscribe() {
        Observable<List<Note>> listObservable = repositoryNotes.getAll();
        listObservable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Note>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            // Disposable представляет собой интерфейс для работы с подпиской. Через него можно отписаться
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(List<Note> note) {
                            updateList(myList);
                            myList = note;

                            listAdapterNotes.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(MainActivity.this,
                            getString(R.string.error_note), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
    }

    private void initViews() {
        addNewNote = findViewById(R.id.FABAddNote);
        list = findViewById(R.id.list);
    }

    private void btnAddNote() {
        addNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startCreateNewNote = new Intent(MainActivity.this,
                        NotesActivity.class);
                startActivity(startCreateNewNote);
            }
        });
    }

    private void setItemClicks() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position,
                                    long id) {
                Intent reWriteNote = new Intent(MainActivity.this,
                        NotesActivity.class);
                reWriteNote.putExtra("id", myList.get(position).getId());
                startActivity(reWriteNote);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view,
                                           final int position, final long id) {
                AlertDialog.Builder builderDialogDelete = new AlertDialog
                        .Builder(MainActivity.this);

                builderDialogDelete.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    Note note = myList.get(position);
                                    Completable completable = repositoryNotes.delete(note);
                                    completable.observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new DisposableCompletableObserver() {
                                                @Override
                                                public void onComplete() {
                                                    Log.i(LOG, "Удалена заметка");
                                                    subscribe();
                                                    listAdapterNotes.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    e.printStackTrace();
                                                }
                                            });

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });

                builderDialogDelete.setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });

                builderDialogDelete.setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title)
                        .setIcon(R.drawable.ic_delete3d);
                AlertDialog dialogDelete = builderDialogDelete.create();
                dialogDelete.show();
                return true;
            }
        });
    }

    private void updateList(List<Note> baseListNote) {

        listAdapterNotes.setItems(baseListNote);
        listAdapterNotes.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent targetIntent;
        if (id == R.id.action_settings) {
            targetIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(targetIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Отписываемся, чтобы не было утечек памяти
        compositeDisposable.dispose();
    }

    @Override
    public void onBackPressed() {
        //эмулируем нажатие на HOME, сворачивая приложение
        Intent endWork = new Intent(Intent.ACTION_MAIN);
        endWork.addCategory(Intent.CATEGORY_HOME);
        endWork.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(endWork);
    }
}