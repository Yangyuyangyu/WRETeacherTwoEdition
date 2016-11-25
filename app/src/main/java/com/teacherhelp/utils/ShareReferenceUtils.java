package com.teacherhelp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Mr-fang on 2016/1/13.
 */
public class ShareReferenceUtils {

    private static SharedPreferences sharedPreferences;

    private static SharedPreferences.Editor editor;

    /**
     * 初始化框架（在application中配置）
     *
     * @param context
     */
    public static void init(Context context) {

        sharedPreferences = context.getSharedPreferences("com.teacherhelp", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    /**
     * 存入键值队
     *
     * @param key
     * @param value
     */
    public static void putValue(String key, String value) {

        editor.putString(key, value);
        editor.commit();

    }

    /**
     * 获取值
     *
     * @param key
     * @return
     */
    public static String getValue(String key) {

        return sharedPreferences.getString(key, null);

    }
}
