package com.app.dekonotes.data.key;

import android.content.Context;
import android.content.SharedPreferences;

import static java.util.Objects.hash;

public class AppSharedPreferences implements KeyStore {

    private static String PIN = "pin";
    private SharedPreferences keySharedPreferences;
    private static final String PREFERENCES_NAME = "keys";

    public AppSharedPreferences(Context context) {
        this.keySharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public boolean hasPin() {
        return keySharedPreferences.contains(PIN);
    }

    @Override
    public boolean checkPin(String pin) {
        if (keySharedPreferences.contains(pin)){
            int hashTarget = keySharedPreferences.getInt(pin, 0);
            int hashSaltTarget = hash(pin + "b4z4wff3mgl" );
            return hashTarget == hashSaltTarget;
        }
        return false;
    }

    @Override
    public void saveNew(String pin) {
        int hashSalt = hash(pin + "b4z4wff3mgl");
        keySharedPreferences.edit()
                .putInt(PIN, hashSalt)
                .apply();

    }
}
