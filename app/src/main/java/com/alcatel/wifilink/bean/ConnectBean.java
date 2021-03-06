package com.alcatel.wifilink.bean;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetConnectDeviceListBean;

import java.io.Serializable;

/**
 * Created by qianli.ma on 2017/6/24.
 */

public class ConnectBean implements Serializable {
    
    public GetConnectDeviceListBean.ConnectedDeviceBean device;
    public boolean isEdit;// 是否可编辑

    public ConnectBean(GetConnectDeviceListBean.ConnectedDeviceBean device, boolean isEdit) {
        this.device = device;
        this.isEdit = isEdit;
    }

    public GetConnectDeviceListBean.ConnectedDeviceBean getDevice() {
        return device;
    }

    public void setDevice(GetConnectDeviceListBean.ConnectedDeviceBean device) {
        this.device = device;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
