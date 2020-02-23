package com.app.dekonotes.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.app.dekonotes.App;
import com.app.dekonotes.R;
import com.app.dekonotes.data.key.KeyStore;

public class EnterPinActivity extends AppCompatActivity {

    private final static String keyBundlePin = "keyPin";
    private Animation animAlpha;
    private Button number0;
    private Button number1;
    private Button number2;
    private Button number3;
    private Button number4;
    private Button number5;
    private Button number6;
    private Button number7;
    private Button number8;
    private Button number9;
    private Button deleteLastInput;
    private View zeroOval;
    private View firstOval;
    private View secondOval;
    private View thirdOval;
    private static final int MAX_ENTER = 4;
    private static final int MIN_ENTER = 0;
    private KeyStore keyStore = App.getInstance().getKeyStore();
    private StringBuilder pin = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);
        hasPin();
        initViews();
        setClickNumberOnKeyboard();
        setClickDeleteLastInput();
    }

    private void hasPin() {
        if (!keyStore.hasPin()) {
            Intent startSettings = new Intent(EnterPinActivity.this,
                    SettingsActivity.class);
            startActivity(startSettings);
        }
    }

    private void storePin() {
        if (keyStore.checkPin(pin.toString())) {
            Intent startMain = new Intent(EnterPinActivity.this, MainActivity.class);
            startActivity(startMain);
        } else {
            Toast.makeText(this, getString(R.string.error_pin), Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        number0 = findViewById(R.id.btnNumber0);
        number1 = findViewById(R.id.btnNumber1);
        number2 = findViewById(R.id.btnNumber2);
        number3 = findViewById(R.id.btnNumber3);
        number4 = findViewById(R.id.btnNumber4);
        number5 = findViewById(R.id.btnNumber5);
        number6 = findViewById(R.id.btnNumber6);
        number7 = findViewById(R.id.btnNumber7);
        number8 = findViewById(R.id.btnNumber8);
        number9 = findViewById(R.id.btnNumber9);
        deleteLastInput = findViewById(R.id.btnDeleteLastInput);
        zeroOval = findViewById(R.id.viewZeroOval);
        firstOval = findViewById(R.id.viewFirstOval);
        secondOval = findViewById(R.id.viewSecondOval);
        thirdOval = findViewById(R.id.viewThirdOval);
    }

    private void setNumberClickListener(Button button, final String number) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pin.length() < MAX_ENTER) {
                    pin.append(number);
                    setShape();
                }
            }
        });
    }

    private void setClickNumberOnKeyboard() {
        setNumberClickListener(number0, "0");
        setNumberClickListener(number1, "1");
        setNumberClickListener(number2, "2");
        setNumberClickListener(number3, "3");
        setNumberClickListener(number4, "4");
        setNumberClickListener(number5, "5");
        setNumberClickListener(number6, "6");
        setNumberClickListener(number7, "7");
        setNumberClickListener(number8, "8");
        setNumberClickListener(number9, "9");
    }

    private void setClickDeleteLastInput() {
        deleteLastInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                if (pin.length() > MIN_ENTER) {
                    deleteShape();
                }
            }
        });
    }

    private void setShape() {
        switch (pin.length()) {
            case 1:
                zeroOval.setBackgroundResource(R.drawable.shape);
                break;

            case 2:
                zeroOval.setBackgroundResource(R.drawable.shape);
                firstOval.setBackgroundResource(R.drawable.shape);
                break;

            case 3:
                zeroOval.setBackgroundResource(R.drawable.shape);
                firstOval.setBackgroundResource(R.drawable.shape);
                secondOval.setBackgroundResource(R.drawable.shape);
                break;

            case 4:
                zeroOval.setBackgroundResource(R.drawable.shape);
                firstOval.setBackgroundResource(R.drawable.shape);
                secondOval.setBackgroundResource(R.drawable.shape);
                thirdOval.setBackgroundResource(R.drawable.shape);
                storePin();
                break;
        }
    }

    private void deleteShape() {
        switch (pin.length()) {
            case 1:
                zeroOval.setBackgroundResource(R.drawable.shape_tint);
                break;

            case 2:
                firstOval.setBackgroundResource(R.drawable.shape_tint);
                break;

            case 3:
                secondOval.setBackgroundResource(R.drawable.shape_tint);
                break;

            case 4:
                thirdOval.setBackgroundResource(R.drawable.shape_tint);
                break;
        }
        int length = pin.length();
        if (length > 0) {
            pin.deleteCharAt(length - 1);
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(keyBundlePin, pin.toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pin.append(savedInstanceState.getString(keyBundlePin));
        setShape();
    }
}
