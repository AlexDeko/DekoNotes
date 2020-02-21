package com.app.dekonotes.data.note;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

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

    public boolean isCheck() {
        return check;
    }

    public long getLastChange() {
        return lastChange;
    }

    public int getContainsDeadline() {
        return containsDeadline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id == note.id &&
                check == note.check &&
                dayDeadline == note.dayDeadline &&
                lastChange == note.lastChange &&
                containsDeadline == note.containsDeadline &&
                Objects.equals(title, note.title) &&
                Objects.equals(text, note.text);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, title, text, check, dayDeadline, lastChange, containsDeadline);
    }
}
