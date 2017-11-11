package com.capstone.deptmanager.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Daehee on 2017-05-25.
 */

public class PrefUtil {
    // 푸시 토큰 값을 저장하는 키 정의
    public static final String KEY_PUSH_TOKEN = "keyPushToken";
    public static final String KEY_USER_ID = "keyUserId";
    public static final String KEY_USER_PW = "keyUserPw";
    public static final String KEY_BADGE_COUNT = "keyBadgeCount";

    public static void setPreference(Context context, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(KEY_PUSH_TOKEN, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static void setIntPreference(Context context, String key, int value) {
        SharedPreferences prefs = context.getSharedPreferences(KEY_PUSH_TOKEN, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    public static String getPreference(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(KEY_PUSH_TOKEN, Activity.MODE_PRIVATE);
        return prefs.getString(key, "");
    }
    public static int getIntPreference(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(KEY_PUSH_TOKEN, Activity.MODE_PRIVATE);
        return prefs.getInt(key, -1);
    }
    public static void rmPreference(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(KEY_PUSH_TOKEN, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.commit();
    }
} // end of class
