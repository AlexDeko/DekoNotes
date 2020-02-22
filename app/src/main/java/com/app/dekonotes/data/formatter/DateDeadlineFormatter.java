package com.app.dekonotes.data.formatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateDeadlineFormatter implements Formatter {

    public DateDeadlineFormatter(){

    }

    @Override
    public String getFormatDate(long dayDeadline) {
        Date date = new Date();
        date.setTime(dayDeadline);
        DateFormat dateFormat =
                new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        return  dateFormat.format(date.getTime());
    }
}
