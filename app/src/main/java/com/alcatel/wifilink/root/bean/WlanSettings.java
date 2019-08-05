package com.alcatel.wifilink.root.bean;

/**
 * Created by tao.j on 2017/6/15.
 */

public class WlanSettings implements Cloneable {
    private int WiFiOffTime;

    private AP AP2G;
    private AP AP5G;

    public int getWiFiOffTime() {
        return WiFiOffTime;
    }

    public void setWiFiOffTime(int wiFiOffTime) {
        WiFiOffTime = wiFiOffTime;
    }

    public AP getAP2G() {
        return AP2G;
    }

    public void setAP2G(AP AP2G) {
        this.AP2G = AP2G;
    }

    public AP getAP5G() {
        return AP5G;
    }

    public void setAP5G(AP AP5G) {
        this.AP5G = AP5G;
    }

    @Override
    public String toString() {
        return "WlanNewSettingResult{" +
                "WiFiOffTime=" + WiFiOffTime +
                ", AP2G=" + AP2G +
                ", AP5G=" + AP5G +
                '}';
    }

    @Override
    public WlanSettings clone() {
        WlanSettings wlanSettings = null;
        try {
            wlanSettings = (WlanSettings) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return wlanSettings;
    }
}
