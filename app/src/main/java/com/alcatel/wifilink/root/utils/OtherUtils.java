package com.alcatel.wifilink.root.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.alcatel.wifilink.R;

/**
 * Created by qianli.ma on 2017/7/10.
 */

public class OtherUtils {

    /**
     * 显示等待进度条
     *
     * @param context
     */
    public static ProgressDialog showProgressPop(Context context) {
        ProgressDialog pgd = new ProgressDialog(context);
        pgd.setMessage(context.getString(R.string.connecting));
        pgd.setCanceledOnTouchOutside(false);
        if (!((Activity) context).isFinishing()) {
            pgd.show();
        }
        return pgd;
    }

    /**
     * 隐藏等待进度条
     *
     * @param pd
     */
    public static void hideProgressPop(ProgressDialog pd) {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

}

