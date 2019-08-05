package com.alcatel.wifilink.root.bean;

/**
 * Created by qianli.ma on 2018/5/31 0031.
 */

public class FeedbackLoginParam {
    /**
     * imei : 1234567890
     * mac_addr : 987654321
     * cu_ref : xxx
     * fw_version : 0.0.2
     */

    private String imei;
    private String mac_addr;
    private String cu_ref;
    private String fw_version;

    public FeedbackLoginParam() {
        
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMac_addr() {
        return mac_addr;
    }

    public void setMac_addr(String mac_addr) {
        this.mac_addr = mac_addr;
    }

    public String getCu_ref() {
        return cu_ref;
    }

    public void setCu_ref(String cu_ref) {
        this.cu_ref = cu_ref;
    }

    public String getFw_version() {
        return fw_version;
    }

    public void setFw_version(String fw_version) {
        this.fw_version = fw_version;
    }
}
