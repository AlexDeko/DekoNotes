package com.app.dekonotes.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

    EditText editNewPin;
    Button btnSave;
    ImageButton btnVisiblePin;
    TextView error;
    int close = 1;
    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.settings);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editNewPin = findViewById(R.id.editPassword);
        btnSave = findViewById(R.id.btnSavePin);
        btnVisiblePin = findViewById(R.id.imgBtnVisible);
        error = findViewById(R.id.textError);
        setBtn();
    }

    private void setBtn(){

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnVisiblePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(close == 1){

                    btnVisiblePin.setImageResource(R.drawable.ic_open_eye);
                    editNewPin.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                    close -= 1;
                } else{
                    btnVisiblePin.setImageResource(R.drawable.ic_close_eye);
                    editNewPin.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                    close +=1;
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = editNewPin.getText().toString();
                if(pin.length() < 4){
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
}
