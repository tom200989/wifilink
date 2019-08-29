package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.XRestoreCallback;

import org.xutils.common.Callback;

import java.io.File;

/*
 * Created by qianli.ma on 2019/8/12 0012.
 */
@SuppressWarnings("unchecked")
public class SetDeviceRestoreHelper extends BaseHelper {

    /**
     * 恢复
     */
    public void setDeviceRestore() {
        prepareHelperNext();
        XSmart xSetDeviceRestore = new XSmart();
        xSetDeviceRestore.xRestore(new XRestoreCallback() {
            @Override
            public void noRestoreFile() {
                noRestoreFileNext();
            }

            @Override
            public void waiting() {
                waittingNext();
            }

            @Override
            public void start() {
                startNext();
            }

            @Override
            public void loading(long total, long current, boolean isUploading) {
                UploadingNext(total, current, isUploading);
            }

            @Override
            public void success(File file) {
                restoreSuccessNext(file);
            }

            @Override
            public void appError(Throwable ex) {
                restoreFailedNext();
            }

            @Override
            public void cancel(Callback.CancelledException cex) {
                restoreFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnRestoreSuccessListener onRestoreSuccessListener;

    // Inteerface--> 接口OnRestoreSuccessListener
    public interface OnRestoreSuccessListener {
        void restoreSuccess(File file);
    }

    // 对外方式setOnRestoreSuccessListener
    public void setOnRestoreSuccessListener(OnRestoreSuccessListener onRestoreSuccessListener) {
        this.onRestoreSuccessListener = onRestoreSuccessListener;
    }

    // 封装方法restoreSuccessNext
    private void restoreSuccessNext(File file) {
        if (onRestoreSuccessListener != null) {
            onRestoreSuccessListener.restoreSuccess(file);
        }
    }

    private OnRestoreFailedListener onRestoreFailedListener;

    // Inteerface--> 接口OnRestoreFailedListener
    public interface OnRestoreFailedListener {
        void restoreFailed();
    }

    // 对外方式setOnRestoreFailedListener
    public void setOnRestoreFailedListener(OnRestoreFailedListener onRestoreFailedListener) {
        this.onRestoreFailedListener = onRestoreFailedListener;
    }

    // 封装方法restoreFailedNext
    private void restoreFailedNext() {
        if (onRestoreFailedListener != null) {
            onRestoreFailedListener.restoreFailed();
        }
    }

    private OnNoRestoreFileListener onNoRestoreFileListener;

    // Inteerface--> 接口OnNoRestoreFileListener
    public interface OnNoRestoreFileListener {
        void noRestoreFile();
    }

    // 对外方式setOnNoRestoreFileListener
    public void setOnNoRestoreFileListener(OnNoRestoreFileListener onNoRestoreFileListener) {
        this.onNoRestoreFileListener = onNoRestoreFileListener;
    }

    // 封装方法noRestoreFileNext
    private void noRestoreFileNext() {
        if (onNoRestoreFileListener != null) {
            onNoRestoreFileListener.noRestoreFile();
        }
    }

    private OnWaittingListener onWaittingListener;

    // Inteerface--> 接口OnWaittingListener
    public interface OnWaittingListener {
        void waitting();
    }

    // 对外方式setOnWaittingListener
    public void setOnWaittingListener(OnWaittingListener onWaittingListener) {
        this.onWaittingListener = onWaittingListener;
    }

    // 封装方法waittingNext
    private void waittingNext() {
        if (onWaittingListener != null) {
            onWaittingListener.waitting();
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

    private OnUploadingListener onUploadingListener;

    // Inteerface--> 接口OnUploadingListener
    public interface OnUploadingListener {
        void Uploading(long total, long current, boolean isUploading);
    }

    // 对外方式setOnUploadingListener
    public void setOnUploadingListener(OnUploadingListener onUploadingListener) {
        this.onUploadingListener = onUploadingListener;
    }

    // 封装方法UploadingNext
    private void UploadingNext(long total, long current, boolean isUploading) {
        if (onUploadingListener != null) {
            onUploadingListener.Uploading(total, current, isUploading);
        }
    }

}
