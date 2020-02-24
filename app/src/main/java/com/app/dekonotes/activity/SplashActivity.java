package com.app.dekonotes.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.app.dekonotes.App;
import com.app.dekonotes.data.key.KeyStore;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KeyStore keyStore = App.getInstance().getKeyStore();
        Intent targetIntent;
        if (!keyStore.hasPin()) {
            targetIntent = new Intent(this,
                    SettingsActivity.class);

        } else {
            targetIntent = new Intent(this,
                    EnterPinActivity.class);
        }
        startActivity(targetIntent);
    }
}
