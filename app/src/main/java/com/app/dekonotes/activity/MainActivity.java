package com.app.dekonotes.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.app.dekonotes.App;
import com.app.dekonotes.adapter.ListAdapterNotes;
import com.app.dekonotes.adapter.RecyclerAdapterNotes;
import com.app.dekonotes.data.AppDatabase;
import com.app.dekonotes.data.note.RepositoryNotesImpl;
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

    private final static String TAG = "Main";
    private static long back_pressed;
    private RecyclerView recyclerView;
 //   private RecyclerView.Adapter recyclerAdapter;
    private RecyclerAdapterNotes recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton addNewNote;
    List<Note> myList;
    // Контейнер для подписок. См. onDestroy()
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    AppDatabase appDatabase = App.getInstance().getDatabase();
    RepositoryNotesImpl repositoryNotes = new RepositoryNotesImpl(appDatabase.noteDao());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        initViews();
        setRecycler();
        subscribe();
        btnAddNote();
        setItemClicks();
    }

    private void setRecycler(){
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new RecyclerAdapterNotes();
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
                        updateList(note);
                        myList = note;
//                        listAdapterNotes.notifyDataSetChanged();
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

    private void setItemClicks() {


//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, final View view, final int position,
//                                    long id) {
//                Intent reWriteNote = new Intent(MainActivity.this,
//                        NotesActivity.class);
//                reWriteNote.putExtra("id", myList.get(position).getId());
//                startActivity(reWriteNote);
//            }
//        });
//
//        recyclerView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, final View view,
//                                           final int position, final long id) {
//                AlertDialog.Builder builderDialogDelete = new AlertDialog
//                        .Builder(MainActivity.this);
//
//                builderDialogDelete.setPositiveButton(R.string.ok,
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                try {
//                                    Note note = myList.get(position);
//                                    Completable completable = repositoryNotes.delete(note);
//                                    completable.observeOn(AndroidSchedulers.mainThread())
//                                            .subscribe(new DisposableCompletableObserver() {
//                                                @Override
//                                                public void onComplete() {
//                                                    Log.i(TAG, "Удалена заметка");
//                                                    subscribe();
//                                                    listAdapterNotes.notifyDataSetChanged();
//                                                }
//
//                                                @Override
//                                                public void onError(Throwable e) {
//                                                    e.printStackTrace();
//                                                }
//                                            });
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//
//                builderDialogDelete.setNegativeButton(R.string.cancel,
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                // User cancelled the dialog
//                            }
//                        });
//
//                builderDialogDelete.setMessage(R.string.dialog_message)
//                        .setTitle(R.string.dialog_title)
//                        .setIcon(R.drawable.ic_delete3d);
//                AlertDialog dialogDelete = builderDialogDelete.create();
//                dialogDelete.show();
//                return true;
//            }
//        });
    }

    private void updateList(List<Note> baseListNote) {
      //  RecyclerAdapterNotes recyclerAdapter = new RecyclerAdapterNotes();
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