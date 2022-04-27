package com.example.s344224mappe2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SetPreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_preferences);

        getSupportActionBar().setTitle(R.string.toolbar_preferences);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings_container, new preferansefragment()).commit();

    }
}