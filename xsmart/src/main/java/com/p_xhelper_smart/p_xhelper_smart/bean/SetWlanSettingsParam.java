package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/7/31 0031.
 */
public class SetWlanSettingsParam implements Serializable {

    private int WiFiOffTime;
    private AP2GBean AP2G;
    private AP2GGuestBean AP2G_guest;
    private AP5GBean AP5G;
    private AP5GGuestBean AP5G_guest;

    public static int CONS_DISABLE_WIFI_AUTO_OFF = 0;
    public static int CONS_THE_TIME_FOR_SLEEP = 10;

    public static int CONS_APSTATUS_OFF = 0;
    public static int CONS_APSTATUS_ON = 1;
    public static int CONS_APSTATUS_WPS = 2;

    public static int CONS_WMODE_AUTO = 0;
    public static int CONS_WMODE_802_11B = 1;
    public static int CONS_WMODE_802_11B_G = 2;
    public static int CONS_WMODE_802_11B_G_N = 3;
    public static int CONS_WMODE_802_11A = 4;
    public static int CONS_WMODE_802_11A_N = 5;
    public static int CONS_WMODE_802_11A_C = 6;

    public static int CONS_SSID_HIDDEN_DISABLE = 0;
    public static int CONS_SSID_HIDDEN_ENABLE = 1;

    public static int CONS_CHANNEL_AUTO = 0;

    public static int CONS_SECURITY_MODE_DISABLE = 0;
    public static int CONS_SECURITY_MODE_WEP = 1;
    public static int CONS_SECURITY_MODE_WPA = 2;
    public static int CONS_SECURITY_MODE_WPA2 = 3;
    public static int CONS_SECURITY_MODE_WPA_WPA2 = 4;

    public static int CONS_WEP_TYPE_OPEN = 0;
    public static int CONS_WEP_TYPE_SHARE = 1;

    public static int CONS_TKIP = 0;
    public static int CONS_AES = 1;
    public static int CONS_AUTO = 2;

    public static int CONS_APISOLATION_DISABLE = 0;
    public static int CONS_APISOLATION_ENABLE = 1;

    public static int CONS_MHZ_20_40 = 0;
    public static int CONS_MHZ_20 = 1;
    public static int CONS_MHZ_40 = 2;
    public static int CONS_MHZ_80 = 3;
    public static int CONS_MHZ40_80 = 4;

    public SetWlanSettingsParam() {
    }

    public int getWiFiOffTime() {
        return WiFiOffTime;
    }

    public void setWiFiOffTime(int WiFiOffTime) {
        this.WiFiOffTime = WiFiOffTime;
    }

    public AP2GBean getAP2G() {
        return AP2G;
    }

    public void setAP2G(AP2GBean AP2G) {
        this.AP2G = AP2G;
    }

    public AP2GGuestBean getAP2G_guest() {
        return AP2G_guest;
    }

    public void setAP2G_guest(AP2GGuestBean AP2G_guest) {
        this.AP2G_guest = AP2G_guest;
    }

    public AP5GBean getAP5G() {
        return AP5G;
    }

    public void setAP5G(AP5GBean AP5G) {
        this.AP5G = AP5G;
    }

    public AP5GGuestBean getAP5G_guest() {
        return AP5G_guest;
    }

    public void setAP5G_guest(AP5GGuestBean AP5G_guest) {
        this.AP5G_guest = AP5G_guest;
    }

    public static class AP2GBean {
        /**
         * ApStatus : 1
         * WMode : 3
         * Ssid : WebPocket-BAB5
         * SsidHidden : 0
         * Channel : 0
         * SecurityMode : 3
         * WepType : 0
         * WepKey : 1234567890
         * WpaType : 1
         * WpaKey : GE747TNT
         * ApIsolation : 0
         * max_numsta : 15
         * curr_num : 0
         * CurChannel : 8
         * Bandwidth : 0
         * CountryCode : IT
         */

        private int ApStatus;
        private int WMode;
        private String Ssid;
        private int SsidHidden;
        private int Channel;
        private int SecurityMode;
        private int WepType;
        private String WepKey;
        private int WpaType;
        private String WpaKey;
        private int ApIsolation;
        private int max_numsta;
        private int curr_num;
        private int CurChannel;
        private int Bandwidth;
        private String CountryCode;

