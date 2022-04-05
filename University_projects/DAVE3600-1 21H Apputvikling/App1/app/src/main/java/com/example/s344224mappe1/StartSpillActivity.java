package com.example.s344224mappe1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartSpillActivity extends AppCompatActivity implements View.OnClickListener{
    private String regnestykke;
    private String sprak;

    TextView textView;
    Button btnSpill;
    Button btnTilbake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        regnestykke = intent.getStringExtra(getString(R.string.ANTALL_REGNESTYKKER));
        sprak = intent.getStringExtra(getString(R.string.VALGT_SPRAK));
        Language.setLanguage(this, sprak);

        setContentView(R.layout.activity_start_spill);

        btnSpill = findViewById(R.id.btnSpill);
        btnTilbake = findViewById(R.id.btnTilbake);
        textView = findViewById(R.id.textSporsmalTall);

        btnSpill.setOnClickListener(this);
        btnTilbake.setOnClickListener(this);

        textView.setText(regnestykke);
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSpill:
                Intent intent = new Intent(this, SpillActivity.class);
                intent.putExtra(getString(R.string.ANTALL_REGNESTYKKER), regnestykke);
                intent.putExtra(getString(R.string.VALGT_SPRAK), sprak);
                startActivity(intent);
                finish();
                break;
            case R.id.btnTilbake:
                finish();
                break;
        }
    }


}