package com.miramicodigo.notesbeta.controller;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.miramicodigo.notesbeta.R;

public class SettingsFragment extends PreferenceFragment {


    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_settings);

        setRetainInstance(true);

    }

}
