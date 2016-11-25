package com.teacherhelp.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/20.
 */
public class OrganizationAdapter extends FragmentStatePagerAdapter {
    private static String TAG = "CommodityAdapter";
    private static String[] mTitles;
    private ArrayList<Fragment> list_fragment;

    public OrganizationAdapter(FragmentManager fm, String[] titles, ArrayList<Fragment> list_fragment) {
        super(fm);
        this.mTitles = titles;
        this.list_fragment=list_fragment;
    }

    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        // super.restoreState(state, loader);
        //重写这个方法是为了防止在restoreState的时候导致应用崩溃，这样做虽然不太好，但是目前我也只能想到这种方法了
        Log.i(TAG, "restoreState");
    }
}
