package com.p_xhelper_smart.p_xhelper_smart.helper;

import android.os.Environment;

import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XBackupCallback;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

import org.xutils.common.Callback;

import java.io.File;
import java.io.IOException;

/*
 * Created by qianli.ma on 2019/8/1 0001.
 */
@SuppressWarnings("unchecked")
public class SetDeviceBackupHelper extends BaseHelper {

    /**
     * 触发备份 -- 并下载
     */
    public void setDeviceBackup() {
        prepareHelperNext();
        XSmart xSetDeviceBackup = new XSmart();
        xSetDeviceBackup.xMethod(XCons.METHOD_SET_DEVICE_BACKUP);
        xSetDeviceBackup.xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                reqBackup();// 请求下载地址
            }

            @Override
            public void appError(Throwable ex) {
                SetDeviceBackupFailedNext();
                doneHelperNext();
            }

            @Override
            public void fwError(FwError fwError) {
                SetDeviceBackupFailedNext();
                doneHelperNext();
            }

            @Override
            public void finish() {
            }
        });
    }

    /**
     * 请求下载地址
     */
    private void reqBackup() {
        String savePath = getSavepath();
        XSmart xBackup = new XSmart();
        xBackup.xBackup(savePath, new XBackupCallback() {
            @Override
            public void waiting() {
                waitingNext();
            }

            @Override
            public void start() {
                startNext();
            }

            @Override
            public void loading(long total, long current, boolean isDownloading) {
                downLoadingNext(total, current, isDownloading);
            }

            @Override
            public void success(File file) {
                downSuccessNext(file);
            }

            @Override
            public void appError(Throwable ex) {
                SetDeviceBackupFailedNext();
            }

            @Override
            public void cancel(Callback.CancelledException cex) {
                downCancelNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    /**
     * @return 获取保存路径 sdcard/smartlink/configure.bin
     */
    private String getSavepath() {
        File sdDir = Environment.getExternalStorageDirectory();
        // 创建根目录
        if (!sdDir.exists() | !sdDir.isDirectory()) {
            sdDir.mkdirs();
        }
        // 创建1级目录
        File smartDir = new File(sdDir.getAbsolutePath() + "/smartlink");
        if (!smartDir.exists() | !smartDir.isDirectory()) {
            smartDir.mkdirs();
        }
        // 创建文件
        File configFile = new File(smartDir.getAbsolutePath() + "/configure.bin");
        if (!configFile.exists() | configFile.isDirectory()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return configFile.getAbsolutePath();
    }

    private OnWaitingListener onWaitingListener;

    // Inteerface--> 接口OnWaitingListener
    public interface OnWaitingListener {
        void waiting();
    }

    // 对外方式setOnWaitingListener
    public void setOnWaitingListener(OnWaitingListener onWaitingListener) {
        this.onWaitingListener = onWaitingListener;
    }

    // 封装方法waitingNext
    private void waitingNext() {
        if (onWaitingListener != null) {
            onWaitingListener.waiting();
        }
    }

    private OnStartListener onStartListener;

    // Inteerface--> 接口OnStartListener
    public interface OnStartListener {
        void start();
    }

    // 对外方式setOnStartListener
    public void setOnStartListener(OnStartListener onStartListener) {
        this.onStartListener = onStartListener;
    }

    // 封装方法startNext
    private void startNext() {
        if (onStartListener != null) {
            onStartListener.start();
        }
    }

    private OnDownLoadingListener onDownLoadingListener;

    // Inteerface--> 接口OnDownLoadingListener
    public interface OnDownLoadingListener {
        void downLoading(long total, long current, boolean isDownloading);
    }

    // 对外方式setOnDownLoadingListener
    public void setOnDownLoadingListener(OnDownLoadingListener onDownLoadingListener) {
        this.onDownLoadingListener = onDownLoadingListener;
    }

    // 封装方法downLoadingNext
    private void downLoadingNext(long total, long current, boolean isDownloading) {
        if (onDownLoadingListener != null) {
            onDownLoadingListener.downLoading(total, current, isDownloading);
        }
    }

    private OnDownSuccessListener onDownSuccessListener;

    // Inteerface--> 接口OnDownSuccessListener
    public interface OnDownSuccessListener {
        void downSuccess(File attr);
    }

    // 对外方式setOnDownSuccessListener
    public void setOnDownSuccessListener(OnDownSuccessListener onDownSuccessListener) {
        this.onDownSuccessListener = onDownSuccessListener;
    }

    // 封装方法downSuccessNext
    private void downSuccessNext(File attr) {
        if (onDownSuccessListener != null) {
            onDownSuccessListener.downSuccess(attr);
        }
    }

    private OnDownCancelListener onDownCancelListener;

    // Inteerface--> 接口OnDownCancelListener
    public interface OnDownCancelListener {
        void downCancel();
    }

    // 对外方式setOnDownCancelListener
    public void setOnDownCancelListener(OnDownCancelListener onDownCancelListener) {
        this.onDownCancelListener = onDownCancelListener;
    }

    // 封装方法downCancelNext
    private void downCancelNext() {
        if (onDownCancelListener != null) {
            onDownCancelListener.downCancel();
        }
    }

    private OnSetDeviceBackupFailedListener onSetDeviceBackupFailedListener;

    // Inteerface--> 接口OnSetDeviceBackupFailedListener
    public interface OnSetDeviceBackupFailedListener {
        void SetDeviceBackupFailed();
    }

    // 对外方式setOnSetDeviceBackupFailedListener
    public void setOnSetDeviceBackupFailedListener(OnSetDeviceBackupFailedListener onSetDeviceBackupFailedListener) {
        this.onSetDeviceBackupFailedListener = onSetDeviceBackupFailedListener;
    }

    // 封装方法SetDeviceBackupFailedNext
    private void SetDeviceBackupFailedNext() {
        if (onSetDeviceBackupFailedListener != null) {
            onSetDeviceBackupFailedListener.SetDeviceBackupFailed();
        }
    }
}
