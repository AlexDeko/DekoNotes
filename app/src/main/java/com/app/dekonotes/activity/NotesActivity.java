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

import com.app.dekonotes.App;
import com.app.dekonotes.R;
import com.app.dekonotes.data.formatter.DateDeadlineFormatter;
import com.app.dekonotes.data.note.Note;
import com.app.dekonotes.data.note.NoteViewModel;
import com.app.dekonotes.lifecycle.FinishEvent;
import com.app.dekonotes.lifecycle.SingleLiveEventObserver;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.AbstractSavedStateViewModelFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NotesActivity extends AppCompatActivity {

    private final static String NOTE_KEY = "NOTE_KEY";
    private ImageButton imgBtnCalendar = null;
    private CheckBox checkDeadline = null;
    private EditText title = null;
    private EditText text = null;
    private EditText dateCalendar = null;
    private Calendar deadlineCalendar = Calendar.getInstance();
    private Toolbar myToolbar;
    private DateDeadlineFormatter dateDeadlineFormatter = new DateDeadlineFormatter();
    private NoteViewModel viewModel;
    @Nullable
    private Note argument;

    // Кажется так удобнее
    public static void startNotesActivityWIthExtra(Context context, Note note) {
        Intent reWriteNote = new Intent(context,
                NotesActivity.class);
        reWriteNote.putExtra(NOTE_KEY, note);

        context.startActivity(reWriteNote);
    }

    public static void startNotesActivity(Context context) {
        Intent reWriteNote = new Intent(context,
                NotesActivity.class);

        context.startActivity(reWriteNote);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        argument = getIntent().getParcelableExtra(NOTE_KEY);
        initViewModel();
        setContentView(R.layout.activity_notes);
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        initViews();
        if (savedInstanceState == null) {
            setDefaultValues();
        }
        setClick();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this, new AbstractSavedStateViewModelFactory(this, getIntent().getExtras()) {
            // Поскольку у нас в конструктор передаются аргументы, нужна фабрика
            @NonNull
            @Override
            protected <T extends ViewModel> T create(@NonNull String key, @NonNull Class<T> modelClass, @NonNull SavedStateHandle handle) {
                Long defaultId = null;
                if (argument != null) {
                    defaultId = argument.getId();
                }

                //noinspection unchecked
                return (T) new NoteViewModel(handle, App.getInstance().getRepositoryNotes(), defaultId);
            }
        }).get(NoteViewModel.class);

        viewModel.getFinishEvent().observe(this, new SingleLiveEventObserver<FinishEvent>() {
            @Override
            public void receiveEvent(FinishEvent event) {
                finish();
            }
        });
        viewModel.getMessageEvent().observe(this, new SingleLiveEventObserver<Integer>() {
            @Override
            public void receiveEvent(Integer event) {
                Toast.makeText(NotesActivity.this, event, Toast.LENGTH_SHORT).show();
            }
        });
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

    private void setDefaultValues() {
        if (argument == null) {
            return;
        }

        if (argument.getDayDeadline() != 0) {
            checkDeadline.setChecked(argument.isCheck());
            if (checkDeadline.isChecked()) {
                deadlineCalendar.setTime(new Date(argument.getDayDeadline()));
                dateCalendar.setText(dateDeadlineFormatter
                        .getFormatDate(argument.getDayDeadline()));
                deadlineSetEnabledAndClickable();

            }
        }
        title.setText(argument.getTitle());
        text.setText(argument.getText());
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

    // Нажатие на тулбар
    @Override
    public boolean onSupportNavigateUp() {
        saveAndFinish();
        return true;
    }

    // Тут если не переопределить этот метод, то по нажатию назад вьюмодель уничтожится сразу, а нам нужно, чтобы в базу сохранилось сначала
    @Override
    public void onBackPressed() {
        saveAndFinish();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!isFinishing()) {
            viewModel.saveNote(title.getText().toString(), text.getText().toString(), checkDeadline.isChecked(), deadlineCalendar.getTime());
        }
    }

    private void saveAndFinish() {
        if (isNoteEmpty()) {
            finish();
            return;
        }

        viewModel.saveNoteAndFinish(title.getText().toString(), text.getText().toString(), checkDeadline.isChecked(), deadlineCalendar.getTime());
    }

    // Наверное нет смысла сохранять пустую заметку?
    private boolean isNoteEmpty() {
        return (title.getText().toString().isEmpty() && text.getText().toString().isEmpty());
    }
}