package com.app.dekonotes.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.app.dekonotes.App;
import com.app.dekonotes.adapter.RecyclerAdapterNotes;
import com.app.dekonotes.data.AppDatabase;
import com.app.dekonotes.data.note.RepositoryNotesImpl;
import com.app.dekonotes.data.note.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.app.dekonotes.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "Main";
    private static long back_pressed;
    private RecyclerView recyclerView;
    private RecyclerAdapterNotes recyclerAdapter;
    private FloatingActionButton addNewNote;
    List<Note> myList = new ArrayList<>();
    // Контейнер для подписок. См. onDestroy()
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    AppDatabase appDatabase = App.getInstance().getDatabase();
    RepositoryNotesImpl repositoryNotes = new RepositoryNotesImpl(appDatabase.noteDao());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setMyToolbar();
        initViews();
        setRecycler();
        subscribe();
        btnAddNote();
    }

    private void setMyToolbar() {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    private void setRecycler() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new RecyclerAdapterNotes(myList,

                new RecyclerAdapterNotes.OnItemClickListener() {
                    @Override
                    public void onItemClick(Note item) {
                        Intent reWriteNote = new Intent(MainActivity.this,
                                NotesActivity.class);
                        reWriteNote.putExtra("id", item.getId());
                        startActivity(reWriteNote);
                    }
                },

                new RecyclerAdapterNotes.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(final Note item) {
                        AlertDialog.Builder builderDialogDelete = new AlertDialog
                                .Builder(MainActivity.this);

                        builderDialogDelete.setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        try {
                                            Completable completable = repositoryNotes.delete(item);
                                            completable.observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new DisposableCompletableObserver() {
                                                        @Override
                                                        public void onComplete() {
                                                            Log.i(TAG, "Удалена заметка");
                                                            subscribe();
                                                            recyclerAdapter.notifyDataSetChanged();
                                                        }

                                                        @Override
                                                        public void onError(Throwable e) {
                                                            e.printStackTrace();
                                                        }
                                                    });

                                        } catch (Exception e) {
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
                    }
                });

        recyclerView.setAdapter(recyclerAdapter);
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
                        myList = note;
                        updateList(note);
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
        recyclerView = findViewById(R.id.recycler);
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

    private void updateList(List<Note> baseListNote) {
        recyclerAdapter.clearItems();
        recyclerAdapter.setItems(baseListNote);
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
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            //эмулируем нажатие на HOME, сворачивая приложение
            Intent endWork = new Intent(Intent.ACTION_MAIN);
            endWork.addCategory(Intent.CATEGORY_HOME);
            endWork.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(endWork);
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.toast_againOnBackPressed),
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
        Log.i(TAG, "onBackPressed()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }
}