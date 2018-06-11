package com.google.engedu.wordstack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatPreferenceActivity {

    Intent intent;
    int count=0;
    ListPreference lp;
    SharedPreferences sp;
    private PreferenceChangeListener preferenceChangeListener=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_screen);
        lp = (ListPreference)findPreference("words");
        lp.setDefaultValue("Two words");
        sp= PreferenceManager.getDefaultSharedPreferences(this);
        preferenceChangeListener=new PreferenceChangeListener();
        sp.registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        ApplySettings();



    }

    private class PreferenceChangeListener implements
            SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences prefs,
                                              String key) {
            //  Toast.makeText(SettingsActivity.this, "hello", Toast.LENGTH_SHORT).show();
            ApplySettings();
        }
    }

    public void ApplySettings() {
        count++;
        String words = sp.getString("words", "Two words");
        //Toast.makeText(this, Integer.toString(count), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, dice, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, player, Toast.LENGTH_SHORT).show();

        if (words.equals("Two words")) {
            intent = new Intent(this, MainActivity.class);
            lp.setSummary("Two words");
        } else {
            intent = new Intent(this, Main2Activity.class);
            lp.setSummary("Three words");
        }


    }

    @Override
    public void onBackPressed() {
        startActivity(intent);
    }
}

