package com.homework1_3.dekonotes.note;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Note {
    @PrimaryKey
    private long id;
    private String title;
    private String text;
    private int dayDeadline;

    public Note(long id, String title, String text, int dayDeadline) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.dayDeadline = dayDeadline;
    }
    @Ignore
    public Note(long id, String text, int dayDeadline) {
        this.id = id;
        this.text = text;
        this.dayDeadline = dayDeadline;
    }
    @Ignore
    public Note(long id, String text) {
        this.id = id;
        this.text = text;
    }



    public String getTitle() {
        return title;
    }


    public String getText() {
        return text;
    }

    public int getDayDeadline() {
        return dayDeadline;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDayDeadline(int dayDeadline) {
        this.dayDeadline = dayDeadline;
    }
}
