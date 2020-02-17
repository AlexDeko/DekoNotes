package com.app.dekonotes.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.LocaleData;
import android.os.Bundle;
import android.text.format.DateUtils;
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
import com.app.dekonotes.data.RepositoryNotes;
import com.app.dekonotes.note.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
    Bundle bundleExtra = null;
    long idNoteBundle;
    RepositoryNotes repositoryNotes = new RepositoryNotes();


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
                setTimeCalendar();


                //setInitialDateTime();
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

    public void setTimeCalendar() {
        new TimePickerDialog(this, onTimeSet,
                todayCalendar.get(Calendar.HOUR_OF_DAY),
                todayCalendar.get(Calendar.MINUTE), true)
                .show();
    }

    TimePickerDialog.OnTimeSetListener onTimeSet= new TimePickerDialog.OnTimeSetListener() {
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

            Date dateChange = new Date();
            long lastChange = dateChange.getTime();

            int containsDeadline ;
            if (date == 0){
                containsDeadline = 0;
            } else {
                containsDeadline = 1;
            }

            Note myNote = new Note(0, titleNote, textNote, checkDeadline.isChecked(), date,
                    lastChange, containsDeadline);


            repositoryNotes.insert(myNote);
            Toast.makeText(NotesActivity.this,
                                    getString(R.string.toast_database_save),
                                    Toast.LENGTH_LONG).show();
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
            try {
                Note note = repositoryNotes.getById(idNoteBundle);
                if (note.getDayDeadline() != 0) {
                    checkDeadline.setChecked(note.isCheck());
                    if (checkDeadline.isChecked()){
                        Date date = new Date();
                        date.setTime(note.getDayDeadline());
                        DateFormat dateFormat =
                                new SimpleDateFormat("dd.MM.yyyy HH:mm");
                        dateCalendar.setText(dateFormat.format(date.getTime()));
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
            } catch (Exception e) {
                Toast.makeText(NotesActivity.this, getString(R.string.error_note),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void update(){
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
            Date dateChange = new Date();
            long lastChange = dateChange.getTime();
            int containsDeadline ;
            if (date == 0){
                containsDeadline = 0;
            } else {
                containsDeadline = 1;
            }
            Note myNote = new Note(idNoteBundle, titleNote, textNote, checkDeadline.isChecked(),
                    date, lastChange, containsDeadline);

            repositoryNotes.update(myNote);
            Toast.makeText(NotesActivity.this,
                    getString(R.string.toast_database_save),
                    Toast.LENGTH_LONG).show();
        } catch (Exception e){
            e.printStackTrace();
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
               update();
           }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //toDO Сохранение заметки при любом случае
//    @Override
//    public void onBackPressed(){
//        super.onBackPressed();
//        if (bundleExtra == null){
//
//            MenuItem item = getMenuInflater().inflate(R.menu.menu_notes, );findItem(R.id.action_save);
//            if (item.isChecked()){
//            saveNote();
//            }
//        } else {
//            update();
//        }
//        onBackPressed();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (bundleExtra == null){
//            saveNote();
//        } else {
//            update();
//        }
//    }
}