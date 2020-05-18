package com.example.vew;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    SwitchPreferenceCompat s;
    Boolean dark_mode = false;
    View view;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        s = (SwitchPreferenceCompat) findPreference("Dark Mode");

        s.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                dark_mode = !dark_mode;

                if(dark_mode){
                    view.setBackgroundColor(Color.parseColor("#121212"));
                    ((SettingsActivity) getActivity()).setTheme(true);
                }
                else{
                    view.setBackgroundColor(Color.parseColor("#7E7E7E"));
                    ((SettingsActivity) getActivity()).setTheme(false);
                }
                return false;
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = super.onCreateView(inflater,container,savedInstanceState);


        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        dark_mode = settings.getBoolean("Dark Mode", false);

        if (dark_mode) {
            view.setBackgroundColor(Color.parseColor("#121212"));
        }
        else {
            view.setBackgroundColor(Color.parseColor("#7E7E7E"));
        }

        return view;
    }

}
