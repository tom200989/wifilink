package com.alcatel.wifilink.root.ue.frag;

import android.view.View;

import com.alcatel.wifilink.R;

/*
 * Created by qianli.ma on 2019/8/19 0019.
 */
public class SmsFrag extends BaseFrag {
    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_sms;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o, view, s);
    }

    @Override
    public boolean onBackPresss() {
        return false;
    }
}
