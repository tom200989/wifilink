package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/**
 * Created by wzhiqiang on 2019/7/31
 */
public class GetWIFIExtenderCurrentStatusBean implements Serializable {

    /**
     * HotspotConnectStatus : 0
     * HotspotSSID :
     * Signal : 1
     * IPV4Addr : 10.125.52.2
     * IPV6Addr :
     */

    private int HotspotConnectStatus;//0: disconnected  1: connecting  2: connected
    private String HotspotSSID;//连接的热点的ssid
    private int Signal;//连接的热点的信号（0-4）
    private String IPV4Addr;
    private String IPV6Addr;

    public static final int CONS_CONNECT_STATUS_DISCONNECTED = 0;
    public static final int CONS_CONNECT_STATUS_CONNECTING = 1;
    public static final int CONS_CONNECT_STATUS_CONNECTED = 2;

    public GetWIFIExtenderCurrentStatusBean() {
    }

    public int getHotspotConnectStatus() {
        return HotspotConnectStatus;
    }

    public void setHotspotConnectStatus(int HotspotConnectStatus) {
        this.HotspotConnectStatus = HotspotConnectStatus;
    }

    public String getHotspotSSID() {
        return HotspotSSID;
    }

    public void setHotspotSSID(String HotspotSSID) {
        this.HotspotSSID = HotspotSSID;
    }

    public int getSignal() {
        return Signal;
    }

    public void setSignal(int Signal) {
        this.Signal = Signal;
    }

    public String getIPV4Addr() {
        return IPV4Addr;
    }

    public void setIPV4Addr(String IPV4Addr) {
        this.IPV4Addr = IPV4Addr;
    }

    public String getIPV6Addr() {
        return IPV6Addr;
    }

    public void setIPV6Addr(String IPV6Addr) {
        this.IPV6Addr = IPV6Addr;
    }
}
