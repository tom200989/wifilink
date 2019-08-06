package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/**
 * Created by wzhiqiang on 2019/8/1
 */
public class ConnectHotspotParam implements Serializable {

    /**
     * HotspotId : 1234567890123456
     * SSID : SSID Name
     * SecurityMode : 0
     * Key : 12345678
     * Hidden : 0
     */
    private String HotspotId;//该热点的唯一标示符
    private String SSID;//热点名
    private int SecurityMode;//加密类型 0:No Security   1:WEP   2:WPA   3: WPA2   4: WPA/WPA2
    private String Key;//热点密码
    private int Hidden;//该ssid是否是隐藏的，0：不隐藏  1：隐藏

    public static final int CONS_SECURITY_NO_SEC = 0;
    public static final int CONS_SECURITY_WEP = 1;
    public static final int CONS_SECURITY_WPA = 2;
    public static final int CONS_SECURITY_WAP2 = 3;
    public static final int CONS_SECURITY_WAP_WAP2 = 4;

    public static final int CONS_SSID_NOT_HIDDEN = 0;
    public static final int CONS_SSID_HIDDEN = 1;

    public ConnectHotspotParam() {
    }

    public String getHotspotId() {
        return HotspotId;
    }

    public void setHotspotId(String HotspotId) {
        this.HotspotId = HotspotId;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public int getSecurityMode() {
        return SecurityMode;
    }

    public void setSecurityMode(int SecurityMode) {
        this.SecurityMode = SecurityMode;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String Key) {
        this.Key = Key;
    }

    public int getHidden() {
        return Hidden;
    }

    public void setHidden(int Hidden) {
        this.Hidden = Hidden;
    }
}
