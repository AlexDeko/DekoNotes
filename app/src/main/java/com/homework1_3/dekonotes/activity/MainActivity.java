package com.homework1_3.dekonotes.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.homework1_3.dekonotes.App;
import com.homework1_3.dekonotes.R;
import com.homework1_3.dekonotes.data.AppDatabase;
import com.homework1_3.dekonotes.note.Note;
import com.homework1_3.dekonotes.note.NoteDao;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = "sample";
    private final static String TITLE = "title";
    private final static String SUBTITLE = "subtitle";
    private final static String TEXT = "text";
    private final static String PREF = "pref";
    List<Map<String, String>> simpleAdapterContent;
    private ListView list;
    private SharedPreferences sharedPref;
    private String result;
    private String[] content;
    private FloatingActionButton addNewNote;
    BaseAdapter listContentAdapter;

    AppDatabase appDatabase;
   // NoteDao noteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        initViews();


        updateList();
        content = prepareContent();
        listContentAdapter = createAdapter(content);
        list.setAdapter(listContentAdapter);
        listContentAdapter.notifyDataSetChanged();

        btnAddNote();
        setItemClicks();
        setSwipe();


//        appDatabase = App.getInstance().getDatabase();
//        noteDao = appDatabase.noteDao();
//        appDatabase.noteDao().getNoteById(1)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DisposableSingleObserver<Note>() {
//                    @Override
//                    public void onSuccess(Note note) {
//                        // ...
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        // ...
//                    }
//                });
//
//        appDatabase.noteDao().getAll()
//                .observeOn(Schedulers.io())
//                .subscribe(new Consumer<List<Note>>() {
//                    @Override
//                    public void accept(List<Note> notes) throws Exception {
//                        //..
//                    }
//                });





    }

    private void setSwipe(){
        final SwipeRefreshLayout swipeLayout = findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            // Будет вызван, когда пользователь потянет список вниз
            @Override
            public void onRefresh() {
                updateList();
                swipeLayout.setRefreshing(false);
            }
        });
    }

    private void initViews(){
        addNewNote =findViewById(R.id.FABAddNote);
    }

    private void btnAddNote(){
        addNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startCreateNewNote = new Intent(MainActivity.this,
                        NotesActivity.class);
                startActivity(startCreateNewNote);
            }
        });
    }

    private void setItemClicks(){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position,
                                    long id) {
                //final View item = (TextView) parent.getItemAtPosition(position);



                //  list.removeView(item);

                // listContentAdapter.notifyDataSetChanged();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, final long id) {
                view.animate().setDuration(20).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builderDialogDelete = new AlertDialog
                                        .Builder(MainActivity.this );

                                builderDialogDelete.setPositiveButton(R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK button


                                        // list.removeView(item);

                                        //   Note note ;

                                        //   appDatabase.noteDao().delete().observeOn(App.getInstance());



                                        simpleAdapterContent.remove(position);
                                        listContentAdapter.notifyDataSetChanged();
                                        view.setAlpha(1);
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

                return true;
            }
        });
    }

    private void updateList() {
       // appDatabase.noteDao().
        sharedPref = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        // SharedPreferences.Editor myEditor = sharedPref.edit();
        if (sharedPref.contains(TEXT)) {
            result = sharedPref.getString(TEXT, null);

        } else {
            String largeText = getString(R.string.large_text);

            sharedPref.edit()
                    .putString(TEXT, largeText)
                    .apply();
            result = largeText;
        }
        content = prepareContent();
    }

    public void getNotes() {
        appDatabase.noteDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Note>>() {
            @Override
            public void accept(List<Note> notes) throws Exception {

            }
        });
    }

    @NonNull
    private BaseAdapter createAdapter(String[] values) {
        list = findViewById(R.id.list);
        simpleAdapterContent = new ArrayList<>();



        for (String value : values) {
            Map<String, String> row = new HashMap<>();
            row.put(TITLE, value);
            row.put(SUBTITLE, String.valueOf(value.length()));
            simpleAdapterContent.add(row);
        }
        return new SimpleAdapter(
                this,
                simpleAdapterContent,
                R.layout.list_item,
                new String[]{TITLE, SUBTITLE},
                new int[]{R.id.titleItem1, R.id.textItem2}
        );
    }

    @NonNull
    private String[] prepareContent() {
        return result.split("\n\n");
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
            Toast.makeText(MainActivity.this, getString(R.string.toast_settings),
                    Toast.LENGTH_LONG).show();
            targetIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(targetIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//
//    }

}