package com.app.dekonotes.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.dekonotes.App;
import com.app.dekonotes.R;
import com.app.dekonotes.data.formatter.DateDeadlineFormatter;
import com.app.dekonotes.data.note.CreatorNotes;
import com.app.dekonotes.data.note.RepositoryNotes;
import com.app.dekonotes.data.note.Note;

import java.util.Calendar;
import java.util.Date;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;

public class NotesActivity extends AppCompatActivity {

    private final static String keyBundleIdNote = "keyIdNote";
    private static String idBundleExtra = "id";
    private ImageButton imgBtnCalendar = null;
    private CheckBox checkDeadline = null;
    private EditText title = null;
    private EditText text = null;
    private EditText dateCalendar = null;
    private Calendar deadlineCalendar = Calendar.getInstance();
    private Toolbar myToolbar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private long idNoteBundle = 0;
    private CreatorNotes creatorNotes = new CreatorNotes();
    private DateDeadlineFormatter dateDeadlineFormatter = new DateDeadlineFormatter();
    private RepositoryNotes repositoryNotes = App.getInstance().getRepositoryNotes();


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
                    dateCalendar.setText(dateDeadlineFormatter.getFormatDate(date.getTime()));
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
                deadlineCalendar.get(Calendar.YEAR),
                deadlineCalendar.get(Calendar.MONTH),
                deadlineCalendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public void setTimeCalendar() {
        new TimePickerDialog(this, onTimeSet,
                deadlineCalendar.get(Calendar.HOUR_OF_DAY),
                deadlineCalendar.get(Calendar.MINUTE), true)
                .show();
    }

    TimePickerDialog.OnTimeSetListener onTimeSet = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            deadlineCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            deadlineCalendar.set(Calendar.MINUTE, minute);

        }
    };

    DatePickerDialog.OnDateSetListener onDateSet = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            deadlineCalendar.set(Calendar.YEAR, year);
            deadlineCalendar.set(Calendar.MONTH, monthOfYear);
            deadlineCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dateCalendar.setText(dateDeadlineFormatter
                    .getFormatDate(deadlineCalendar.getTimeInMillis()));
        }
    };

    private void saveNote() {

        Note myNote = creatorNotes.createNote(idNoteBundle, title, text,
                checkDeadline.isChecked(), deadlineCalendar);

        repositoryNotes.insert(myNote)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(Long aLong) {
                        idNoteBundle = aLong;
                        Toast.makeText(NotesActivity.this,
                                getString(R.string.toast_database_save),
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public static Intent startNotesActivityWIthExtra(Context context, long id) {
        Intent reWriteNote = new Intent(context,
                NotesActivity.class);
        reWriteNote.putExtra(idBundleExtra, id);
        return reWriteNote;
    }

    private void getNoteById(long id) {
        repositoryNotes.getById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Note>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Note note) {
                        if (note.getDayDeadline() != 0) {
                            checkDeadline.setChecked(note.isCheck());
                            if (checkDeadline.isChecked()) {
                                deadlineCalendar.setTime(new Date(note.getDayDeadline()));
                                dateCalendar.setText(dateDeadlineFormatter
                                        .getFormatDate(note.getDayDeadline()));
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
    }

    private void getInfoExtra() {
        Intent intentInfoIdNote = getIntent();
        Bundle bundleExtra = intentInfoIdNote.getExtras();
        if (bundleExtra != null) {
            idNoteBundle = bundleExtra.getLong(idBundleExtra);
            getNoteById(idNoteBundle);
        } else if (idNoteBundle != 0){
            getNoteById(idNoteBundle);
        }
    }


    private void update() {
        Note myNote = creatorNotes.createNote(idNoteBundle, title, text,
                checkDeadline.isChecked(), deadlineCalendar);

        repositoryNotes.update(myNote)
                .observeOn(AndroidSchedulers.mainThread())
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
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (idNoteBundle == 0) {
            saveNote();
        } else {
            update();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (idNoteBundle != 0 ){
            outState.putLong(keyBundleIdNote, idNoteBundle);
        }

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        idNoteBundle = savedInstanceState.getLong(keyBundleIdNote);
        getInfoExtra();
    }
}