package com.example.s344224mappe2;

import static com.example.s344224mappe2.Utils.restartService;
import static com.example.s344224mappe2.Utils.tillattService;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;

import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

public class preferansefragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {
    TimePreference timePreference;
    SwitchPreferenceCompat allowNotifications;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        allowNotifications = findPreference("notifikasjoner");
        timePreference = findPreference("tid");
        timePreference.setEnabled(allowNotifications.isChecked());
        if (timePreference != null) {
            timePreference.setSummary("Påminnelse skal sendes kl. "+timePreference.timeToString(timePreference.getHour(), timePreference.getMinute()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference pref = findPreference(s);

        if (pref instanceof SwitchPreferenceCompat) {
            SwitchPreferenceCompat switchPreference = (SwitchPreferenceCompat) pref;
            timePreference.setEnabled(switchPreference.isChecked());
            tillattService(this.getContext(), switchPreference.isChecked());
        }

        if (pref instanceof TimePreference) {
            TimePreference timePreference = (TimePreference) pref;
            pref.setSummary("Påminnelse skal sendes kl. "+timePreference.timeToString(timePreference.getHour(), timePreference.getMinute()));
            restartService(this.getContext());
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        return false;
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment dialogFragment = null;
        if (preference instanceof TimePreference) {
            dialogFragment = new TimeDialog();
            Bundle bundle = new Bundle(1);
            bundle.putString("key", preference.getKey());
            dialogFragment.setArguments(bundle);
        }

        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getParentFragmentManager(), "time picker");
        }
        else {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
