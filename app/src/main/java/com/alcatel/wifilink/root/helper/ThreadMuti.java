package com.alcatel.wifilink.root.helper;

/**
 * Created by qianli.ma on 2018/3/12 0012.
 */

public class ThreadMuti {
    /**
     * 获取允许的最大线程数
     *
     * @return
     */
    public static int getMaxThreadNum() {
        return Runtime.getRuntime().availableProcessors();
    }
}
