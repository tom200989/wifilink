package com.alcatel.wifilink.root.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by qianli.ma on 2017/7/31.
 */

public class ProgressUtils extends ProgressDialog {
    public ProgressUtils(Context context) {
        super(context);
    }

    public ProgressUtils(Context context, int theme) {
        super(context, theme);
    }

    public ProgressDialog getProgressPop(String msg) {
        setCanceledOnTouchOutside(false);
        setMessage(msg);
        show();
        return this;
    }

    public void dismissIt() {
        dismiss();
    }

}
