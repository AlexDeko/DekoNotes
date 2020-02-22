package com.app.dekonotes.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.dekonotes.App;
import com.app.dekonotes.R;
import com.app.dekonotes.data.AppDatabase;
import com.app.dekonotes.data.note.CreatorNotes;
import com.app.dekonotes.data.note.RepositoryNotesImpl;
import com.app.dekonotes.data.note.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;

public class NotesActivity extends AppCompatActivity {

    private static final String TAG = "Notes";
    private ImageButton imgBtnCalendar = null;
    private CheckBox checkDeadline = null;
    private EditText title = null;
    private EditText text = null;
    private EditText dateCalendar = null;
    private Calendar todayCalendar = Calendar.getInstance();
    private Toolbar myToolbar;
    AppDatabase appDatabase = App.getInstance().getDatabase();
    Bundle bundleExtra = null;
    long idNoteBundle;
    long dateBundle;
    RepositoryNotesImpl repositoryNotes = new RepositoryNotesImpl(appDatabase.noteDao());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        setClick();
        getInfoExtra();
    }

    private void initViews() {
        imgBtnCalendar = findViewById(R.id.imgBtnCalendar);
        imgBtnCalendar.setClickable(false);
        imgBtnCalendar.setEnabled(false);
        checkDeadline = findViewById(R.id.checkDeadline);
        title = findViewById(R.id.editTitle);
        text = findViewById(R.id.editText);
        dateCalendar = findViewById(R.id.editDateDeadline);
        dateCalendar.setClickable(false);
        dateCalendar.setEnabled(false);
    }

    private void setClick() {
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgBtnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateCalendar();
                setTimeCalendar();
            }
        });

        checkDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkDeadline.isChecked()) {
                    deadlineSetEnabledAndClickable();
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm",
                            Locale.getDefault());
                    dateCalendar.setText(dateFormat.format(date));
                } else {
                    dateCalendar.setText(null);
                    imgBtnCalendar.setClickable(false);
                    imgBtnCalendar.setEnabled(false);
                    dateCalendar.setClickable(false);
                    dateCalendar.setEnabled(false);
                }
            }
        });
    }

    private void setDateCalendar() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                onDateSet,
                todayCalendar.get(Calendar.YEAR),
                todayCalendar.get(Calendar.MONTH),
                todayCalendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public void setTimeCalendar() {
        new TimePickerDialog(this, onTimeSet,
                todayCalendar.get(Calendar.HOUR_OF_DAY),
                todayCalendar.get(Calendar.MINUTE), true)
                .show();
    }

    TimePickerDialog.OnTimeSetListener onTimeSet = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            todayCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            todayCalendar.set(Calendar.MINUTE, minute);

        }
    };

    DatePickerDialog.OnDateSetListener onDateSet = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            todayCalendar.set(Calendar.YEAR, year);
            todayCalendar.set(Calendar.MONTH, monthOfYear);
            todayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Date date = new Date();
            date.setTime(todayCalendar.getTimeInMillis());
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            dateCalendar.setText(dateFormat.format(date.getTime()));
        }
    };


    private void saveNote() {
        try {
            CreatorNotes creatorNotes = new CreatorNotes();
            Note myNote = creatorNotes.createNote(idNoteBundle, title, text,
                    checkDeadline.isChecked(), todayCalendar);

            Completable completable = repositoryNotes.insert(myNote);
            completable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {
                            Toast.makeText(NotesActivity.this,
                                    getString(R.string.toast_database_save),
                                    Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }
                    });


        } catch (Exception e) {
            Log.e(TAG, "ERROR_SAVE");
        }
    }

    private void getInfoExtra() {
        Intent intentInfoIdNote = getIntent();
        bundleExtra = intentInfoIdNote.getExtras();
        idNoteBundle = 0;
        if (bundleExtra != null) {
            idNoteBundle = bundleExtra.getLong("id");
            try {
                Single<Note> subscribeNote = repositoryNotes.getById(idNoteBundle);

                subscribeNote.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Note>() {

                            @Override
                            public void onSubscribe(Disposable d) {


                            }

                            @Override
                            public void onSuccess(Note note) {
                                if (note.getDayDeadline() != 0) {
                                    checkDeadline.setChecked(note.isCheck());
                                    if (checkDeadline.isChecked()) {
                                        Date date = new Date();
                                        date.setTime(note.getDayDeadline());
                                        todayCalendar.setTime(date);
                                        DateFormat dateFormat =
                                                new SimpleDateFormat("dd.MM.yyyy HH:mm");
                                        dateCalendar.setText(dateFormat.format(date.getTime()));
                                        deadlineSetEnabledAndClickable();

                                    }
                                }
                                title.setText(note.getTitle());
                                text.setText(note.getText());

                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
            } catch (Exception e) {
                Toast.makeText(NotesActivity.this, getString(R.string.error_note),
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    private void update() {
        try {

            CreatorNotes creatorNotes = new CreatorNotes();
            Note myNote = creatorNotes.createNote(idNoteBundle, title, text,
                    checkDeadline.isChecked(), todayCalendar);

            Completable completable = repositoryNotes.update(myNote);
            completable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {
                            Toast.makeText(NotesActivity.this,
                                    getString(R.string.toast_database_save),
                                    Toast.LENGTH_LONG).show();
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

    private void deadlineSetEnabledAndClickable() {
        imgBtnCalendar.setClickable(true);
        imgBtnCalendar.setEnabled(true);
        dateCalendar.setClickable(true);
        dateCalendar.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_save) {
            if (bundleExtra == null) {
                onBackPressed();

            } else {
                onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bundleExtra == null) {
            saveNote();
        } else {
            update();
        }
        Log.i(TAG, "onStop()");
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