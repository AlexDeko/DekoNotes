package com.app.dekonotes.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dekonotes.App;
import com.app.dekonotes.R;
import com.app.dekonotes.data.key.KeyStore;
import com.app.dekonotes.life.methods.DoubleBackPressed;

public class SettingsActivity extends AppCompatActivity {

    private final static String keyBundlePin = "keyPin";
    private final static String keyBundleVisibleError = "keyPin";
    private final static String keyBundleEyesBoolean = "keyEyes";
    private final static String keyBundleOldVisibleError = "keyOldPin";
    private final static String keyBundleOldEyesBoolean = "keyOldEyes";
    private EditText editNewPin;
    private Button btnSave;
    private ImageButton btnVisiblePin;
    private TextView error;
    private EditText editOldPin;
    private ImageButton btnVisibleOldPin;
    private TextView errorOldPin;
    private boolean close = true;
    private boolean closeOld = true;
    private boolean correctOldPin = false;
    private Toolbar myToolbar;
    private KeyStore keyStore = App.getInstance().getKeyStore();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setMyToolbar();
        initViews();
        hasOldPin();
        setBtnVisibleEyes(close);
        setBtnVisibleEyesOld(closeOld);
        setBtn();
    }

    private void hasOldPin() {
        if (!keyStore.hasPin()) {
            errorOldPin.setVisibility(View.GONE);
            editOldPin.setVisibility(View.GONE);
            btnVisibleOldPin.setVisibility(View.GONE);
            TextView enterPin = findViewById(R.id.titleEnterOldPin);
            enterPin.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        editNewPin = findViewById(R.id.editPassword);
        btnSave = findViewById(R.id.btnSavePin);
        btnVisiblePin = findViewById(R.id.imgBtnVisible);
        error = findViewById(R.id.textError);
        editOldPin = findViewById(R.id.editPasswordOld);
        btnVisibleOldPin = findViewById(R.id.imgBtnVisibleOldPin);
        errorOldPin = findViewById(R.id.textErrorForOldPin);
    }

    private void setMyToolbar() {
        myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.settings);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setBtnVisibleEyes(boolean close) {
        if (!close) {
            btnVisiblePin.setImageResource(R.drawable.ic_open_eye);
            editNewPin.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
        } else {
            btnVisiblePin.setImageResource(R.drawable.ic_close_eye);
            editNewPin.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
        }
    }

    private void setBtnVisibleEyesOld(boolean closeOld) {
        if (!closeOld) {
            btnVisibleOldPin.setImageResource(R.drawable.ic_open_eye);
            editOldPin.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
        } else {
            btnVisibleOldPin.setImageResource(R.drawable.ic_close_eye);
            editOldPin.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
        }
    }

    private void setOldPinAndError(String pin) {
        if (keyStore.checkPin(pin)) {
            errorOldPin.setVisibility(View.INVISIBLE);
            savePin();
        } else {
            errorOldPin.setText(R.string.error_enter_old_pin);
            errorOldPin.setVisibility(View.VISIBLE);
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
            errorOldPin.setVisibility(View.VISIBLE);
        } else if (keyStore.hasPin()) {
            errorOldPin.setText(R.string.error_enter_newPin);
            errorOldPin.setVisibility(View.INVISIBLE);
            correctOldPin = true;
        }
        if (pin.length() < 4) {
            error.setVisibility(View.VISIBLE);
        } else {
            error.setVisibility(View.INVISIBLE);
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

        btnVisiblePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close = !close;
                setBtnVisibleEyes(close);
            }
        });

        btnVisibleOldPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeOld = !closeOld;
                setBtnVisibleEyesOld(closeOld);
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(keyBundleEyesBoolean, close);
        if (error.getVisibility() == View.VISIBLE) {
            outState.putString(keyBundleVisibleError, editNewPin.getText().toString());
        }
        if (keyStore.hasPin()) {
            outState.putBoolean(keyBundleOldEyesBoolean, closeOld);
            if (errorOldPin.getVisibility() == View.VISIBLE) {
                outState.putString(keyBundleOldVisibleError, editOldPin.getText().toString());
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        close = savedInstanceState.getBoolean(keyBundleEyesBoolean);
        setBtnVisibleEyes(close);
        if (savedInstanceState.containsKey(keyBundleVisibleError)) {
            setPinAndError(savedInstanceState.getString(keyBundleVisibleError));
        }
        if (keyStore.hasPin()) {
            closeOld = savedInstanceState.getBoolean(keyBundleOldEyesBoolean);
            setBtnVisibleEyesOld(closeOld);
            if (savedInstanceState.containsKey(keyBundleOldVisibleError)) {
                setPinAndError(savedInstanceState.getString(keyBundleOldVisibleError));
            }
        }
    }
}
