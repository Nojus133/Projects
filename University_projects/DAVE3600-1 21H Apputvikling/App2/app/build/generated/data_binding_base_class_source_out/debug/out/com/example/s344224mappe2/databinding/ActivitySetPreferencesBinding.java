// Generated by view binder compiler. Do not edit!
package com.example.s344224mappe2.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.example.s344224mappe2.R;
import java.lang.NullPointerException;
import java.lang.Override;

public final class ActivitySetPreferencesBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final LinearLayout settingsContainer;

  private ActivitySetPreferencesBinding(@NonNull LinearLayout rootView,
      @NonNull LinearLayout settingsContainer) {
    this.rootView = rootView;
    this.settingsContainer = settingsContainer;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySetPreferencesBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySetPreferencesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_set_preferences, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySetPreferencesBinding bind(@NonNull View rootView) {
    if (rootView == null) {
      throw new NullPointerException("rootView");
    }

    LinearLayout settingsContainer = (LinearLayout) rootView;

    return new ActivitySetPreferencesBinding((LinearLayout) rootView, settingsContainer);
  }
}