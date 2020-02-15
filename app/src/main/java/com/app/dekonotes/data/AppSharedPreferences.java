package com.app.dekonotes.data;

import android.content.SharedPreferences;

import com.app.dekonotes.key.KeyStore;

import java.util.HashMap;

import static java.util.Objects.hash;

public class AppSharedPreferences implements KeyStore {

    private static String PIN = "pin";
    private SharedPreferences keySharedPreferences;
    private String pinForHash;
    private int hashSalt;
    private HashMap hashMap = new HashMap();

    @Override
    public boolean hasPin() {
        if (hashMap.isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public boolean checkPin(String pin) {
        if (hashMap.containsKey(pin)){
            int hashTarget = (int) hashMap.get(pin);
            int hashSaltTarget = hash(pin + "b4z4wff3mgl" );
            if (hashTarget == hashSaltTarget){
             return true;
            }
        }
        return false;
    }

    @Override
    public void saveNew(String pin) {
        SharedPreferences.Editor myEditor = keySharedPreferences.edit();
        pinForHash = pin;
       // hash = pin.hashCode();
        hashSalt = hash(pinForHash + "b4z4wff3mgl" );
        hashMap.put(PIN, hashSalt);
      //  myEditor.putString(PIN, pin);
       // myEditor.apply();

    }
}
