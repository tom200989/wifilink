package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/7/31 0031.
 */
public class GetSystemStatusBean implements Serializable {

    /**
     * NetworkType : 8
     * NetworkName : UNICOM
     * Roaming : 1
     * Domestic_Roaming : 1
     * SignalStrength : 3
     * chg_state : 1
     * bat_cap : 100
     * bat_level : 4
     * ConnectionStatus : 2
     * Conprofileerror : 0
     * SmsState : 2
     * WlanState : 1
     * curr_num : 0
     * curr_num_2g : 0
     * curr_num_5g : 0
     * TotalConnNum : 1
     * UsbStatus : 0
     * UsbName : disk1
     * Ssid_2g : HH70-459D_2.4GHz
     * Ssid_5g : HH70-459D_5GHz
     * WlanState_2g : 1
     * WlanState_5g : 1
     * DeviceUptime : 100
     */

    public static int CONS_NO_SERVER = 0;
    public static int CONS_GPRS = 1;
    public static int CONS_EDGE = 2;
    public static int CONS_HSPA = 3;
    public static int CONS_HSUPA = 4;
    public static int CONS_UMTS = 5;
    public static int CONS_HSPA_PLUS = 6;
    public static int CONS_DC_HSPA_PLUS = 7;
    public static int CONS_LTE = 8;
    public static int CONS_LTE_PLUS = 9;
    public static int CONS_WCDMA = 10;
    public static int CONS_CDMA = 11;
    public static int CONS_GSM = 12;
    
    public static int CONS_ROAMING = 0;
    public static int CONS_NO = 1;
    
    public static int CONS_NATIONAL_ROAMING = 0;
    public static int CONS_INTERNATIONAL_ROAMING = 1;
    
    public static int CONS_NO_SERVICE = -1;
    public static int CONS_LEVEL_0 = 0;
    public static int CONS_LEVEL_1 = 1;
    public static int CONS_LEVEL_2 = 2;
    public static int CONS_LEVEL_3 = 3;
    public static int CONS_LEVEL_4 = 4;
    
    public static int CONS_DISCONNECTED = 0;
    public static int CONS_CONNECTING = 1;
    public static int CONS_CONNECTED = 2;
    public static int CONS_DISCONNECTING = 3;
    
    public static int CONS_NO_ERROR = 0;
    public static int CONS_APN_ERROR = 1;
    public static int CONS_PDP_ERROR = 2;
    
    public static int CONS_NO_SMS = 0;
    public static int CONS_SMS_FULL = 1;
    public static int CONS_NORAL = 2;
    public static int CONS_NEW_SMS = 3;
    
    public static int CONS_WLANSTATE_OFF = 0;
    public static int CONS_WLANSTATE_ON = 1;
    public static int CONS_WLANSTATE_WPS = 2;
    
    public static int CONS_WLANSTATE_2G_OFF = 0;
    public static int CONS_WLANSTATE_2G_ON = 1;
    public static int CONS_WLANSTATE_2G_WPS = 2;
    
    public static int CONS_WLANSTATE_5G_OFF = 0;
    public static int CONS_WLANSTATE_5G_ON = 1;
    public static int CONS_WLANSTATE_5G_WPS = 2;
    
    public static int CONS_NOT_INSERT = 0;
    public static int CONS_USB_STORAGE = 1;
    public static int CONS_USB_PRINT = 2;

    private int NetworkType;
    private String NetworkName;
    private int Roaming;
    private int Domestic_Roaming;
    private int SignalStrength;
    private int chg_state;
    private int bat_cap;
    private int bat_level;
    private int ConnectionStatus;
    private int Conprofileerror;
    private int SmsState;
    private int WlanState;
    private int curr_num;
    private int curr_num_2g;
    private int curr_num_5g;
    private int TotalConnNum;
    private int UsbStatus;
    private String UsbName;
    private String Ssid_2g;
    private String Ssid_5g;
    private int WlanState_2g;
    private int WlanState_5g;
    private int DeviceUptime;

