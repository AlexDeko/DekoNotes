package com.app.dekonotes.key;

public class Key implements KeyStore{

    @Override
    public boolean hasPin() {
        return false;
    }

    @Override
    public boolean checkPin(String pin) {
        return false;
    }

    @Override
    public void saveNew(String pin) {

    }
}