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

public class SettingsActivity extends AppCompatActivity {

    private final static String keyBundlePin = "keyPin";
    private final static String keyBundleVisibleError = "keyPin";
    private final static String keyBundleEyesBoolean = "keyEyes";
    private EditText editNewPin;
    private Button btnSave;
    private ImageButton btnVisiblePin;
    private TextView error;
    private boolean close = true;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setMyToolbar();
        initViews();
        setBtnVisibleEyes(close);
        setBtn();
    }

    private void initViews() {
        editNewPin = findViewById(R.id.editPassword);
        btnSave = findViewById(R.id.btnSavePin);
        btnVisiblePin = findViewById(R.id.imgBtnVisible);
        error = findViewById(R.id.textError);
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

    private void setPinAndError(String pin) {
        if (pin.length() < 4) {
            error.setVisibility(View.VISIBLE);
        } else {
            error.setVisibility(View.INVISIBLE);
            KeyStore keyStore = App.getInstance().getKeyStore();
            keyStore.saveNew(editNewPin.getText().toString());
            Toast.makeText(SettingsActivity.this,
                    getString(R.string.toast_database_save),
                    Toast.LENGTH_SHORT).show();
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
                if (close){
                    close = false;
                } else {
                    close = true;
                }
                setBtnVisibleEyes(close);
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
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(keyBundleEyesBoolean, close);
        if (error.getVisibility() == View.VISIBLE){
            outState.putString(keyBundleVisibleError, editNewPin.getText().toString());
        }

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        close = savedInstanceState.getBoolean(keyBundleEyesBoolean);
        setBtnVisibleEyes(close);
        if (savedInstanceState.containsKey(keyBundleVisibleError)){
            setPinAndError(savedInstanceState.getString(keyBundlePin));
        }
    }
}
