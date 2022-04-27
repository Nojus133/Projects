package com.example.s344224mappe3;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;

public class ProgressButton {
    private ConstraintLayout layout;
    private MaterialButton button;
    private ProgressBar progressBar;
    private ColorStateList defaultColorButtonText;
    private ColorStateList defaultColorButtonIcon;

    public ProgressButton(Context context, View view) {
        layout = view.findViewById(R.id.contstraint_layout);
        button = view.findViewById(R.id.btn_main);
        progressBar = view.findViewById(R.id.progressBar);
        defaultColorButtonText = button.getTextColors();
        defaultColorButtonIcon = button.getIconTint();
    }

    public void startLoader() {
        button.setClickable(false);
        progressBar.setVisibility(View.VISIBLE);
        button.setTextColor(Color.parseColor("#00FFFFFF"));
        button.setIconTint(ColorStateList.valueOf(Color.TRANSPARENT));
    }

    public void stopLoader() {
        button.setClickable(true);
        progressBar.setVisibility(View.GONE);
        button.setTextColor(defaultColorButtonText);
        button.setIconTint(defaultColorButtonIcon);
    }

    public void setDisabled(boolean bool) {
        button.setEnabled(!bool);
    }

    public Button getButton() {
        return button;
    }
}
