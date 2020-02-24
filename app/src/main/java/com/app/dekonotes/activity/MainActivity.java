package com.app.dekonotes.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.app.dekonotes.App;
import com.app.dekonotes.adapter.DiffUtil.NotesDiffUtilResult;
import com.app.dekonotes.adapter.RecyclerAdapterNotes;
import com.app.dekonotes.data.note.RepositoryNotes;
import com.app.dekonotes.data.note.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.app.dekonotes.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;

public class MainActivity extends AppCompatActivity {

    private static long backPressed;
    private RecyclerView recyclerView;
    private RecyclerAdapterNotes recyclerAdapter;
    private FloatingActionButton addNewNote;
    private List<Note> myList = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RepositoryNotes repositoryNotes = App.getInstance().getRepositoryNotes();

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
                        startActivity(NotesActivity
                                .startNotesActivityWIthExtra(getApplicationContext(),
                                        item.getId()));
                    }
                },

                new RecyclerAdapterNotes.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(final Note item) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setPositiveButton(R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                compositeDisposable.add(
                                                        repositoryNotes.delete(item)
                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                .subscribeWith(new DisposableCompletableObserver() {
                                                                    @Override
                                                                    public void onComplete() {

                                                                    }

                                                                    @Override
                                                                    public void onError(Throwable e) {
                                                                        Toast.makeText(MainActivity.this,
                                                                                getString(R.string.error_notes),
                                                                                Toast.LENGTH_LONG).show();
                                                                    }
                                                                })
                                                );
                                            }
                                        }).setNegativeButton(R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                }).setMessage(R.string.dialog_message)
                                .setTitle(R.string.dialog_title)
                                .setIcon(R.drawable.ic_delete3d).create().show();
                    }
                });

        recyclerView.setAdapter(recyclerAdapter);
    }

    private void subscribe() {
        repositoryNotes.getAll().
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Note>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);

                    }

                    @Override
                    public void onNext(List<Note> note) {
                        new NotesDiffUtilResult().getDIffUtilResult(recyclerAdapter, note);
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
        if (backPressed + 2000 > System.currentTimeMillis()) {
            //эмулируем нажатие на HOME, сворачивая приложение
            Intent endWork = new Intent(Intent.ACTION_MAIN);
            endWork.addCategory(Intent.CATEGORY_HOME);
            endWork.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(endWork);
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.toast_againOnBackPressed),
                    Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}