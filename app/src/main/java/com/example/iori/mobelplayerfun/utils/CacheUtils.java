package com.example.iori.mobelplayerfun.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class CacheUtils {

    /**
     * Keep data
     * @param context
     * @param key
     * @param values
     */
    public static void putString(Context context, String key, String values){
        SharedPreferences sharedPreferences = context.getSharedPreferences("atmedia", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, values).commit();
    }

    /**
     * Get data
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("atmedia", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }
}
