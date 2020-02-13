package com.app.dekonotes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.dekonotes.R;

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
    private static final int MIN_ENTER = 0;
    int countShape = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);

        storePin();
        initViews();
        setClickNumberOnKeyboard();
        setClickDeleteLastInput();



    }

    private void storePin(){

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

                if(countShape < MAX_ENTER){
                    setShape();
                    countShape += 1;
                }
            }
        });

        number1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countShape < MAX_ENTER){
                    setShape();
                    countShape += 1;
                }
            }
        });

        number3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countShape < MAX_ENTER){
                    setShape();
                    countShape += 1;
                }
            }
        });

        number4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countShape < MAX_ENTER){
                    setShape();
                    countShape += 1;
                }
            }
        });

        number5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countShape < MAX_ENTER){
                    setShape();
                    countShape += 1;
                }
            }
        });

        number6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countShape < MAX_ENTER){
                    setShape();
                    countShape += 1;
                }

            }
        });

        number6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countShape < MAX_ENTER){
                    setShape();
                    countShape += 1;
                }

            }
        });

        number7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countShape < MAX_ENTER){
                    setShape();
                    countShape += 1;
                }

            }
        });

        number8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countShape < MAX_ENTER){
                    setShape();
                    countShape += 1;
                }
            }
        });

        number9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countShape < MAX_ENTER){
                    setShape();
                    countShape += 1;
                }
            }
        });
    }

    private void setClickDeleteLastInput(){
        deleteLastInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countShape > MIN_ENTER){
                    deleteShape();
                    countShape -= 1;
                }
            }
        });
    }

    private void setShape(){

        switch (countShape){
            case 0:
                zeroOval.setBackgroundResource(R.drawable.shape);

                break;

            case 1:
                firstOval.setBackgroundResource(R.drawable.shape);

                break;

            case 2:
                secondOval.setBackgroundResource(R.drawable.shape);

                break;

            case 3:
                thirdOval.setBackgroundResource(R.drawable.shape);

                break;
        }
    }

    private void deleteShape(){

        switch (countShape){
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
    }
}
