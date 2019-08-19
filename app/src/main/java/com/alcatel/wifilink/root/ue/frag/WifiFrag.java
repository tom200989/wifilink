package com.alcatel.wifilink.root.ue.frag;

import android.view.View;

import com.alcatel.wifilink.R;

/*
 * Created by qianli.ma on 2019/8/19 0019.
 */
public class WifiFrag extends BaseFrag {
    @Override
    public int onInflateLayout() {
        // TODO: 2019/8/19 0019  
        return R.layout.hh70_frag_wifi;
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
