package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
public class SetConnectionSettingsParam implements Serializable {

    /**
     * ConnectMode : 1
     * RoamingConnect : 0
     * PdpType : 0
     * ConnOffTime : 600
     */

    public static int CONS_MANUAL_CONNECT = 0;
    public static int CONS_AUTO_CONNECT = 1;

    public static int CONS_WHEN_ROAMING_CAN_NOT_CONNECT = 0;
    public static int CONS_WHEN_ROAMING_CAN_CONNECT = 1;

    public static int CONS_IPV4 = 0;
    public static int CONS_PPP = 1;
    public static int CONS_IPV6 = 2;
    public static int CONS_IPV4V6 = 3;

    private int ConnectMode;
    private int RoamingConnect;
    private int PdpType;
    private int ConnOffTime;

    public SetConnectionSettingsParam() {
    }

    public SetConnectionSettingsParam(int connectMode, int roamingConnect, int pdpType, int connOffTime) {
        ConnectMode = connectMode;
        RoamingConnect = roamingConnect;
        PdpType = pdpType;
        ConnOffTime = connOffTime;
    }

    public int getConnectMode() {
        return ConnectMode;
    }

    public void setConnectMode(int ConnectMode) {
        this.ConnectMode = ConnectMode;
    }

    public int getRoamingConnect() {
        return RoamingConnect;
    }

    public void setRoamingConnect(int RoamingConnect) {
        this.RoamingConnect = RoamingConnect;
    }

    public int getPdpType() {
        return PdpType;
    }

    public void setPdpType(int PdpType) {
        this.PdpType = PdpType;
    }

    public int getConnOffTime() {
        return ConnOffTime;
    }

    public void setConnOffTime(int ConnOffTime) {
        this.ConnOffTime = ConnOffTime;
    }
}
