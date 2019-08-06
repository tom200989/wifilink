package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wzhiqiang on 2019/7/31
 */
public class GetHotSpotListBean implements Serializable {

    /**
     * Status : 0
     * HotspotList : [{"HotspotId":"1234567890123456","ConnectState":0,"SSID":"MW120-123","Signal":6,"SecurityMode":0,"IsSave":0},{"HotspotId":"1234567890123456","ConnectState":0,"SSID":"MW70-123","Signal":2,"SecurityMode":0,"IsSave":1}]
     */
    private int Status;//wifi搜索状态， 0: none  1: searching 2: completed
    private List<HotspotBean> HotspotList;

    public static final int CONS_WIFI_STATE_NONE = 0;
    public static final int CONS_WIFI_STATE_SEARCHING = 1;
    public static final int CONS_WIFI_STATE_COMPLETED = 2;

    public GetHotSpotListBean() {
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public List<HotspotBean> getHotspotList() {
        return HotspotList;
    }

    public void setHotspotList(List<HotspotBean> HotspotList) {
        this.HotspotList = HotspotList;
    }

    public static class HotspotBean implements Serializable {
        /**
         * HotspotId : 1234567890123456
         * ConnectState : 0
         * SSID : MW120-123
         * Signal : 6
         * SecurityMode : 0
         * IsSave : 0
         */

        private String HotspotId;
        private int ConnectState;//热点当前的连接状态，0: disconnected 1: connecting 2: connected
        private String SSID;//ssid的名字
        private int Signal;//信号强度，0-4
        private int SecurityMode;//加密类型：0:No Security  1:WEP  2:WPA  3: WPA2  4: WPA/WPA2
        private int IsSave;//之前是否保存过密码 0：没有 1：有

        public static final int CONS_SECURITY_NO_SEC = 0;
        public static final int CONS_SECURITY_WEP = 1;
        public static final int CONS_SECURITY_WPA = 2;
        public static final int CONS_SECURITY_WAP2 = 3;
        public static final int CONS_SECURITY_WAP_WAP2 = 4;

        public static final int CONS_CONNECT_STATE_DISCONNECTED = 0;
        public static final int CONS_CONNECT_STATE_CONNECTING = 1;
        public static final int CONS_CONNECT_STATE_CONNECTED = 2;

        public static final int CONS_PW_NOT_SAVE_BEFORE = 0;
        public static final int CONS_PW_SAVE_BEFORE = 1;

        public HotspotBean() {
        }

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
