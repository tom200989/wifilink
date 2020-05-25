package com.alcatel.wifilink.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qianli.ma on 2018/5/23 0023.
 */

public class Extender_GetHotspotListBean implements Serializable {
    /**
     * Status : 0
     * HotspotList : [{"HotspotId":"1234567890123456","ConnectState":0,"SSID":"MW120-123","Signal":6,"SecurityMode":0,"IsSave":0},{"HotspotId":"1234567890123456","ConnectState":0,"SSID":"MW70-123","Signal":2,"SecurityMode":0,"IsSave":1}]
     */

    private int Status;
    /**
     * HotspotId : 1234567890123456
     * ConnectState : 0
     * SSID : MW120-123
     * Signal : 6
     * SecurityMode : 0
     * IsSave : 0
     */

    private List<HotspotListBean> HotspotList;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public List<HotspotListBean> getHotspotList() {
        return HotspotList;
    }

    public void setHotspotList(List<HotspotListBean> HotspotList) {
        this.HotspotList = HotspotList;
    }

    public static class HotspotListBean implements Serializable {
        private String HotspotId;
        private int ConnectState;
        private String SSID;
        private int Signal;
        private int SecurityMode;
        private int IsSave;

        public String getHotspotId() {
            return HotspotId;
        }

        public void setHotspotId(String HotspotId) {
            this.HotspotId = HotspotId;
        }

        public int getConnectState() {
            return ConnectState;
        }

        public void setConnectState(int ConnectState) {
            this.ConnectState = ConnectState;
        }

        public String getSSID() {
            return SSID;
        }

        public void setSSID(String SSID) {
            this.SSID = SSID;
        }

        public int getSignal() {
            return Signal;
        }

        public void setSignal(int Signal) {
            this.Signal = Signal;
        }

        public int getSecurityMode() {
            return SecurityMode;
        }

        public void setSecurityMode(int SecurityMode) {
            this.SecurityMode = SecurityMode;
        }

        public int getIsSave() {
            return IsSave;
        }

        public void setIsSave(int IsSave) {
            this.IsSave = IsSave;
        }
    }
}
