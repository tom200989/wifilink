package com.alcatel.wifilink.root.helper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alcatel.wifilink.root.ue.activity.RefreshWifiRxActivity;
import com.alcatel.wifilink.root.utils.Logs;

public class CrashHanlder implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;// 自身接口
    private static CrashHanlder INSTANCE = new CrashHanlder();// CrashHandler实例
    private Context mContext;

    private CrashHanlder() {
    }
    public static CrashHanlder getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 1.获取系统默认的UncaughtException处理
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 2.设置该CrashHandler为程序的默认处理
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 异常捕获
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            // 默认处理方式
            // defaultHandler();
            
            // 自定义处理方式
            Logs.t("crash").vv("crash: "+ex.getMessage());
            Logs.t("ma_unknown").vv("CrashHandler--> uncaughtException");
            Intent intent = new Intent(mContext, RefreshWifiRxActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            System.exit(0);// 关闭已奔溃的app进程
        }
    }

    /**
     * 默认处理方式
     */
    private void defaultHandler() {
        Intent intent = new Intent(mContext, CrashDialog.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        System.exit(0);// 关闭已奔溃的app进程
    }

    /**
     * 自定义错误捕获
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        // 异常为空, 直接返回false
        if (ex == null) {
            return false;
        }
        // 打印错误
        printLog(ex);
        return true;
    }

    private void printLog(Throwable ex) {
        Log.v("crash", "crash cause: " + ex.getCause());
        Log.v("crash", "crash message: " + ex.getMessage());
        Log.v("crash", "crash localizedMessage: " + ex.getLocalizedMessage());
    }
}
