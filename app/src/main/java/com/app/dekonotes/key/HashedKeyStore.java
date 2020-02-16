package com.app.dekonotes.key;

import android.content.Context;
import android.content.SharedPreferences;

public class HashedKeyStore implements KeyStore {
    private static final String PREFERENCES_NAME = "store";
    private static final String KEY = "key";
    private SharedPreferences preferences;

    public HashedKeyStore(Context context) {
        this.preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
    @Override
    public boolean hasPin() {
        return preferences.contains(KEY);
    }
    @Override
    public boolean checkPin(String pin) {
        return PasswordEncoder.validatePassword(pin, preferences.getString(KEY, ""));
    }
    @Override
    public void saveNew(String pin) {
        preferences.edit()
                .putString(KEY, PasswordEncoder.generateStoringPasswordHash(pin))
                .apply();
    }
}
