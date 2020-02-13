package com.app.dekonotes.activity;

import android.app.DatePickerDialog;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.dekonotes.App;
import com.app.dekonotes.R;
import com.app.dekonotes.data.AppDatabase;
import com.app.dekonotes.note.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;


public class NotesActivity extends AppCompatActivity {

    private static final String TAG = "Note";
    private ImageButton imgBtnCalendar = null;
    private CheckBox checkDeadline = null;
    private EditText title = null;
    private EditText text = null;
    private EditText dateCalendar = null;
    private DatePickerDialog datePickerDialog;
    private Calendar todayCalendar = null;
    private Toolbar myToolbar;
    AppDatabase appDatabase = App.getInstance().getDatabase();
    private Note myNote = null;
    Bundle bundleExtra = null;
    long idNoteBundle;


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

    private void setClick(){
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
            }
        });

        checkDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkDeadline.isChecked()){
                    imgBtnCalendar.setClickable(true);
                    imgBtnCalendar.setEnabled(true);
                    dateCalendar.setClickable(true);
                    dateCalendar.setEnabled(true);
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy",
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

    private void setDateCalendar(){
        todayCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(
                this,
                onDateSet,
                todayCalendar.get(Calendar.YEAR),
                todayCalendar.get(Calendar.MONTH),
                todayCalendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener onDateSet = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            todayCalendar.set(Calendar.YEAR, year);
            todayCalendar.set(Calendar.MONTH, monthOfYear);
            todayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Date date = new Date();
            date.setTime(todayCalendar.getTimeInMillis());
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            dateCalendar.setText(dateFormat.format(date.getTime()));
        }
    };

    private void saveNote(){
        try {
            long date;
            if(todayCalendar != null){
                date = todayCalendar.getTimeInMillis() ;
            } else {
                date = 0;
            }

            String titleNote;
            if(title != null){
                titleNote = title.getText().toString();
            } else {
                titleNote = getString(R.string.null_string);
            }

            String textNote;
            if(text != null){
                textNote = text.getText().toString();
            } else {
                textNote = getString(R.string.null_string);
            }
            myNote = new Note(0, titleNote, textNote, checkDeadline.isChecked(), date);

            appDatabase.noteDao().insertNote(myNote)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {
                            Log.i(TAG, "Новая заметка");
                            Toast.makeText(NotesActivity.this,
                                    getString(R.string.toast_database_save),
                                    Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e){
            Log.e(TAG, "ERROR_SAVE");
        }
    }

    private void getInfoExtra(){
        Intent intentInfoIdNote = getIntent();
        bundleExtra = intentInfoIdNote.getExtras();
        idNoteBundle = 0;
        if (bundleExtra != null){
            idNoteBundle= bundleExtra.getLong("id");
            appDatabase.noteDao().getNoteById(idNoteBundle)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Note>() {


                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Note note) {
                            if (note.getDayDeadline() == 0) {
                               // dateCalendar = null;
                                //todayCalendar = null;
                            } else {
                                checkDeadline.setChecked(note.isCheck());
                                if (checkDeadline.isChecked()){
                                    Date date = new Date();
                                    date.setTime(note.getDayDeadline());
                                    DateFormat dateFormat =
                                            new SimpleDateFormat("dd.MM.yyyy");
                                    dateCalendar.setText(dateFormat.format(date.getTime()));
                                } else {
                                   // dateCalendar = null;
                                }
                            }

                            if (note.getTitle().equals(getString(R.string.null_string))) {
                                title = null;
                            } else {
                                title.setText(note.getTitle());
                            }

                            if (note.getText().equals(getString(R.string.null_string))) {
                                text = null;
                            } else {
                                text.setText(note.getText());
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            Toast.makeText(NotesActivity.this, getString(R.string.error_note),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void update(Note note){
        try {
            long date;
            if (todayCalendar != null) {
                date = todayCalendar.getTimeInMillis();
            } else {
                date = 0;
            }
            String titleNote;
            if (title != null) {
                titleNote = title.getText().toString();
            } else {
                titleNote = getString(R.string.null_string);
            }
            String textNote;
            if (text != null) {
                textNote = text.getText().toString();
            } else {
                textNote = getString(R.string.null_string);
            }
            myNote = new Note(idNoteBundle, titleNote, textNote, checkDeadline.isChecked(), date);

            appDatabase.noteDao().update(note)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(NotesActivity.this,
                                    getString(R.string.error_note),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (Exception e){

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
       // Intent targetIntent;
       if (id == R.id.action_save) {
           if (bundleExtra == null){
               saveNote();
           } else {
               update(myNote);
           }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}