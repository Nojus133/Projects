package com.example.s344224mappe3;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class EndreHusActivity extends AppCompatActivity implements View.OnClickListener{
    TextInputLayout adresse, antallEtasjer, beskrivelse;
    TextInputEditText etAdresse, etAntallEtasjer, etBeskrivelse;
    View progressButtonLayout;
    Button btnLagre;
    int felterMedTekst;
    ProgressButton progressButton;
    private Hus hus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legg_til_hus);
        hus = getIntent().getExtras().getParcelable("hus");

        initInputViews();
        initButton();
        initActionBar();
        setInputText();

        etAdresse.setInputType(InputType.TYPE_NULL);
        etAdresse.setFocusable(false);
        adresse.setEnabled(false);
        etAdresse.setTextColor(getResources().getColor(R.color.quantum_grey600, null));

        listenForInputChange();
    }

    public void initInputViews() {
        adresse = (TextInputLayout) findViewById(R.id.txt_input_layout_adresse);
        etAdresse = (TextInputEditText) findViewById(R.id.edittext_adresse);
        etAntallEtasjer = (TextInputEditText) findViewById(R.id.edittext_antall_etasjer);
        etBeskrivelse = (TextInputEditText) findViewById(R.id.edittext_beskrivelse);
    }

    public void initButton() {
        progressButtonLayout = findViewById(R.id.btnLagre);
        progressButton = new ProgressButton(this, progressButtonLayout);
        btnLagre = progressButton.getButton();
        btnLagre.setOnClickListener(this);
        progressButton.setDisabled(false);
    }

    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Endre hus "+hus.getId());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
    }

    public void setInputText() {
        etAdresse.setText(hus.getAdresse());
        etBeskrivelse.setText(hus.getBeskrivelse());
        etAntallEtasjer.setText(Integer.toString(hus.getAntallEtasjer()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_main:
                progressButton.startLoader();
                lagre();
        }
    }

    public void lagre() {
        int id = hus.getId();
        int antallEtasjer = Integer.parseInt(etAntallEtasjer.getText().toString());
        String beskrivelse = etBeskrivelse.getText().toString();

        String url = "http://studdata.cs.oslomet.no/~dbuser6/endre.php";
        String urlParams = "id="+id+"&antallEtasjer="+antallEtasjer+"&beskrivelse="+beskrivelse+"";
        HttpPostAsyncTask task = new HttpPostAsyncTask(this);
        task.setHttpPostAsyncTaskListener(new HttpPostAsyncTask.HttpPostAsyncTaskListener() {
            @Override
            public void onResult(String resultCode) {
                handleResult(resultCode);
            }
        });
        task.execute(url, urlParams);
    }

    public void handleResult(String resultCode) {
        progressButton.stopLoader();
        int responseCode = Integer.parseInt(resultCode);
        if (responseCode == 200) {
            Toast.makeText(getApplicationContext(), "Hus endret", Toast.LENGTH_SHORT).show();
            Intent returnIntent = new Intent();
            returnIntent.putExtra("koordinater", hus.getKoordinater());
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(), "Kunne ikke endre hus", Toast.LENGTH_SHORT).show();
        }
    }

    public void listenForInputChange() {
        List<TextInputEditText> etList = new ArrayList<>();
        etList.add(etAntallEtasjer);
        etList.add(etBeskrivelse);
        felterMedTekst = etList.size();

        for (TextInputEditText editText : etList) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().isEmpty()) felterMedTekst--;
                    else if (i == 0 && i1 == 0) felterMedTekst++;
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (felterMedTekst == etList.size()) progressButton.setDisabled(false);
                    else progressButton.setDisabled(true);
                }
            });
        }
    }
}