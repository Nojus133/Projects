package com.example.s344224mappe3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ConfirmDeleteDialog extends DialogFragment {
    private DialogClickListener listener;
    private String title;
    private String message;
    private String positiveButton;
    private String negativeButton;

    public interface DialogClickListener {
        void onYesClick();
        void onNoClick();
    }

    public ConfirmDeleteDialog(DialogClickListener listener) {
        this.title = "Slett hus?";
        this.positiveButton = "Ja, slett";
        this.negativeButton = "Nei";
        this.listener = listener;
    }

    public void setDialogClickListener(DialogClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MaterialAlertDialogBuilder(getActivity())
                .setCancelable(true)
                .setTitle(title)
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        listener.onYesClick();
                    }
                })
                .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        listener.onNoClick();
                    }
                })
                .create();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPositiveButton() {
        return positiveButton;
    }

    public void setPositiveButton(String positiveButton) {
        this.positiveButton = positiveButton;
    }

    public String getNegativeButton() {
        return negativeButton;
    }

    public void setNegativeButton(String negativeButton) {
        this.negativeButton = negativeButton;
    }
}
