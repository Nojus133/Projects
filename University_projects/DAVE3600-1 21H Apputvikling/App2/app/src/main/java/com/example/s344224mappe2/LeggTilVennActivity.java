package com.example.s344224mappe2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LeggTilVennActivity extends AppCompatActivity {
    private ViewGroup view;
    private int felterMedTekst = 0;
    private Button btnLagre;
    private Venn venn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leggtil_venn);

        Intent data = getIntent();
        String toolbarNavn = data.getStringExtra("ToolbarNavn");
        venn = data.getParcelableExtra("Venn");


        getSupportActionBar().setTitle(toolbarNavn);
        view = findViewById(R.id.leggTilView);
        btnLagre = findViewById(R.id.btnLagre);

        if (venn != null) leggTilText(venn);

        for (int i = 0; i < view.getChildCount(); i++) {
            View child = (View) view.getChildAt(i);

            if (child instanceof TextInputLayout) {
                TextInputEditText editText = (TextInputEditText) ((TextInputLayout) child).getEditText();
                if (venn != null) {
                    ((TextInputLayout) child).setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    ((TextInputLayout) child).setEndIconDrawable(R.drawable.ic_baseline_check_circle_24);
                }
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (child.getId() == R.id.textField3) {
                            if (charSequence.toString().length() == 8 && i == 7) {
                                ((TextInputLayout) child).setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                                ((TextInputLayout) child).setEndIconDrawable(R.drawable.ic_baseline_check_circle_24);
                                felterMedTekst++;
                            }
                            else if (charSequence.toString().length() == 7 && i1 == 1 ) {
                                ((TextInputLayout) child).setEndIconMode(TextInputLayout.END_ICON_NONE);
                                felterMedTekst--;
                            }
                        }
                        else {
                            if (charSequence.toString().isEmpty()) {
                                ((TextInputLayout) child).setEndIconMode(TextInputLayout.END_ICON_NONE);
                                felterMedTekst--;
                            }
                            else if (i == 0) {
                                ((TextInputLayout) child).setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                                ((TextInputLayout) child).setEndIconDrawable(R.drawable.ic_baseline_check_circle_24);
                                felterMedTekst++;
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (felterMedTekst == view.getChildCount() - 2) btnLagre.setEnabled(true);
                        else btnLagre.setEnabled(false);
                    }
                });
            }
        }

        btnLagre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

    }

    public void saveData() {
        TextView idText = findViewById(R.id.text_id3);
        TextInputLayout fornavnEditText = findViewById(R.id.textField);
        TextInputLayout etternavnEditText = findViewById(R.id.txt_bestilling_dato);
        TextInputLayout telefonEditText = findViewById(R.id.textField3);

        String id = idText.getText().toString();
        String fornavn = fornavnEditText.getEditText().getText().toString();
        String etternavn = etternavnEditText.getEditText().getText().toString();
        String telefon = telefonEditText.getEditText().getText().toString();

        Venn venn;
        if (id.isEmpty()) venn = new Venn(fornavn, etternavn, telefon);
        else venn = new Venn(Long.parseLong(id), fornavn, etternavn, telefon);

        Intent data = new Intent();
        data.putExtra("Venn", venn);
        setResult(RESULT_OK, data);
        finish();
    }

    public void leggTilText(Venn venn) {
        btnLagre.setEnabled(true);
        felterMedTekst = view.getChildCount() - 2;
        TextView id = findViewById(R.id.text_id3);
        TextInputLayout fornavnEditText = findViewById(R.id.textField);
        TextInputLayout etternavnEditText = findViewById(R.id.txt_bestilling_dato);
        TextInputLayout telefonEditText = findViewById(R.id.textField3);

        id.setText(Long.toString(venn.get_id()));
        fornavnEditText.getEditText().setText(venn.getFirstName());
        etternavnEditText.getEditText().setText(venn.getLastName());
        telefonEditText.getEditText().setText(venn.getTelefon());
    }
}