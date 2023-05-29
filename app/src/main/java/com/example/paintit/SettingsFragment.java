package com.example.paintit;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.example.paintit.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        boolean soundEffectsEnabled = sharedPreferences.getBoolean("sound_effects", true);

        if (soundEffectsEnabled) {
            mediaPlayer = MediaPlayer.create(requireContext(), R.raw.button_click);
        } else {
            mediaPlayer = null;
        }

        SwitchPreferenceCompat soundEffectsSwitch = findPreference("sound_effects");
        soundEffectsSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean isEnabled = (boolean) newValue;
            if (isEnabled && mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(requireContext(), R.raw.button_click);
            } else if (!isEnabled && mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        boolean soundEffectsEnabled = sharedPreferences.getBoolean("sound_effects", true);

        if (soundEffectsEnabled) {
            mediaPlayer = MediaPlayer.create(requireContext(), R.raw.button_click);
        } else {
            mediaPlayer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
