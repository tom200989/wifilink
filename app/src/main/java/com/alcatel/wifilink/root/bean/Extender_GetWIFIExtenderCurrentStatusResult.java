package com.alcatel.wifilink.root.bean;

/**
 * Created by qianli.ma on 2018/5/23 0023.
 */

public class Extender_GetWIFIExtenderCurrentStatusResult {
    /**
     * HotspotConnectStatus : 0
     * HotspotSSID : 
     * Signal : 1
     * IPV4Addr : 10.125.52.2
     * IPV6Addr : 
     */

    private int HotspotConnectStatus;
    private String HotspotSSID;
    private int Signal;
    private String IPV4Addr;
    private String IPV6Addr;

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
