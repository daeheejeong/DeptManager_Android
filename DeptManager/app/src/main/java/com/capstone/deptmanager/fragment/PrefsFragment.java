package com.capstone.deptmanager.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.capstone.deptmanager.R;

/**
 * Created by daehee on 2017. 10. 30..
 */

public class PrefsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.set);
    }

} // end of class
