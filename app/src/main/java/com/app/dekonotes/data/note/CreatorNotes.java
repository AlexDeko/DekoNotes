package com.app.dekonotes.data.note;

import java.util.Date;

public class CreatorNotes {

    private CreatorNotes() {
    }

    // Лучше статика в данном случае
    public static Note createNote(long idNote, String title, String text, boolean checkDeadline,
                           Date deadlineDate) {
        long date;
        if (checkDeadline) {
            date = deadlineDate.getTime();
        } else {
            date = 0;
        }

        Date dateChange = new Date();
        long lastChange = dateChange.getTime();

        int containsDeadline;
        if (date == 0) {
            containsDeadline = 0;
        } else {
            containsDeadline = 1;
        }

        return new Note(idNote, title, text,
                checkDeadline, date, lastChange, containsDeadline);
    }

}
