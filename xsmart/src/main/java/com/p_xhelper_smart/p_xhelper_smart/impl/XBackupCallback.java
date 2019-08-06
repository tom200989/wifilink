package com.p_xhelper_smart.p_xhelper_smart.impl;

import org.xutils.http.request.UriRequest;

/*
 * Created by qianli.ma on 2019/8/1 0001.
 */
public abstract class XBackupCallback implements XBackupListener {

    @Override
    public void getUriRequest(UriRequest uriRequest) {
        // 选择性实现
    }

    @Override
    public void fwError(FwError fwError) {
        // 选择性实现(下载一般用不上)
    }

    @Override
    public void wifiOff() {
        // 选择性实现
    }
}
