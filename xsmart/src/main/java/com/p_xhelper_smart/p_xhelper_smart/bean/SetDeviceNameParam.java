package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/**
 * Created by wzhiqiang on 2019/8/1
 */
public class SetDeviceNameParam implements Serializable {


    /**
     * DeviceName : huide-iPhone2
     * MacAddress : a0:99:9b:70:0b:c4
     * DeviceType : 1
     * ConnectMode : 0
     */
    private String DeviceName;//设备名
    private String MacAddress;//设备mac地址
    private int DeviceType;//0: use the web UI and login device;  1: just connected to device but not login.
    private int ConnectMode;//0: USB connect 1: WIFI connect

    public static final int CONS_CONNECT_MODE_USB = 0;
    public static final int CONS_CONNECT_MODE_WIFI = 1;

    public static final int CONS_DEVICE_TYPE_LOGIN = 0;
    public static final int CONS_DEVICE_TYPE_NOT_LOGIN = 1;

    public SetDeviceNameParam() {
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String DeviceName) {
        this.DeviceName = DeviceName;
    }

    public String getMacAddress() {
        return MacAddress;
    }

    public void setMacAddress(String MacAddress) {
        this.MacAddress = MacAddress;
    }

    public int getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(int DeviceType) {
        this.DeviceType = DeviceType;
    }

    public int getConnectMode() {
        return ConnectMode;
    }

    public void setConnectMode(int ConnectMode) {
        this.ConnectMode = ConnectMode;
    }
}
