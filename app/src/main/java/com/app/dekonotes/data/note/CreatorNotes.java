package com.app.dekonotes.data.note;

import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class CreatorNotes {

    public CreatorNotes() {

    }

    public Note createNote(long idNoteBundle, TextView title, TextView text, boolean checkDeadline,
                           Calendar todayCalendar) {
        long date;
        if (todayCalendar != null) {
            date = todayCalendar.getTimeInMillis();
        } else {
            date = 0;
        }

        String titleNote = title.getText().toString();
        String textNote = text.getText().toString();
        Date dateChange = new Date();
        long lastChange = dateChange.getTime();

        int containsDeadline;
        if (date == 0) {
            containsDeadline = 0;
        } else {
            containsDeadline = 1;
        }

        Note myNote = new Note(idNoteBundle, titleNote, textNote,
                checkDeadline, date, lastChange, containsDeadline);
        return myNote;
    }

}
