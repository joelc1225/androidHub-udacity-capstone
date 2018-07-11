package com.joelcamargojr.androidhub.fragment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.joelcamargojr.androidhub.ListenFragment;
import com.joelcamargojr.androidhub.R;
import com.joelcamargojr.androidhub.ReadFragment;
import com.joelcamargojr.androidhub.WatchFragment;

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    public FragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ReadFragment();
            case 1:
                return new WatchFragment();
            case 2:
                return new ListenFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getResources().getString(R.string.read_label);
            case 1:
                return mContext.getResources().getString(R.string.watch_label);
            case 2:
                return mContext.getResources().getString(R.string.listen_label);
        }
        return null;
    }
}