    public GetSystemStatusBean() {
    }

    public int getNetworkType() {
        return NetworkType;
    }

    public void setNetworkType(int NetworkType) {
        this.NetworkType = NetworkType;
    }

    public String getNetworkName() {
        return NetworkName;
    }

    public void setNetworkName(String NetworkName) {
        this.NetworkName = NetworkName;
    }

    public int getRoaming() {
        return Roaming;
    }

    public void setRoaming(int Roaming) {
        this.Roaming = Roaming;
    }

    public int getDomestic_Roaming() {
        return Domestic_Roaming;
    }

    public void setDomestic_Roaming(int Domestic_Roaming) {
        this.Domestic_Roaming = Domestic_Roaming;
    }

    public int getSignalStrength() {
        return SignalStrength;
    }

    public void setSignalStrength(int SignalStrength) {
        this.SignalStrength = SignalStrength;
    }

    public int getChg_state() {
        return chg_state;
    }

    public void setChg_state(int chg_state) {
        this.chg_state = chg_state;
    }

    public int getBat_cap() {
        return bat_cap;
    }

    public void setBat_cap(int bat_cap) {
        this.bat_cap = bat_cap;
    }

    public int getBat_level() {
        return bat_level;
    }

    public void setBat_level(int bat_level) {
        this.bat_level = bat_level;
    }

    public int getConnectionStatus() {
        return ConnectionStatus;
    }

    public void setConnectionStatus(int ConnectionStatus) {
        this.ConnectionStatus = ConnectionStatus;
    }

    public int getConprofileerror() {
        return Conprofileerror;
    }

    public void setConprofileerror(int Conprofileerror) {
        this.Conprofileerror = Conprofileerror;
    }

    public int getSmsState() {
        return SmsState;
    }

    public void setSmsState(int SmsState) {
        this.SmsState = SmsState;
    }

    public int getWlanState() {
        return WlanState;
    }

    public void setWlanState(int WlanState) {
        this.WlanState = WlanState;
    }

    public int getCurr_num() {
        return curr_num;
    }

    public void setCurr_num(int curr_num) {
        this.curr_num = curr_num;
    }

    public int getCurr_num_2g() {
        return curr_num_2g;
    }

    public void setCurr_num_2g(int curr_num_2g) {
        this.curr_num_2g = curr_num_2g;
    }

    public int getCurr_num_5g() {
        return curr_num_5g;
    }

    public void setCurr_num_5g(int curr_num_5g) {
        this.curr_num_5g = curr_num_5g;
    }

    public int getTotalConnNum() {
        return TotalConnNum;
    }

    public void setTotalConnNum(int TotalConnNum) {
        this.TotalConnNum = TotalConnNum;
    }

    public int getUsbStatus() {
        return UsbStatus;
    }

    public void setUsbStatus(int UsbStatus) {
        this.UsbStatus = UsbStatus;
    }

    public String getUsbName() {
        return UsbName;
    }

    public void setUsbName(String UsbName) {
        this.UsbName = UsbName;
    }

    public String getSsid_2g() {
        return Ssid_2g;
    }

    public void setSsid_2g(String Ssid_2g) {
        this.Ssid_2g = Ssid_2g;
    }

    public String getSsid_5g() {
        return Ssid_5g;
    }

    public void setSsid_5g(String Ssid_5g) {
        this.Ssid_5g = Ssid_5g;
    }

    public int getWlanState_2g() {
        return WlanState_2g;
    }

    public void setWlanState_2g(int WlanState_2g) {
        this.WlanState_2g = WlanState_2g;
    }

    public int getWlanState_5g() {
        return WlanState_5g;
    }

    public void setWlanState_5g(int WlanState_5g) {
        this.WlanState_5g = WlanState_5g;
    }

    public int getDeviceUptime() {
        return DeviceUptime;
    }

    public void setDeviceUptime(int DeviceUptime) {
        this.DeviceUptime = DeviceUptime;
    }
}
