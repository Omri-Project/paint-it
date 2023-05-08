package com.example.paintit;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        boolean soundEffectsEnabled = sharedPreferences.getBoolean("sound_effects", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (soundEffectsEnabled){
            editor.putBoolean("sounds_enabled", true);
            editor.apply();
        }
        else {
            editor.putBoolean("sounds_enabled", false);
            editor.apply();
        }


    }
}
