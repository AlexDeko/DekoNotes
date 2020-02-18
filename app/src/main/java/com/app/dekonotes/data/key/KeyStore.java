package com.app.dekonotes.data.key;


public interface KeyStore {

    boolean hasPin();
    boolean checkPin(String pin);
    void saveNew(String pin);
}
