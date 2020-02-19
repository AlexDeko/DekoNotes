package com.app.dekonotes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.dekonotes.App;
import com.app.dekonotes.R;
import com.app.dekonotes.data.key.KeyStore;

public class EnterPinActivity extends AppCompatActivity {

    private final static String TAG = "EnterPin";
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
    int countShape = 0;
    KeyStore keyStore = App.getInstance().getKeyStore();
    StringBuilder pin = new StringBuilder();


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

    private void setClickNumberOnKeyboard() {
        number0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (countShape < MAX_ENTER) {
                    pin.append("0");
                    setShape();
                    countShape += 1;
                }
            }
        });

        number1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countShape < MAX_ENTER) {
                    pin.append("1");
                    setShape();
                    countShape += 1;
                }
            }
        });

        number2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countShape < MAX_ENTER) {
                    pin.append("2");
                    setShape();
                    countShape += 1;
                }
            }
        });

        number3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countShape < MAX_ENTER) {
                    pin.append("3");
                    setShape();
                    countShape += 1;
                }
            }
        });

        number4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countShape < MAX_ENTER) {
                    pin.append("4");
                    setShape();
                    countShape += 1;
                }
            }
        });

        number5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countShape < MAX_ENTER) {
                    pin.append("5");
                    setShape();
                    countShape += 1;
                }
            }
        });

        number6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countShape < MAX_ENTER) {
                    pin.append("6");
                    setShape();
                    countShape += 1;
                }

            }
        });

        number7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countShape < MAX_ENTER) {
                    pin.append("7");
                    setShape();
                    countShape += 1;
                }

            }
        });

        number8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countShape < MAX_ENTER) {
                    pin.append("8");
                    setShape();
                    countShape += 1;
                }
            }
        });

        number9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countShape < MAX_ENTER) {
                    pin.append("9");
                    setShape();
                    countShape += 1;
                }
            }
        });
    }

    private void setClickDeleteLastInput() {
        deleteLastInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countShape > MIN_ENTER) {
                    deleteShape();
                }
            }
        });
    }

    private void setShape() {

        switch (countShape) {
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
                storePin();

                break;
        }
    }

    private void deleteShape() {
        int length = pin.length();
        if (length > 0) {
            pin.deleteCharAt(length - 1);
        }

        switch (countShape) {
            case 1:
                zeroOval.setBackgroundResource(R.drawable.shape_tint);
                countShape -= 1;
                break;

            case 2:
                firstOval.setBackgroundResource(R.drawable.shape_tint);
                countShape -= 1;
                break;

            case 3:
                secondOval.setBackgroundResource(R.drawable.shape_tint);
                countShape -= 1;
                break;

            case 4:
                thirdOval.setBackgroundResource(R.drawable.shape_tint);
                countShape -= 1;
                break;
        }
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
