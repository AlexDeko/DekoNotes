package com.app.dekonotes.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dekonotes.R;
import com.app.dekonotes.data.formatter.DateDeadlineFormatter;
import com.app.dekonotes.data.note.Note;

import java.util.Date;
import java.util.List;

public class RecyclerAdapterNotes extends RecyclerView.Adapter<RecyclerAdapterNotes
        .RecyclerViewHolder> {

    private List<Note> notesList;
    private final OnItemClickListener listener;
    private final OnItemLongClickListener listenerLong;

    public interface OnItemClickListener {
        void onItemClick(Note item);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Note item);
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView deadlineView;
        TextView textView;
        TextView titleView;

        private RecyclerViewHolder(final View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.titleItem1);
            textView = itemView.findViewById(R.id.textItem2);
            deadlineView = itemView.findViewById(R.id.deadlineItem3);
        }

        private void bind(final Note note, final OnItemClickListener listener,
                          final OnItemLongClickListener listenerLong) {

            if (TextUtils.isEmpty(note.getTitle())) {
                titleView.setVisibility(View.GONE);
            } else {
                titleView.setVisibility(View.VISIBLE);
                titleView.setText(note.getTitle());
            }
            if (TextUtils.isEmpty(note.getText())) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(note.getText());
            }
            if (note.getDayDeadline() == 0) {
                deadlineView.setVisibility(View.GONE);
            } else {
                deadlineView.setVisibility(View.VISIBLE);
                deadlineView.setText(new DateDeadlineFormatter()
                        .getFormatDate(note.getDayDeadline()));
                if (note.getDayDeadline() < new Date().getTime()) {
                    deadlineView.setTextColor((ContextCompat.getColor(deadlineView.getContext(),
                            R.color.colorAccent)));
                } else {
                    deadlineView.setTextColor((ContextCompat.getColor(deadlineView.getContext(),
                            R.color.colorBlackText)));
                }
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(note);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listenerLong.onItemLongClick(note);
                    return true;
                }
            });
        }
    }

    public RecyclerAdapterNotes(List<Note> items, OnItemClickListener listener,
                                OnItemLongClickListener listenerLong) {
        this.notesList = items;
        this.listener = listener;
        this.listenerLong = listenerLong;
    }

    public List<Note> getList(){
        return notesList;
    }

    public void setData(List<Note> newNoteList){
        notesList = newNoteList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);


        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        holder.bind(notesList.get(position), listener, listenerLong);
    }

    @Override
    public int getItemCount() {
        return notesList != null ? notesList.size() : 0;
    }

}