package com.alcatel.wifilink.root.bean;

/**
 * Created by qianli.ma on 2018/5/23 0023.
 */

public class Extender_ConnectHotspotParam {
    /**
     * HotspotId : 1234567890123456
     * SSID : SSID Name
     * Key : 12345678
     * SecurityMode : 0
     * Hidden : 0
     */

    private String HotspotId;
    private String SSID;
    private String Key;
    private int SecurityMode;
    private int Hidden;

    public Extender_ConnectHotspotParam(String hotspotId, String SSID, String key, int SecurityMode, int Hidden) {
        this.HotspotId = hotspotId;
        this.SSID = SSID;
        this.Key = key;
        this.SecurityMode = SecurityMode;
        this.Hidden = Hidden;
    }

    public String getHotspotId() {
        return HotspotId;
    }

    public void setHotspotId(String hotspotId) {
        HotspotId = hotspotId;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public int getSecurityMode() {
        return SecurityMode;
    }

    public void setSecurityMode(int securityMode) {
        SecurityMode = securityMode;
    }

    public int getHidden() {
        return Hidden;
    }

    public void setHidden(int hidden) {
        Hidden = hidden;
    }
}
