package com.example.s344224mappe1;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Locale;

public class Language {

    private static void setLand(Activity activity, String landskode) {
        Resources resources = activity.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(landskode));
        resources.updateConfiguration(configuration, displayMetrics);
    }

    public static void setLanguage(Activity activity, String landskode) {
        String localeCode = "";
        if (landskode.equals("Tysk")) {
            localeCode = "de";
        }
        setLand(activity, localeCode);

    }
}
