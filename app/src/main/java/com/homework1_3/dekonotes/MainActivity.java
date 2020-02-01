package com.homework1_3.dekonotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setLogo(getDrawable(R.drawable.ic_notepad_12));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent targetIntent;
        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, getString(R.string.toast_settings),
                    Toast.LENGTH_LONG).show();
            targetIntent = new Intent(MainActivity.this, NotesActivity.class);
            startActivity(targetIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}