        public AP2GBean() {
        }

        public int getApStatus() {
            return ApStatus;
        }

        public void setApStatus(int ApStatus) {
            this.ApStatus = ApStatus;
        }

        public int getWMode() {
            return WMode;
        }

        public void setWMode(int WMode) {
            this.WMode = WMode;
        }

        public String getSsid() {
            return Ssid;
        }

        public void setSsid(String Ssid) {
            this.Ssid = Ssid;
        }

        public int getSsidHidden() {
            return SsidHidden;
        }

        public void setSsidHidden(int SsidHidden) {
            this.SsidHidden = SsidHidden;
        }

        public int getChannel() {
            return Channel;
        }

        public void setChannel(int Channel) {
            this.Channel = Channel;
        }

        public int getSecurityMode() {
            return SecurityMode;
        }

        public void setSecurityMode(int SecurityMode) {
            this.SecurityMode = SecurityMode;
        }

        public int getWepType() {
            return WepType;
        }

        public void setWepType(int WepType) {
            this.WepType = WepType;
        }

        public String getWepKey() {
            return WepKey;
        }

        public void setWepKey(String WepKey) {
            this.WepKey = WepKey;
        }

        public int getWpaType() {
            return WpaType;
        }

        public void setWpaType(int WpaType) {
            this.WpaType = WpaType;
        }

        public String getWpaKey() {
            return WpaKey;
        }

        public void setWpaKey(String WpaKey) {
            this.WpaKey = WpaKey;
        }

        public int getApIsolation() {
            return ApIsolation;
        }

        public void setApIsolation(int ApIsolation) {
            this.ApIsolation = ApIsolation;
        }

        public int getMax_numsta() {
            return max_numsta;
        }

        public void setMax_numsta(int max_numsta) {
            this.max_numsta = max_numsta;
        }

        public int getCurr_num() {
            return curr_num;
        }

        public void setCurr_num(int curr_num) {
            this.curr_num = curr_num;
        }

        public int getCurChannel() {
            return CurChannel;
        }

        public void setCurChannel(int CurChannel) {
            this.CurChannel = CurChannel;
        }

        public int getBandwidth() {
            return Bandwidth;
        }

        public void setBandwidth(int Bandwidth) {
            this.Bandwidth = Bandwidth;
        }

        public String getCountryCode() {
            return CountryCode;
        }

        public void setCountryCode(String CountryCode) {
            this.CountryCode = CountryCode;
        }
    }

    public static class AP2GGuestBean {
        /**
         * ApStatus : 1
         * WMode : 3
         * Ssid : WebPocket-BAB5
         * SsidHidden : 0
         * Channel : 0
         * SecurityMode : 3
         * WepType : 0
         * WepKey : 1234567890
         * WpaType : 1
         * WpaKey : GE747TNT
         * ApIsolation : 0
         * max_numsta : 15
         * curr_num : 0
         * CurChannel : 8
         * Bandwidth : 0
         * CountryCode : IT
         */

        private int ApStatus;
        private int WMode;
        private String Ssid;
        private int SsidHidden;
        private int Channel;
        private int SecurityMode;
        private int WepType;
        private String WepKey;
        private int WpaType;
        private String WpaKey;
        private int ApIsolation;
        private int max_numsta;
        private int curr_num;
        private int CurChannel;
        private int Bandwidth;
        private String CountryCode;

        public AP2GGuestBean() {
        }

        public int getApStatus() {
            return ApStatus;
        }

        public void setApStatus(int ApStatus) {
            this.ApStatus = ApStatus;
        }

        public int getWMode() {
            return WMode;
        }

        public void setWMode(int WMode) {
            this.WMode = WMode;
        }

        public String getSsid() {
            return Ssid;
        }

        public void setSsid(String Ssid) {
            this.Ssid = Ssid;
        }

        public int getSsidHidden() {
            return SsidHidden;
        }

        public void setSsidHidden(int SsidHidden) {
            this.SsidHidden = SsidHidden;
        }

        public int getChannel() {
            return Channel;
        }

        public void setChannel(int Channel) {
            this.Channel = Channel;
        }

