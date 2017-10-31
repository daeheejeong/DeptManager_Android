package com.capstone.deptmanager.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.capstone.deptmanager.R;
import com.capstone.deptmanager.fragment.PrefsFragment;

/**
 * Created by daehee on 2017. 10. 30..
 */

public class SettingActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ((ImageView) findViewById(R.id.btnBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    } // end of onCreate

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        finish();
//        overridePendingTransition(R.anim.do_nothing, android.R.anim.slide_out_right);
    }
} // end of class
