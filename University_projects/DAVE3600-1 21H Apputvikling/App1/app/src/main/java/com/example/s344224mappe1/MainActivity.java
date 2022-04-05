package com.example.s344224mappe1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private String regnestykke;
    private String sprak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        regnestykke = Preference.getPreference(this, getString(R.string.ANTALL_REGNESTYKKER), getString(R.string.default_preference_antall_regnestykker));
        sprak = Preference.getPreference(this, getString(R.string.VALGT_SPRAK), getString(R.string.default_preference_valgt_sprak));
        Language.setLanguage(this, sprak);

        setContentView(R.layout.activity_main);

        findViewById(R.id.btnStartSpill).setOnClickListener(this);
        findViewById(R.id.btnSeStatistikk).setOnClickListener(this);
        findViewById(R.id.btnPreferanser).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnStartSpill:
                intent = new Intent(this, StartSpillActivity.class);
                intent.putExtra(getString(R.string.ANTALL_REGNESTYKKER), regnestykke);
                intent.putExtra(getString(R.string.VALGT_SPRAK), sprak);
                startActivity(intent);
                break;
            case R.id.btnSeStatistikk:
                intent = new Intent(this, StatistikkActivity.class);
                intent.putExtra(getString(R.string.VALGT_SPRAK), sprak);
                startActivity(intent);
                break;
            case R.id.btnPreferanser:
                intent = new Intent(this, PreferanserActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void onPause() {
        super.onPause();
    }

    public void onRestart() {
        super.onRestart();
        recreate();
    }

}