        public int getSecurityMode() {
            return SecurityMode;
        }

        public void setSecurityMode(int SecurityMode) {
            this.SecurityMode = SecurityMode;
        }

        public int getWepType() {
            return WepType;
        }

        public void setWepType(int WepType) {
            this.WepType = WepType;
        }

        public String getWepKey() {
            return WepKey;
        }

        public void setWepKey(String WepKey) {
            this.WepKey = WepKey;
        }

        public int getWpaType() {
            return WpaType;
        }

        public void setWpaType(int WpaType) {
            this.WpaType = WpaType;
        }

        public String getWpaKey() {
            return WpaKey;
        }

        public void setWpaKey(String WpaKey) {
            this.WpaKey = WpaKey;
        }

        public int getApIsolation() {
            return ApIsolation;
        }

        public void setApIsolation(int ApIsolation) {
            this.ApIsolation = ApIsolation;
        }

        public int getMax_numsta() {
            return max_numsta;
        }

        public void setMax_numsta(int max_numsta) {
            this.max_numsta = max_numsta;
        }

        public int getCurr_num() {
            return curr_num;
        }

        public void setCurr_num(int curr_num) {
            this.curr_num = curr_num;
        }

        public int getCurChannel() {
            return CurChannel;
        }

        public void setCurChannel(int CurChannel) {
            this.CurChannel = CurChannel;
        }

        public int getBandwidth() {
            return Bandwidth;
        }

        public void setBandwidth(int Bandwidth) {
            this.Bandwidth = Bandwidth;
        }

        public String getCountryCode() {
            return CountryCode;
        }

        public void setCountryCode(String CountryCode) {
            this.CountryCode = CountryCode;
        }
    }

    public static class AP5GBean {
        /**
         * ApStatus : 1
         * WMode : 4
         * Ssid : WebPocket-BAB5-5G
         * SsidHidden : 0
         * Channel : 0
         * SecurityMode : 3
         * WepType : 0
         * WepKey : 1234567890
         * WpaType : 1
         * WpaKey : GE747TNT
         * ApIsolation : 0
         * max_numsta : 15
         * curr_num : 0
         * CurChannel : 8
         * Bandwidth : 0
         * CountryCode : IT
         */

        private int ApStatus;
        private int WMode;
        private String Ssid;
        private int SsidHidden;
        private int Channel;
        private int SecurityMode;
        private int WepType;
        private String WepKey;
        private int WpaType;
        private String WpaKey;
        private int ApIsolation;
        private int max_numsta;
        private int curr_num;
        private int CurChannel;
        private int Bandwidth;
        private String CountryCode;

        public AP5GBean() {
        }

        public int getApStatus() {
            return ApStatus;
        }

        public void setApStatus(int ApStatus) {
            this.ApStatus = ApStatus;
        }

        public int getWMode() {
            return WMode;
        }

        public void setWMode(int WMode) {
            this.WMode = WMode;
        }

        public String getSsid() {
            return Ssid;
        }

        public void setSsid(String Ssid) {
            this.Ssid = Ssid;
        }

        public int getSsidHidden() {
            return SsidHidden;
        }

        public void setSsidHidden(int SsidHidden) {
            this.SsidHidden = SsidHidden;
        }

        public int getChannel() {
            return Channel;
        }

        public void setChannel(int Channel) {
            this.Channel = Channel;
        }

        public int getSecurityMode() {
            return SecurityMode;
        }

        public void setSecurityMode(int SecurityMode) {
            this.SecurityMode = SecurityMode;
        }

        public int getWepType() {
            return WepType;
        }

        public void setWepType(int WepType) {
            this.WepType = WepType;
        }

        public String getWepKey() {
            return WepKey;
        }

        public void setWepKey(String WepKey) {
            this.WepKey = WepKey;
        }

        public int getWpaType() {
            return WpaType;
        }

        public void setWpaType(int WpaType) {
            this.WpaType = WpaType;
        }

        public String getWpaKey() {
            return WpaKey;
        }

        public void setWpaKey(String WpaKey) {
            this.WpaKey = WpaKey;
        }

        public int getApIsolation() {
            return ApIsolation;
        }

