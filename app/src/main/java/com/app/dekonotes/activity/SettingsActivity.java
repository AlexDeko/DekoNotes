package com.app.dekonotes.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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

    private final static String TAG = "Settings";
    private EditText editNewPin;
    private Button btnSave;
    private ImageButton btnVisiblePin;
    private TextView error;
    private int close = 1;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setMyToolbar();
        initViews();
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
                if (close == 1) {
                    btnVisiblePin.setImageResource(R.drawable.ic_open_eye);
                    editNewPin.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                    close -= 1;
                } else {
                    btnVisiblePin.setImageResource(R.drawable.ic_close_eye);
                    editNewPin.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                    close += 1;
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = editNewPin.getText().toString();
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
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }
}
