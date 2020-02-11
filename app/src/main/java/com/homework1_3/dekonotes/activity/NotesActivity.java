package com.homework1_3.dekonotes.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.Calendar;

public class NotesActivity extends AppCompatActivity {

    private EditText inputNote;
    private Button btnSaveNote;
    private SharedPreferences myNoteSharedPref;
    private static String NOTE_TEXT = "note_text";
    private ImageButton imgBtnCalendar;
    private CheckBox checkDeadline;
    private EditText title;
    private EditText text;
    private EditText dateCalendar;
    private DatePickerDialog datePickerDialog;
    private Calendar todayCalendar;

    AppDatabase appDatabase;


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
        checkDeadline = findViewById(R.id.checkDeadline);
        title = findViewById(R.id.editTitle);
        text = findViewById(R.id.editText);
        dateCalendar = findViewById(R.id.editDateDeadline);


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
                Note id;
                long date = todayCalendar.getTimeInMillis() ;
                setDateCalendar();
                String titleNote = title.getText().toString();
                String textNote = text.getText().toString();
                Note note = new Note(0,titleNote,textNote, date);
                appDatabase.noteDao().insertNote(note);
            }
        });

        checkDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkDeadline.isChecked()){

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

//    private void setInitialDateTime() {
//
//        currentDateTime.setText(DateUtils.formatDateTime(this,
//                dateAndTime.getTimeInMillis(),
//                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
//                        | DateUtils.FORMAT_SHOW_TIME));
//    }

    DatePickerDialog.OnDateSetListener onDateSet = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            todayCalendar.set(Calendar.YEAR, year);
            todayCalendar.set(Calendar.MONTH, monthOfYear);
            todayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
           // setInitialDateTime();
        }
    };

//    private void getDateFromSharedPref(){
//        String noteTxt = myNoteSharedPref.getString(NOTE_TEXT, "");
//        inputNote.setText(noteTxt);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent targetIntent;
       if (id == R.id.action_settings) {
            Toast.makeText(NotesActivity.this, getString(R.string.toast_settings),
                    Toast.LENGTH_LONG).show();
           // targetIntent = new Intent(MainActivity.this, NotesActivity.class);
           // startActivity(targetIntent);
            return true;
        }
       if (id == android.R.id.home) {
            targetIntent = new Intent(NotesActivity.this, MainActivity.class);
             startActivity(targetIntent);
        }

        return super.onOptionsItemSelected(item);
    }


}