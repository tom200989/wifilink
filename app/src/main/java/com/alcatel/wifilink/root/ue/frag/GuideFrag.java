package com.alcatel.wifilink.root.ue.frag;

import android.view.View;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.RootCons;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.alcatel.wifilink.root.widget.HH70_GuideWidget;
import com.hiber.hiber.RootFrag;
import com.hiber.tools.ShareUtils;

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/15 0015.
 */
public class GuideFrag extends RootFrag {

    @BindView(R.id.wd_guide)
    HH70_GuideWidget wdGuide;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_guide;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        wdGuide.setOnClickGuideListener(() -> {
            ShareUtils.set(RootCons.SP_GUIDE, true);// 保存向导缓存
            String cacheDevice = ShareUtils.get(RootCons.DEVICE_NAME, RootCons.DEVICE_NAME_DEFAULT);
            boolean isMwDev = RootUtils.isMWDEV(cacheDevice);// 是否为MW系列
            toFrag(getClass(), isMwDev ? LoginFrag_mw.class : LoginFrag.class, null, true);
        });
    }

    @Override
    public boolean onBackPresss() {
        return false;
    }

}
