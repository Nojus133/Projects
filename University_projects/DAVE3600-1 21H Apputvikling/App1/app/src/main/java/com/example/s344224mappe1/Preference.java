package com.example.s344224mappe1;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
    private static SharedPreferences sharedPreferences;

    public static void savePreference(Context context, String preferenceName, String preference) {
        if (preference.equals("Deutsch")) preference = "Tysk";

        sharedPreferences = context.getSharedPreferences(context.getString(R.string.SHARED_PREFERENCES), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preference);
        editor.apply();
    }

    public static String getPreference(Context context, String preferenceName, String defaultValue) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.SHARED_PREFERENCES), Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }
}
