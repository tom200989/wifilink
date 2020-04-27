package com.p_xhelper_smart.p_xhelper_smart.helper;

import android.content.Context;
import android.text.TextUtils;

import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XBackupCallback;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.SmartUtils;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

import org.xutils.common.Callback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by qianli.ma on 2019/8/1 0001.
 */
@SuppressWarnings("unchecked")
public class SetDeviceBackupHelper extends BaseHelper {


    private Context context;

    public SetDeviceBackupHelper(Context context) {
        this.context = context;
    }

    /**
     * 触发备份 -- 并下载
     */
    public void setDeviceBackup(String path) {
        prepareHelperNext();
        XSmart xSetDeviceBackup = new XSmart();
        xSetDeviceBackup.xMethod(XCons.METHOD_SET_DEVICE_BACKUP);
        xSetDeviceBackup.xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                reqBackup(path);
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
    private void reqBackup(String path) {
        // 路径中包含了［.］-- 不通过
        if (!checkPath(path)) {
            pathNotMatchRuleNext(path);
            return;
        }
        // 整理路径
        String savePath = getSavepath(handlePath(path));
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
    private String getSavepath(String path) {
        /* 默认路径: sdcard/smartlink/configure.bin */
        File sdDir = new File(SmartUtils.get_android_Q_SD_path(context));
        // 创建根目录
        if (!sdDir.exists() | !sdDir.isDirectory()) {
            sdDir.mkdirs();
        }
        // 创建1级目录
        String defaultPath = sdDir.getAbsolutePath() + XCons.PATH_SMARTLINK;
        File smartDir = new File(TextUtils.isEmpty(path) ? defaultPath : sdDir.getAbsolutePath() + path);
        if (!smartDir.exists() | !smartDir.isDirectory()) {
            smartDir.mkdirs();
        }
        // 创建文件
        File configFile = new File(smartDir.getAbsolutePath() + XCons.PATH_CONFIGURE_BIN);
        if (!configFile.exists() | configFile.isDirectory()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return configFile.getAbsolutePath();
    }


    /**
     * 处理路径
     *
     * @param path 原路径
     * @return T:符合规范 F:含有非法字符
     */
    private boolean checkPath(String path) {
        // 空字符
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        // 全部是空格
        if (!isNotAllEmpty(path)) {
            return false;
        }
        // 1.定义不能填写的字符
        List<String> Symbols = new ArrayList<>();
        Symbols.add("\\");
        Symbols.add(":");
        Symbols.add("*");
        Symbols.add("?");
        Symbols.add("\"");
        Symbols.add("<");
        Symbols.add(">");
        Symbols.add("|");
        Symbols.add("\\.");
        // 2.遍历 -- 路径中是否包含了非法字符
        for (String symbol : Symbols) {
            if (path.contains(symbol)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否全部非空格
     *
     * @param path 路径
     * @return T:存在非空格字符 F:全部都是空格或者空串
     */
    private boolean isNotAllEmpty(String path) {
        char[] chars = path.toCharArray();
        for (char aChar : chars) {
            String str = String.valueOf(aChar);
            if (!TextUtils.isEmpty(str) & !str.equals(" ")) {// 当前字符不为null, 且不等于空格或者空串
                return true;
            }
        }
        return false;
    }

    /**
     * 重新拼接 (防止出现: xxxx/////// 的情况)
     *
     * @param path 路径
     * @return 新路径
     */
    private String handlePath(String path) {
        String SPLIT = "/";
        String newPath = "";
        String[] chs = path.split(SPLIT);
        for (String ch : chs) {
            if (!TextUtils.isEmpty(ch)) {
                newPath = newPath + ch + SPLIT;
            }
        }
        return SPLIT + newPath.substring(0, newPath.length() - 1);
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

    private OnPathNotMatchRuleListener onPathNotMatchRuleListener;

    // Inteerface--> 接口OnPathNotMatchRuleListener
    public interface OnPathNotMatchRuleListener {
        void pathNotMatchRule(String path);
    }

    // 对外方式setOnPathNotMatchRuleListener
    public void setOnPathNotMatchRuleListener(OnPathNotMatchRuleListener onPathNotMatchRuleListener) {
        this.onPathNotMatchRuleListener = onPathNotMatchRuleListener;
    }

    // 封装方法pathNotMatchRuleNext
    private void pathNotMatchRuleNext(String path) {
        if (onPathNotMatchRuleListener != null) {
            onPathNotMatchRuleListener.pathNotMatchRule(path);
        }
    }
}
