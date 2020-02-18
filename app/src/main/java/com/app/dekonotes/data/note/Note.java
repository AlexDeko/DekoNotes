package com.app.dekonotes.data.note;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Note {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "text")
    private String text;
    private boolean check;
    @ColumnInfo(name = "deadline")
    private long dayDeadline;
    @ColumnInfo(name = "last_change")
    private long lastChange;
    @ColumnInfo(name = "contains_deadline")
    private int containsDeadline;

    public Note(long id, String title, String text, boolean check, long dayDeadline,
                long lastChange, int containsDeadline) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.check = check;
        this.dayDeadline = dayDeadline;
        this.lastChange = lastChange;
        this.containsDeadline = containsDeadline;
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

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public void setDayDeadline(long dayDeadline) {
        this.dayDeadline = dayDeadline;
    }

    public long getLastChange() {
        return lastChange;
    }

    public void setLastChange(long lastChange) {
        this.lastChange = lastChange;
    }

    public int getContainsDeadline() {
        return containsDeadline;
    }

    public void setContainsDeadline(int containsDeadline) {
        this.containsDeadline = containsDeadline;
    }
}
