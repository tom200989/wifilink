package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
public class GetConnectionStateBean implements Serializable {
    /**
     * ConnectionStatus : 0
     * Conprofileerror : 0
     * IPv4Adrress : 0.0.0.0
     * IPv6Adrress : 0::0
     * Speed_Dl : 0
     * Speed_Ul : 0
     * DlRate : 0
     * UlRate : 0
     * ConnectionTime : 0
     * UlBytes : 0
     * DlBytes : 0
     */

    public static int CONS_DISCONNECTED = 0;
    public static int CONS_CONNECTING = 1;
    public static int CONS_CONNECTED = 2;
    public static int CONS_DISCONNECTING = 3;
    
    public static int CONS_NO_ERROR = 0;
    public static int CONS_APN_ERROR = 1;
    public static int CONS_PDP_ERROR = 2;

    private int ConnectionStatus;
    private int Conprofileerror;
    private String IPv4Adrress;
    private String IPv6Adrress;
    private int Speed_Dl;
    private int Speed_Ul;
    private int DlRate;
    private int UlRate;
    private int ConnectionTime;
    private int UlBytes;
    private int DlBytes;

    public GetConnectionStateBean() {
    }

    public int getConnectionStatus() {
        return ConnectionStatus;
    }

    public void setConnectionStatus(int ConnectionStatus) {
        this.ConnectionStatus = ConnectionStatus;
    }

    public int getConprofileerror() {
        return Conprofileerror;
    }

    public void setConprofileerror(int Conprofileerror) {
        this.Conprofileerror = Conprofileerror;
    }

    public String getIPv4Adrress() {
        return IPv4Adrress;
    }

    public void setIPv4Adrress(String IPv4Adrress) {
        this.IPv4Adrress = IPv4Adrress;
    }

    public String getIPv6Adrress() {
        return IPv6Adrress;
    }

    public void setIPv6Adrress(String IPv6Adrress) {
        this.IPv6Adrress = IPv6Adrress;
    }

    public int getSpeed_Dl() {
        return Speed_Dl;
    }

    public void setSpeed_Dl(int Speed_Dl) {
        this.Speed_Dl = Speed_Dl;
    }

    public int getSpeed_Ul() {
        return Speed_Ul;
    }

    public void setSpeed_Ul(int Speed_Ul) {
        this.Speed_Ul = Speed_Ul;
    }

    public int getDlRate() {
        return DlRate;
    }

    public void setDlRate(int DlRate) {
        this.DlRate = DlRate;
    }

    public int getUlRate() {
        return UlRate;
    }

    public void setUlRate(int UlRate) {
        this.UlRate = UlRate;
    }

    public int getConnectionTime() {
        return ConnectionTime;
    }

    public void setConnectionTime(int ConnectionTime) {
        this.ConnectionTime = ConnectionTime;
    }

    public int getUlBytes() {
        return UlBytes;
    }

    public void setUlBytes(int UlBytes) {
        this.UlBytes = UlBytes;
    }

    public int getDlBytes() {
        return DlBytes;
    }

    public void setDlBytes(int DlBytes) {
        this.DlBytes = DlBytes;
    }
}
