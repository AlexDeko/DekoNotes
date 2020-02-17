package com.app.dekonotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.dekonotes.R;
import com.app.dekonotes.note.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListAdapterNotes extends BaseAdapter {

    private List<Note> listNotes;
    private LayoutInflater inflater;

    public ListAdapterNotes(List<Note> listNotes, Context context) {
        this.listNotes = listNotes;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listNotes != null ? listNotes.size() : 0;
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
            TextView tittleView = currentView.findViewById(R.id.titleItem1);
            TextView textView = currentView.findViewById(R.id.textItem2);
            TextView deadlineView = currentView.findViewById(R.id.deadlineItem3);
            if (note.getTitle().equals("Пусто")){
                tittleView.setVisibility(View.GONE);
            } else {
                tittleView.setText(note.getTitle());
            }
            if (note.getText().equals("Пусто")){
                textView.setVisibility(View.GONE);
            } else {
                textView.setText(note.getText());
            }
            if (note.getDayDeadline() == 0){
                deadlineView.setVisibility(View.GONE);
            } else {
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
