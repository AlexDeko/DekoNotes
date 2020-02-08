package com.homework1_3.dekonotes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.homework1_3.dekonotes.R;

public class EnterPinActivity extends AppCompatActivity {
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

    private boolean pinCodeEntered;
    private static final int MAX_ENTER = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);

        initViews();
        setClickNumberOnKeyboard();
        setClickDeleteLastInput();




    }

    private void initViews(){
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

    private void setClickNumberOnKeyboard(){
        number0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        number1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        number3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        number4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        number5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        number6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        number6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        number7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        number8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        number9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setClickDeleteLastInput(){
        deleteLastInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
