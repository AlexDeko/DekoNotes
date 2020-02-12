package com.homework1_3.dekonotes.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private final static String LOG = "MAIN";
    private final static String TITLE_NOTE = "title";
    private final static String TEXT_NOTE = "subtitle";
    private final static String DEADLINE_NOTE = "deadline";
    List<Map<String, String>> simpleAdapterContent = new ArrayList<>();
    private ListView list;
    private FloatingActionButton addNewNote;
    BaseAdapter listContentAdapter;
    // Контейнер для подписок. См. onDestroy()
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    AppDatabase appDatabase = App.getInstance().getDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        initViews();
        listContentAdapter = createAdapter();
        list.setAdapter(listContentAdapter);
        listContentAdapter.notifyDataSetChanged();

        btnAddNote();
        setItemClicks();
        setSwipe();

        subscribe();
    }

    private void setSwipe(){
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
        appDatabase.noteDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Note>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // Disposable представляет собой интерфейс для работы с подпиской. Через него можно отписаться
                        compositeDisposable.add(d);
                    }
                    @Override
                    public void onNext(List<Note> note) {
                        updateList(note);
                    }
                    @Override
                    public void onError(Throwable e) {
                        // TODO отобразить ошибку вместо списка или тост
                    }
                    @Override
                    public void onComplete() {
                        // Ничего не делаем
                    }
                });
    }

    private void initViews(){
        addNewNote = findViewById(R.id.FABAddNote);
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
            public boolean onItemLongClick(AdapterView<?> parent, final View view,
                                           final int position, final long id) {
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
//                                        appDatabase.noteDao().delete()
//                                                .subscribeOn(Schedulers.io())
//                                                .observeOn(AndroidSchedulers.mainThread())
//                                                .subscribe();

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

    private void updateList(List<Note >baseListNote) {
        simpleAdapterContent.clear();
        for (Note value : baseListNote) {
            String deadline;
            if (value.getDayDeadline() == 0) {
                deadline = null;
            } else {
                Date date = new Date();
                date.setTime(value.getDayDeadline());
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                deadline = dateFormat.format(date.getTime());
            }
            String title;
            if (value.getTitle().equals(getString(R.string.null_string))) {
                title = null;
            } else {
                title = value.getTitle();
            }
            String text;
            if (value.getText().equals(getString(R.string.null_string))) {
                text = null;
            } else {
                text = value.getText();
            }
            Map<String, String> row = new HashMap<>();
            row.put(TITLE_NOTE, title);
            row.put(TEXT_NOTE, text);
            row.put(DEADLINE_NOTE, deadline);
            simpleAdapterContent.add(row);
        }
        listContentAdapter.notifyDataSetChanged();
    }

    @NonNull
    private BaseAdapter createAdapter() {
        list = findViewById(R.id.list);

        return new SimpleAdapter(
                this,
                simpleAdapterContent,
                R.layout.list_item,
                new String[]{TITLE_NOTE, TEXT_NOTE, DEADLINE_NOTE},
                new int[]{R.id.titleItem1, R.id.textItem2, R.id.deadlineItem3}
        );
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Отписываемся, чтобы не было утечек памяти
        compositeDisposable.dispose();
    }
}