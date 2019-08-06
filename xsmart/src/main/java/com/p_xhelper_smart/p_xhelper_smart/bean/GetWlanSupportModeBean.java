package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/7/31 0031.
 */
public class GetWlanSupportModeBean implements Serializable {
    
    /**
     * WlanAPMode : 0
     */

    private int WlanAPMode;

    public static int CONS_WLAN_2_4G = 0;
    public static int CONS_WLAN_5G = 1;
    public static int CONS_WLAN_2_4G_2_4G = 2;
    public static int CONS_WLAN_5G_5G = 3;

    public GetWlanSupportModeBean() {
    }

    public int getWlanAPMode() {
        return WlanAPMode;
    }

    public void setWlanAPMode(int WlanAPMode) {
        this.WlanAPMode = WlanAPMode;
    }
}
