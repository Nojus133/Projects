// Generated by view binder compiler. Do not edit!
package com.example.s344224mappe2.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.s344224mappe2.R;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLeggtilVennBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final Button btnLagre;

  @NonNull
  public final RelativeLayout leggTilView;

  @NonNull
  public final TextInputLayout textField;

  @NonNull
  public final TextInputLayout textField3;

  @NonNull
  public final TextView textId3;

  @NonNull
  public final TextInputLayout txtBestillingDato;

  private ActivityLeggtilVennBinding(@NonNull RelativeLayout rootView, @NonNull Button btnLagre,
      @NonNull RelativeLayout leggTilView, @NonNull TextInputLayout textField,
      @NonNull TextInputLayout textField3, @NonNull TextView textId3,
      @NonNull TextInputLayout txtBestillingDato) {
    this.rootView = rootView;
    this.btnLagre = btnLagre;
    this.leggTilView = leggTilView;
    this.textField = textField;
    this.textField3 = textField3;
    this.textId3 = textId3;
    this.txtBestillingDato = txtBestillingDato;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLeggtilVennBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLeggtilVennBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_leggtil_venn, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLeggtilVennBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnLagre;
      Button btnLagre = ViewBindings.findChildViewById(rootView, id);
      if (btnLagre == null) {
        break missingId;
      }

      RelativeLayout leggTilView = (RelativeLayout) rootView;

      id = R.id.textField;
      TextInputLayout textField = ViewBindings.findChildViewById(rootView, id);
      if (textField == null) {
        break missingId;
      }

      id = R.id.textField3;
      TextInputLayout textField3 = ViewBindings.findChildViewById(rootView, id);
      if (textField3 == null) {
        break missingId;
      }

      id = R.id.text_id3;
      TextView textId3 = ViewBindings.findChildViewById(rootView, id);
      if (textId3 == null) {
        break missingId;
      }

      id = R.id.txt_bestilling_dato;
      TextInputLayout txtBestillingDato = ViewBindings.findChildViewById(rootView, id);
      if (txtBestillingDato == null) {
        break missingId;
      }

      return new ActivityLeggtilVennBinding((RelativeLayout) rootView, btnLagre, leggTilView,
          textField, textField3, textId3, txtBestillingDato);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}