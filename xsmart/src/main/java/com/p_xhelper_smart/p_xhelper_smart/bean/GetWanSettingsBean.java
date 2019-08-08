package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/7/31 0031.
 */
public class GetWanSettingsBean implements Serializable {
    
    /**
     * SubNetMask : 255.255.255.0
     * Gateway : 192.168.1.1
     * IpAddress : 192.168.1.120
     * Mtu : 1500
     * ConnectType : 0
     * PrimaryDNS : 202.201.23.01
     * SecondaryDNS : 202.201.23.01
     * Account : sz13234@163gd.com
     * Password : 123456
     * Status : 0
     * StaticIpAddress : 10.128.208.150
     * pppoeMtu : 1492
     * DurationTime : 100
     * WanType : eth0
     */

    public static final int CONS_PPPOE = 0;
    public static final int CONS_DHCP = 1;
    public static final int CONS_STATIC = 2;

    public static final int CONS_DISCONNECTED = 0;
    public static final int CONS_CONNECTING = 1;
    public static final int CONS_CONNECTED = 2;
    public static final int CONS_DISCONNECTING = 3;

    private String SubNetMask;
    private String Gateway;
    private String IpAddress;
    private int Mtu;
    private int ConnectType;
    private String PrimaryDNS;
    private String SecondaryDNS;
    private String Account;
    private String Password;
    private int Status;
    private String StaticIpAddress;
    private int pppoeMtu;
    private int DurationTime;
    private String WanType;

    public GetWanSettingsBean() {
    }

    public String getSubNetMask() {
        return SubNetMask;
    }

    public void setSubNetMask(String SubNetMask) {
        this.SubNetMask = SubNetMask;
    }

    public String getGateway() {
        return Gateway;
    }

    public void setGateway(String Gateway) {
        this.Gateway = Gateway;
    }

    public String getIpAddress() {
        return IpAddress;
    }

    public void setIpAddress(String IpAddress) {
        this.IpAddress = IpAddress;
    }

    public int getMtu() {
        return Mtu;
    }

    public void setMtu(int Mtu) {
        this.Mtu = Mtu;
    }

    public int getConnectType() {
        return ConnectType;
    }

    public void setConnectType(int ConnectType) {
        this.ConnectType = ConnectType;
    }

    public String getPrimaryDNS() {
        return PrimaryDNS;
    }

    public void setPrimaryDNS(String PrimaryDNS) {
        this.PrimaryDNS = PrimaryDNS;
    }

    public String getSecondaryDNS() {
        return SecondaryDNS;
    }

    public void setSecondaryDNS(String SecondaryDNS) {
        this.SecondaryDNS = SecondaryDNS;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String Account) {
        this.Account = Account;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getStaticIpAddress() {
        return StaticIpAddress;
    }

    public void setStaticIpAddress(String StaticIpAddress) {
        this.StaticIpAddress = StaticIpAddress;
    }

    public int getPppoeMtu() {
        return pppoeMtu;
    }

    public void setPppoeMtu(int pppoeMtu) {
        this.pppoeMtu = pppoeMtu;
    }

    public int getDurationTime() {
        return DurationTime;
    }

    public void setDurationTime(int DurationTime) {
        this.DurationTime = DurationTime;
    }

    public String getWanType() {
        return WanType;
    }

    public void setWanType(String WanType) {
        this.WanType = WanType;
    }
}
