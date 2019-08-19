package com.alcatel.wifilink.root.ue.frag;

import android.view.View;

import com.alcatel.wifilink.R;

/*
 * Created by qianli.ma on 2019/8/16 0016.
 */
public class mainFrag extends BaseFrag {
    @Override
    public int onInflateLayout() {
        // TODO: 2019/8/16 0016  ROOT
        return R.layout.hh70_frag_main;
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
