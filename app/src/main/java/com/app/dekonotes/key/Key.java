package com.app.dekonotes.key;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//@Entity
public class Key {

  //  @PrimaryKey
    private String pin;

    public Key(String pin) {
        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
