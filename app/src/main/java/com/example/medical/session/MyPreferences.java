package com.example.medical.session;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {
    private Context context;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private final String ACCOUNT_PREF = "ACCOUNT_PREF";
    public static final String IS_LOGINPASIEN = "IS_LOGINPASIEN";
    public static final String IS_LOGINDOKTER = "IS_LOGINDOKTER";
    public static final String IDUSER = "IDUSER";
    public static final String NAMALENGKAP = "NAMALENGKAP";
    public static final String EMAIL = "EMAIL";
    public static final String PASSWORD = "PASSWORD";
    public static final String STATUS = "STATUS";

    public MyPreferences(Context context) {
        this.context = context;
        this.sharedPreferences = this.context.getSharedPreferences(ACCOUNT_PREF, Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static SharedPreferences.Editor getEditorPreferences() {
        return editor;
    }

}
