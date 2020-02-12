package com.homework1_3.dekonotes.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.homework1_3.dekonotes.R;
import com.homework1_3.dekonotes.data.AppDatabase;
import com.homework1_3.dekonotes.note.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;


public class NotesActivity extends AppCompatActivity {

    private EditText inputNote;
    private Button btnSaveNote;
    private SharedPreferences myNoteSharedPref;
    private static String NOTE_TEXT = "note_text";
    private static final String TAG = "Note";
    private ImageButton imgBtnCalendar;
    private CheckBox checkDeadline;
    private EditText title;
    private EditText text;
    private EditText dateCalendar;
    private DatePickerDialog datePickerDialog;
    private Calendar todayCalendar;

    AppDatabase appDatabase;
    Note note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        setClick();
      //  getDateFromSharedPref();
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


        //inputNote = findViewById(R.id.inputNote);
       // btnSaveNote = findViewById(R.id.btnSaveNote);
       // myNoteSharedPref = getSharedPreferences("MyNote", MODE_PRIVATE);

//        btnSaveNote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences.Editor myEditor = myNoteSharedPref.edit();
//                String noteTxt = inputNote.getText().toString();
//                myEditor.putString(NOTE_TEXT, noteTxt);
//                myEditor.apply();
//                Toast.makeText(NotesActivity.this, getString(R.string.toast_save),
//                        Toast.LENGTH_LONG).show();
//            }
//        });
    }

    private void setClick(){
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
            long date = todayCalendar.getTimeInMillis() ;
            String titleNote = title.getText().toString();
            String textNote = text.getText().toString();
            note = new Note(0,titleNote,textNote,checkDeadline.isChecked(), date);
        } catch (Exception e){
            Log.e(TAG, "Error");
        }

        try {

            appDatabase.noteDao().insertNote(note)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {
                            Log.i(TAG, "Новая заметка");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "Ошибка с новой заметкой");
                        }
                    });
        } catch (Exception e){
            Log.e(TAG, "ERROR_SAVE");
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
        Intent targetIntent;
       if (id == R.id.action_save) {
            saveNote();
            Toast.makeText(NotesActivity.this, getString(R.string.toast_database_save),
                    Toast.LENGTH_LONG).show();
            //targetIntent = new Intent(MainActivity.this, NotesActivity.class);
            //startActivity(targetIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}