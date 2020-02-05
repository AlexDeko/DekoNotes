package com.homework1_3.dekonotes.key;

public interface KeyStore {
    boolean hasPin();
    boolean checkPin(String pin);
    void saveNew(String pin);
}
