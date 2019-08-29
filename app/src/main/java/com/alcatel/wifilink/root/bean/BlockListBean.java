package com.alcatel.wifilink.root.bean;

import java.util.List;


public class BlockListBean {

    public List<BlockDevice> BlockList;

    @Override
    public String toString() {
        return "BlockListBean{" + "BlockListBean=" + BlockList + '}';
    }

    public List<BlockDevice> getBlockList() {
        return BlockList;
    }

    public void setBlockList(List<BlockDevice> blockList) {
        BlockList = blockList;
    }

    public static class BlockDevice {
        public int id;
        public String DeviceName;
        public String MacAddress;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDeviceName() {
            return DeviceName;
        }

        public void setDeviceName(String deviceName) {
            DeviceName = deviceName;
        }

        public String getMacAddress() {
            return MacAddress;
        }

        public void setMacAddress(String macAddress) {
            MacAddress = macAddress;
        }

        @Override
        public String toString() {
            return "BlockDevice{" + "id=" + id + ", DeviceName='" + DeviceName + '\'' + ", MacAddress='" + MacAddress + '\'' + '}';
        }
    }

}
