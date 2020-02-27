package com.app.dekonotes.data.formatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateDeadlineFormatter implements Formatter {

    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
    private Date date = new Date();

    public DateDeadlineFormatter() {

    }

    @Override
    public String getFormatDate(long dayDeadline) {
        date.setTime(dayDeadline);
        return dateFormat.format(date.getTime());
    }
}
