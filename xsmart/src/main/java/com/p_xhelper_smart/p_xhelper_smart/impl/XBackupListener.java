package com.p_xhelper_smart.p_xhelper_smart.impl;

import java.io.File;

/*
 * Created by qianli.ma on 2019/8/1 0001.
 */
public interface XBackupListener extends XBaseListener {

    /**
     * 传输等待中
     */
    void waiting();

    /**
     * 传输开始
     */
    void start();

    /**
     * 传输进度
     *
     * @param total         总大小
     * @param current       当前进度
     * @param isDownloading 是否正在传输
     */
    void loading(long total, long current, boolean isDownloading);

    /**
     * 传输成功(回调FILE, 一般用于下载成功时的回调)
     *
     * @param file 成功后服务器返回File
     */
    void success(File file);
}
