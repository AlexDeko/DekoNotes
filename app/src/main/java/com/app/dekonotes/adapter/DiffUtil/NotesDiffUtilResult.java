package com.app.dekonotes.adapter.DiffUtil;

import androidx.recyclerview.widget.DiffUtil;

import com.app.dekonotes.adapter.RecyclerAdapterNotes;
import com.app.dekonotes.data.note.Note;

import java.util.List;

public class NotesDiffUtilResult {

    public NotesDiffUtilResult() {

    }

    public void getDIffUtilResult(RecyclerAdapterNotes recyclerAdapter,
                                  List<Note> baseListNote) {
        NotesDiffUtilCallback notesDiffUtilCallback =
                new NotesDiffUtilCallback(recyclerAdapter.getList(), baseListNote);
        DiffUtil.DiffResult notesDiffResult =
                DiffUtil.calculateDiff(notesDiffUtilCallback);
        recyclerAdapter.setData(baseListNote);
        notesDiffResult.dispatchUpdatesTo(recyclerAdapter);
    }
}
