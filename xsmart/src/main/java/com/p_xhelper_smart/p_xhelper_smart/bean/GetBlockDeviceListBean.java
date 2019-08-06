package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class GetBlockDeviceListBean implements Serializable {


    private List<BlockDeviceBean> BlockList;//黑名单设备列表

    public GetBlockDeviceListBean() {
    }

    public List<BlockDeviceBean> getBlockList() {
        return BlockList;
    }

    public void setBlockList(List<BlockDeviceBean> BlockList) {
        this.BlockList = BlockList;
    }

    public static class BlockDeviceBean implements Serializable {
        /**
         * id : 1
         * DeviceName : USBdevice
         * MacAddress : 3a:11:14:47:f6:a3
         * IPAddress : 192.168.1.117
         * AssociationTime : 0
         * DeviceType : 1
         * ConnectMode : 0
         */

        private int id;
        private String DeviceName;//设备名
        private String MacAddress;//设备mac地址
        private String IPAddress;//设备ip地址
        private int AssociationTime;//设备连接时间
        private int DeviceType;//0: use the web UI and login device;  1: just connected to device but not login.
        private int ConnectMode;//连接模式，0: USB connect 1: WIFI connect

        public static final int CONS_CONNECT_MODE_USB = 0;
        public static final int CONS_CONNECT_MODE_WIFI = 1;

        public static final int CONS_DEVICE_TYPE_LOGIN = 0;
        public static final int CONS_DEVICE_TYPE_NOT_LOGIN = 1;

        public BlockDeviceBean() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getIPAddress() {
            return IPAddress;
        }

        public void setIPAddress(String IPAddress) {
            this.IPAddress = IPAddress;
        }

        public int getAssociationTime() {
            return AssociationTime;
        }

        public void setAssociationTime(int AssociationTime) {
            this.AssociationTime = AssociationTime;
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
}
