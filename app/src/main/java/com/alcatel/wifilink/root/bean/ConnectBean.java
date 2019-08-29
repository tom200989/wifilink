package com.alcatel.wifilink.root.bean;

/**
 * Created by qianli.ma on 2017/6/24.
 */

public class ConnectBean {
    public ConnectedListBean.Device device;
    public boolean isEdit;

    public ConnectBean(ConnectedListBean.Device device, boolean isEdit) {
        this.device = device;
        this.isEdit = isEdit;
    }

    @Override
    public String toString() {
        return "ConnectBean{" + "device=" + device + ", isEdit=" + isEdit + '}';
    }

    public ConnectedListBean.Device getDevice() {
        return device;
    }

    public void setDevice(ConnectedListBean.Device device) {
        this.device = device;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
