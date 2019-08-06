package com.p_xhelper_smart.p_xhelper_smart.impl;

import org.xutils.common.Callback;
import org.xutils.http.request.UriRequest;

/*
 * Created by qianli.ma on 2019/7/26 0026.
 */
public interface XBaseListener {

    /**
     * 获取响应请求时的其他实体内容(如头部信息, 请求体, 响应体等)
     *
     * @param uriRequest 其他实体内容
     */
    void getUriRequest(UriRequest uriRequest);

    /**
     * 客户端错误
     */
    void appError(Throwable ex);

    /**
     * 服务器错误
     */
    void fwError(FwError fwError);

    /**
     * 请求取消
     */
    void cancel(Callback.CancelledException cex);

    /**
     * 请求完成(无论成功与否)
     */
    void finish();

    /**
     * WIFI无效或断连
     */
    void wifiOff();
}
