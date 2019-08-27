package com.alcatel.wifilink.root.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class ToastTool {
    public static void show(Context context, final String tip) {
        String threadName = Thread.currentThread().getName();
        if (threadName.equalsIgnoreCase("main")) {
            Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
        } else {
            final Activity activity = (Activity) context;
            activity.runOnUiThread(() -> Toast.makeText(activity, tip, Toast.LENGTH_SHORT).show());
        }
    }

    public static void show(Context context, final int id) {
        String threadName = Thread.currentThread().getName();
        if (threadName.equalsIgnoreCase("main")) {
            Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
        } else {
            final Activity activity = (Activity) context;
            activity.runOnUiThread(() -> Toast.makeText(activity, id, Toast.LENGTH_SHORT).show());
        }
    }


    public static void showLong(Context context, final int id) {
        String threadName = Thread.currentThread().getName();
        if (threadName.equalsIgnoreCase("main")) {
            Toast.makeText(context, context.getString(id), Toast.LENGTH_LONG).show();
        } else {
            final Activity activity = (Activity) context;
            activity.runOnUiThread(() -> Toast.makeText(activity, context.getString(id), Toast.LENGTH_SHORT).show());
        }
    }

    public static void showLong(Context context, final String tip) {
        String threadName = Thread.currentThread().getName();
        if (threadName.equalsIgnoreCase("main")) {
            Toast.makeText(context, tip, Toast.LENGTH_LONG).show();
        } else {
            final Activity activity = (Activity) context;
            activity.runOnUiThread(() -> Toast.makeText(activity, tip, Toast.LENGTH_SHORT).show());
        }
    }
}
