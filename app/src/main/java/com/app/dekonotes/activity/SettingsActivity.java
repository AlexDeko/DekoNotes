package com.app.dekonotes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.dekonotes.App;
import com.app.dekonotes.R;
import com.app.dekonotes.data.key.KeyStore;
import com.app.dekonotes.life.methods.DoubleBackPressed;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    private TextInputLayout inputPasswordOld;
    private TextInputLayout inputPasswordNew;
    private EditText editNewPin;
    private Button btnSave;
    private EditText editOldPin;
    private boolean correctOldPin = false;
    private Toolbar myToolbar;
    private KeyStore keyStore = App.getInstance().getKeyStore();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setMyToolbar();
        initViews();
        setBtn();
    }

    private void initViews() {
        inputPasswordOld = findViewById(R.id.inputPasswordOld);
        inputPasswordNew = findViewById(R.id.inputPasswordNew);
        editNewPin = findViewById(R.id.editPassword);
        btnSave = findViewById(R.id.btnSavePin);
        editOldPin = findViewById(R.id.editPasswordOld);
    }

    private void setMyToolbar() {
        myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.settings);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setOldPinAndError(String pin) {
        if (keyStore.checkPin(pin)) {
            inputPasswordOld.setError(null);
            savePin();
        } else {
            inputPasswordOld.setError(getString(R.string.error_enter_old_pin));
        }
    }

    private void savePin() {
        keyStore.saveNew(editNewPin.getText().toString());
        Toast.makeText(SettingsActivity.this,
                getString(R.string.toast_database_save),
                Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void setPinAndError(String pin) {
        if (editOldPin.getText().toString().length() < 4 && keyStore.hasPin()) {
            inputPasswordOld.setError(getString(R.string.error_enter_old_pin));
        } else if (keyStore.hasPin()) {
            inputPasswordOld.setError(null);
            correctOldPin = true;
        }

        if (pin.length() < 4) {
            inputPasswordNew.setError(getString(R.string.error_enter_newPin));
        } else {
            inputPasswordNew.setError(null);
            if (keyStore.hasPin() && correctOldPin) {
                setOldPinAndError(editOldPin.getText().toString());
            } else if (!keyStore.hasPin()) {
                savePin();
            }
        }
    }

    private void setBtn() {
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPinAndError(editNewPin.getText().toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!keyStore.hasPin()) {
            DoubleBackPressed.onBackPressed(SettingsActivity.this,
                    getString(R.string.toast_againOnBackPressed));
        } else {
            super.onBackPressed();
        }
    }
}
