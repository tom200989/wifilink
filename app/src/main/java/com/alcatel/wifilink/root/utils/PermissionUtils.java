package com.alcatel.wifilink.root.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by qianli.ma on 2018/2/8 0008.
 */

public class PermissionUtils {
    private Activity context;
    public static int REQUEST_EXTERNAL_STORAGE = 0x123;// 读写约定码

    public PermissionUtils(Activity context) {
        this.context = context;
    }

    /**
     * 获取读写外部设备权限
     */
    public void getWriteReadExtenalPermisson() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            int writePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int readPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
            // 权限本身不允许--> 请求
            if (writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(context, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            } else {// 权限本身允许--> 执行业务逻辑
                writeReadPermissonNext(null);
            }
        } else {
            writeReadPermissonNext(null);
        }
    }

    private OnWriteReadPermissonListener onWriteReadPermissonListener;

    // 接口OnWriteReadPermissonListener
    public interface OnWriteReadPermissonListener {
        void writeReadPermisson(Object attr);
    }

    // 对外方式setOnWriteReadPermissonListener
    public void setOnWriteReadPermissonListener(OnWriteReadPermissonListener onWriteReadPermissonListener) {
        this.onWriteReadPermissonListener = onWriteReadPermissonListener;
    }

    // 封装方法writeReadPermissonNext
    private void writeReadPermissonNext(Object attr) {
        if (onWriteReadPermissonListener != null) {
            onWriteReadPermissonListener.writeReadPermisson(attr);
        }
    }
}
