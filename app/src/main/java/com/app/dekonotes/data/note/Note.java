package com.app.dekonotes.data.note;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

// http://developer.alexanderklimov.ru/android/theory/parcelable.php Чтобы передавать объекты через Bundle
@Entity
public class Note implements Parcelable {
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

    // Это всё boilerplate я создал по нажатию alt+enter, когда написал implements Parcelable.
    // Есть способы, как это вообще не писать, но для простоты понимания пока так
    protected Note(Parcel in) {
        id = in.readLong();
        title = in.readString();
        text = in.readString();
        check = in.readByte() != 0;
        dayDeadline = in.readLong();
        lastChange = in.readLong();
        containsDeadline = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(text);
        dest.writeByte((byte) (check ? 1 : 0));
        dest.writeLong(dayDeadline);
        dest.writeLong(lastChange);
        dest.writeInt(containsDeadline);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

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
