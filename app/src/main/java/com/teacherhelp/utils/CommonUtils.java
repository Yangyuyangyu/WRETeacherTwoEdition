package com.teacherhelp.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Mr-fang on 2016/1/26.
 */
public class CommonUtils {

    private static Context mContext;

    public static void init( Context applicationContext ){
        mContext = applicationContext;
    }

    public static void showToast(String msg){
        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
    }

    public static void tag(String tag){
        Log.i("TAG",tag);
    }

}
