package com.homework1_3.dekonotes;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;
import java.util.Date;

public class NotesActivity extends AppCompatActivity {

    private EditText inputNote;
    private Button btnSaveNote;
    private SharedPreferences myNoteSharedPref;
    private static String NOTE_TEXT = "note_text";
    private ImageView imgBtnCalendar;
    private CheckBox checkDeadline;
    private EditText title;
    private EditText text;
    private EditText dateCalendar;
    private DatePickerDialog datePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setLogo(getDrawable(R.drawable.ic_notepad_12));

        initViews();
        setDateCalendar();
        getDateFromSharedPref();
    }

    private void initViews() {
        imgBtnCalendar = findViewById(R.id.imgBtnCalendar);
        checkDeadline = findViewById(R.id.checkDeadline);
        title = findViewById(R.id.editTitle);
        text = findViewById(R.id.editText);
        dateCalendar = findViewById(R.id.editDateDeadline);

        inputNote = findViewById(R.id.inputNote);
        btnSaveNote = findViewById(R.id.btnSaveNote);
        myNoteSharedPref = getSharedPreferences("MyNote", MODE_PRIVATE);

        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor myEditor = myNoteSharedPref.edit();
                String noteTxt = inputNote.getText().toString();
                myEditor.putString(NOTE_TEXT, noteTxt);
                myEditor.apply();
                Toast.makeText(NotesActivity.this, getString(R.string.toast_save),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setDateCalendar(){

        Calendar todayCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(
                this,
                onDateSet,
                todayCalendar.get(Calendar.YEAR),
                todayCalendar.get(Calendar.MONTH),
                todayCalendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();


    }

    private void DatePickerDialog onDateSet(){
        datePickerDialog = ;
        DatePickerDialog.OnDateSetListener;
        return null;
    }

    private void getDateFromSharedPref(){
        String noteTxt = myNoteSharedPref.getString(NOTE_TEXT, "");
        inputNote.setText(noteTxt);
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
//        if (id == R.id.action_settings) {
//            Toast.makeText(Not.this, getString(R.string.toast_settings),
//                    Toast.LENGTH_LONG).show();
//            targetIntent = new Intent(MainActivity.this, NotesActivity.class);
//            startActivity(targetIntent);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


}