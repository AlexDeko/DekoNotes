//package com.app.dekonotes.calendar;
//
//import android.app.DatePickerDialog;
//import android.app.TimePickerDialog;
//import android.widget.DatePicker;
//import android.widget.TimePicker;
//
//import com.app.dekonotes.App;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//public class CalendarDialog {
//    private Calendar todayCalendar;
//    DatePickerDialog datePickerDialog;
//
//    public CalendarDialog(Calendar todayCalendar) {
//        this.todayCalendar = todayCalendar;
//    }
//
//    public void setDateCalendar(Calendar todayCalendar){
//       // todayCalendar = Calendar.getInstance();
//        datePickerDialog = new DatePickerDialog(
//                App.getInstance(),
//                onDateSet,
//                todayCalendar.get(Calendar.YEAR),
//                todayCalendar.get(Calendar.MONTH),
//                todayCalendar.get(Calendar.DAY_OF_MONTH)
//        );
//        datePickerDialog.show();
//    }
//
//    public void setTimeCalendar(Calendar todayCalendar) {
//        new TimePickerDialog(App.getInstance(), onTimeSet,
//                todayCalendar.get(Calendar.HOUR_OF_DAY),
//                todayCalendar.get(Calendar.MINUTE), true)
//                .show();
//    }
//
//    TimePickerDialog.OnTimeSetListener onTimeSet= new TimePickerDialog.OnTimeSetListener() {
//        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            todayCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//            todayCalendar.set(Calendar.MINUTE, minute);
//
//        }
//    };
//
//    DatePickerDialog.OnDateSetListener onDateSet = new DatePickerDialog.OnDateSetListener() {
//        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            todayCalendar.set(Calendar.YEAR, year);
//            todayCalendar.set(Calendar.MONTH, monthOfYear);
//            todayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//         //   Date date = new Date();
//        //    date.setTime(todayCalendar.getTimeInMillis());
//         //   DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
//         //   dateCalendar.setText(dateFormat.format(date.getTime()));
//        }
//    };
//}
