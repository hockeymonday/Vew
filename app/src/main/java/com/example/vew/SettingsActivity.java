package com.example.vew;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

public class SettingsActivity extends AppCompatActivity {
    private static String SWITCH_KEY = "Dark Mode";
    SharedPreferences settings;
    Boolean dark_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        dark_mode = settings.getBoolean("Dark Mode", false);
        if (dark_mode){
            setTheme(R.style.DarkTheme);
        }
        else{
            setTheme(R.style.AppTheme);
        }

        try { this.getSupportActionBar().hide(); }
        catch (NullPointerException e){ e.printStackTrace();}

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }

    public void setTheme(Boolean dark){
        this.finish();
        Intent i =new Intent(this, getClass());
        overridePendingTransition(0,0);
        this.startActivity(i);
    }

}
