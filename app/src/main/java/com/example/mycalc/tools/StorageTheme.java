package com.example.mycalc.tools;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class StorageTheme {

    private static final String ARG_SAVED_MODE = "ARG_SAVED_MODE";

    private final Context context;

    public StorageTheme(Context context) {
        this.context = context;
    }

    public void saveModeTheme(Integer mode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Themes", Context.MODE_PRIVATE);

        sharedPreferences.edit()
                .putInt(ARG_SAVED_MODE, mode)
                .apply();
    }

    public Integer getSavedModeTheme() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Themes", Context.MODE_PRIVATE);

        return sharedPreferences.getInt(ARG_SAVED_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }
}
