package com.capstone.deptmanager.fragment;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.capstone.deptmanager.R;

/**
 * Created by daehee on 2017. 10. 30..
 */

public class PrefsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.set);

        Preference btnVerInfo = findPreference("btnVerInfo");
        btnVerInfo.setOnPreferenceClickListener(prefClickListener);


    } // end of onCreate

    private Preference.OnPreferenceClickListener prefClickListener = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();
            switch (key) {
                case "btnVerInfo":
                    showDevInfo();
                    break;
            } // end of switch
            return true;
        }
    };

    public void showDevInfo() {
        LayoutInflater lif = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = lif.inflate(R.layout.dialog_dev_info, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("")
                .setPositiveButton("확인", null)
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setView(view);
        dialog.show();
    } // end of showDevInfo

} // end of class
