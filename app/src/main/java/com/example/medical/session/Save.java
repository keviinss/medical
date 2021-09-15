package com.example.medical.session;

import android.content.Context;
import android.content.SharedPreferences;

public class Save {

    public static void save (Context ctx, String name, String value){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("Medical", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, value);
        editor.apply();
    }

    public static String read(Context ctx, String name, String dafultvalue) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences("Medical", Context.MODE_PRIVATE);
        return sharedPreferences.getString(name, dafultvalue);
    }

}
