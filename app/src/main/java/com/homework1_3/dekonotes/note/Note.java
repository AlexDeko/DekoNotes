package com.homework1_3.dekonotes.note;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Note {
    @PrimaryKey(autoGenerate = true)
    private long id;
  //  @ColumnInfo(name = "title")
    private String title;
  //  @ColumnInfo(name = "text")
    private String text;
   // @ColumnInfo(name = "deadline")
    private long dayDeadline;

    public Note(long id, String title, String text, long dayDeadline) {
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

    public long getDayDeadline() {
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

    public void setDayDeadline(long dayDeadline) {
        this.dayDeadline = dayDeadline;
    }
}
