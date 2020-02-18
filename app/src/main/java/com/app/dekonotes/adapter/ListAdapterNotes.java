package com.app.dekonotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.dekonotes.R;
import com.app.dekonotes.data.note.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ListAdapterNotes extends BaseAdapter {

    private List<Note> listNotes = Collections.emptyList();
    private LayoutInflater inflater;

    public ListAdapterNotes(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listNotes != null ? listNotes.size() : 0;
    }

    public void setItems(final @NonNull List<Note> listNotes) {
        this.listNotes = listNotes;
    }

    @Override
    public Note getItem(int position) {
        return listNotes != null ? listNotes.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final  View currentView;

        if(convertView != null){
            currentView = convertView;
        } else {
            currentView = inflater.inflate(R.layout.list_item, parent, false);
        }

        Note note = getItem(position);
        if(note != null){
            TextView titleView = currentView.findViewById(R.id.titleItem1);
            TextView textView = currentView.findViewById(R.id.textItem2);
            TextView deadlineView = currentView.findViewById(R.id.deadlineItem3);
            if (note.getTitle().equals("Пусто")){
                titleView.setVisibility(View.GONE);
            } else {
                titleView.setVisibility(View.VISIBLE);
                titleView.setText(note.getTitle());
            }
            if (note.getText().equals("Пусто")){
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(note.getText());
            }
            if (note.getDayDeadline() == 0){
                deadlineView.setVisibility(View.GONE);
            } else {
                deadlineView.setVisibility(View.VISIBLE);
                Date date = new Date();
                date.setTime(note.getDayDeadline());
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                String deadline = dateFormat.format(date.getTime());
                deadlineView.setText(deadline);
            }
        }
        return currentView;
    }


}