        public void setApIsolation(int ApIsolation) {
            this.ApIsolation = ApIsolation;
        }

        public int getMax_numsta() {
            return max_numsta;
        }

        public void setMax_numsta(int max_numsta) {
            this.max_numsta = max_numsta;
        }

        public int getCurr_num() {
            return curr_num;
        }

        public void setCurr_num(int curr_num) {
            this.curr_num = curr_num;
        }

        public int getCurChannel() {
            return CurChannel;
        }

        public void setCurChannel(int CurChannel) {
            this.CurChannel = CurChannel;
        }

        public int getBandwidth() {
            return Bandwidth;
        }

        public void setBandwidth(int Bandwidth) {
            this.Bandwidth = Bandwidth;
        }

        public String getCountryCode() {
            return CountryCode;
        }

        public void setCountryCode(String CountryCode) {
            this.CountryCode = CountryCode;
        }
    }

    public static class AP5GGuestBean {
        /**
         * ApStatus : 1
         * WMode : 4
         * Ssid : AP5G_guest
         * SsidHidden : 0
         * Channel : 0
         * SecurityMode : 3
         * WepType : 0
         * WepKey : 1234567890
         * WpaType : 1
         * WpaKey : GE747TNT
         * ApIsolation : 0
         * max_numsta : 15
         * curr_num : 0
         * CurChannel : 8
         * Bandwidth : 0
         * CountryCode : IT
         */

        private int ApStatus;
        private int WMode;
        private String Ssid;
        private int SsidHidden;
        private int Channel;
        private int SecurityMode;
        private int WepType;
        private String WepKey;
        private int WpaType;
        private String WpaKey;
        private int ApIsolation;
        private int max_numsta;
        private int curr_num;
        private int CurChannel;
        private int Bandwidth;
        private String CountryCode;

        public AP5GGuestBean() {
        }

        public int getApStatus() {
            return ApStatus;
        }

        public void setApStatus(int ApStatus) {
            this.ApStatus = ApStatus;
        }

        public int getWMode() {
            return WMode;
        }

        public void setWMode(int WMode) {
            this.WMode = WMode;
        }

        public String getSsid() {
            return Ssid;
        }

        public void setSsid(String Ssid) {
            this.Ssid = Ssid;
        }

        public int getSsidHidden() {
            return SsidHidden;
        }

        public void setSsidHidden(int SsidHidden) {
            this.SsidHidden = SsidHidden;
        }

        public int getChannel() {
            return Channel;
        }

        public void setChannel(int Channel) {
            this.Channel = Channel;
        }

        public int getSecurityMode() {
            return SecurityMode;
        }

        public void setSecurityMode(int SecurityMode) {
            this.SecurityMode = SecurityMode;
        }

        public int getWepType() {
            return WepType;
        }

        public void setWepType(int WepType) {
            this.WepType = WepType;
        }

        public String getWepKey() {
            return WepKey;
        }

        public void setWepKey(String WepKey) {
            this.WepKey = WepKey;
        }

        public int getWpaType() {
            return WpaType;
        }

        public void setWpaType(int WpaType) {
            this.WpaType = WpaType;
        }

        public String getWpaKey() {
            return WpaKey;
        }

        public void setWpaKey(String WpaKey) {
            this.WpaKey = WpaKey;
        }

        public int getApIsolation() {
            return ApIsolation;
        }

        public void setApIsolation(int ApIsolation) {
            this.ApIsolation = ApIsolation;
        }

        public int getMax_numsta() {
            return max_numsta;
        }

        public void setMax_numsta(int max_numsta) {
            this.max_numsta = max_numsta;
        }

        public int getCurr_num() {
            return curr_num;
        }

        public void setCurr_num(int curr_num) {
            this.curr_num = curr_num;
        }

        public int getCurChannel() {
            return CurChannel;
        }

        public void setCurChannel(int CurChannel) {
            this.CurChannel = CurChannel;
        }

        public int getBandwidth() {
            return Bandwidth;
        }

        public void setBandwidth(int Bandwidth) {
            this.Bandwidth = Bandwidth;
        }

        public String getCountryCode() {
            return CountryCode;
        }

        public void setCountryCode(String CountryCode) {
            this.CountryCode = CountryCode;
        }
    }
}
