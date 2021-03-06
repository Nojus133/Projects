// Generated by view binder compiler. Do not edit!
package com.example.s344224mappe2.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.s344224mappe2.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ListItem2Binding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final ImageButton deleteItem3;

  @NonNull
  public final TextView textBestillingDato;

  @NonNull
  public final TextView textBestillingNavn;

  @NonNull
  public final TextView textVennId;

  private ListItem2Binding(@NonNull RelativeLayout rootView, @NonNull ImageButton deleteItem3,
      @NonNull TextView textBestillingDato, @NonNull TextView textBestillingNavn,
      @NonNull TextView textVennId) {
    this.rootView = rootView;
    this.deleteItem3 = deleteItem3;
    this.textBestillingDato = textBestillingDato;
    this.textBestillingNavn = textBestillingNavn;
    this.textVennId = textVennId;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ListItem2Binding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ListItem2Binding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.list_item2, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ListItem2Binding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.delete_item3;
      ImageButton deleteItem3 = ViewBindings.findChildViewById(rootView, id);
      if (deleteItem3 == null) {
        break missingId;
      }

      id = R.id.text_bestilling_dato;
      TextView textBestillingDato = ViewBindings.findChildViewById(rootView, id);
      if (textBestillingDato == null) {
        break missingId;
      }

      id = R.id.text_bestilling_navn;
      TextView textBestillingNavn = ViewBindings.findChildViewById(rootView, id);
      if (textBestillingNavn == null) {
        break missingId;
      }

      id = R.id.text_venn_id;
      TextView textVennId = ViewBindings.findChildViewById(rootView, id);
      if (textVennId == null) {
        break missingId;
      }

      return new ListItem2Binding((RelativeLayout) rootView, deleteItem3, textBestillingDato,
          textBestillingNavn, textVennId);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
