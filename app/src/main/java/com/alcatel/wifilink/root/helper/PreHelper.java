package com.alcatel.wifilink.root.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.alcatel.wifilink.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianli.ma on 2018/2/26 0026.
 */

public class PreHelper {
    private Context context;
    private List<Drawable> signals;// 信号强度集合

    public PreHelper(Context context) {
        this.context = context;
        initRes();
    }

    private void initRes() {
        signals = new ArrayList<>();
        signals.add(context.getResources().getDrawable(R.drawable.mw_signal_0));
        signals.add(context.getResources().getDrawable(R.drawable.mw_signal_1));
        signals.add(context.getResources().getDrawable(R.drawable.mw_signal_2));
        signals.add(context.getResources().getDrawable(R.drawable.mw_signal_3));
        signals.add(context.getResources().getDrawable(R.drawable.mw_signal_4));
        signals.add(context.getResources().getDrawable(R.drawable.mw_signal_4));
    }

    /**
     * 获取网络类型
     *
     * @param level
     * @return
     */
    public String getMobileType(int level) {
        String text = context.getString(R.string.home_no_service);
        if (level <= 0) {
            return text;
        } else if (level >= 1 & level <= 2) {
            return context.getString(R.string.home_network_type_2g);
        } else if (level >= 3 & level <= 7) {
            return context.getString(R.string.home_network_type_3g_plus);
        } else if (level >= 8 & level <= 9) {
            return context.getString(R.string.home_network_type_4g);
        } else if (level >= 10 & level <= 12) {
            return context.getString(R.string.home_network_type_2g);
        }
        return text;
    }

    /**
     * 获取强度图片
     *
     * @param level
     * @return
     */
    public Drawable getStrenght(int level) {
        return signals.get(level <= 0 ? 0 : level);
    }
}
