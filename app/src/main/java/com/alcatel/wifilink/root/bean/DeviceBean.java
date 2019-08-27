package com.alcatel.wifilink.root.bean;

/**
 * Created by qianli.ma on 2018/5/21 0021.
 */

public class DeviceBean {
    
    private boolean isPhone;// 手机或电脑
    private String deviceName;// 设备名
    private String deviceIP;// 设备IP
    private String deviceMac;// 设备MAC
    private boolean isHost;// 是否为管理者

    public DeviceBean() {
    }

    public boolean isPhone() {
        return isPhone;
    }

    public void setPhone(boolean phone) {
        isPhone = phone;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceIP() {
        return deviceIP;
    }

    public void setDeviceIP(String deviceIP) {
        this.deviceIP = deviceIP;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }
}
