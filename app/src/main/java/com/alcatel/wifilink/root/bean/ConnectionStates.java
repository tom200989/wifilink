package com.alcatel.wifilink.root.bean;

/**
 * Created by tao.j on 2017/6/16.
 */

public class ConnectionStates {
    public int ConnectionStatus;
    public int Conprofileerror;
    public String IPv4AdrressString;
    public String IPv6AdrressString;
    public int Speed_Dl;
    public int Speed_Ul;
    public int DlRate;
    public int UlRate;
    public int ConnectionTime;
    public long UlBytes;
    public long DlBytes;

    public int getConnectionStatus() {
        return ConnectionStatus;
    }

    public void setConnectionStatus(int connectionStatus) {
        ConnectionStatus = connectionStatus;
    }

    public int getConprofileerror() {
        return Conprofileerror;
    }

    public void setConprofileerror(int conprofileerror) {
        Conprofileerror = conprofileerror;
    }

    public String getIPv4AdrressString() {
        return IPv4AdrressString;
    }

    public void setIPv4AdrressString(String IPv4AdrressString) {
        this.IPv4AdrressString = IPv4AdrressString;
    }

    public String getIPv6AdrressString() {
        return IPv6AdrressString;
    }

    public void setIPv6AdrressString(String IPv6AdrressString) {
        this.IPv6AdrressString = IPv6AdrressString;
    }

    public int getSpeed_Dl() {
        return Speed_Dl;
    }

    public void setSpeed_Dl(int speed_Dl) {
        Speed_Dl = speed_Dl;
    }

    public int getSpeed_Ul() {
        return Speed_Ul;
    }

    public void setSpeed_Ul(int speed_Ul) {
        Speed_Ul = speed_Ul;
    }

    public int getDlRate() {
        return DlRate;
    }

    public void setDlRate(int dlRate) {
        DlRate = dlRate;
    }

    public int getUlRate() {
        return UlRate;
    }

    public void setUlRate(int ulRate) {
        UlRate = ulRate;
    }

    public int getConnectionTime() {
        return ConnectionTime;
    }

    public void setConnectionTime(int connectionTime) {
        ConnectionTime = connectionTime;
    }

    public long getUlBytes() {
        return UlBytes;
    }

    public void setUlBytes(long ulBytes) {
        UlBytes = ulBytes;
    }

    public long getDlBytes() {
        return DlBytes;
    }

    public void setDlBytes(long dlBytes) {
        DlBytes = dlBytes;
    }
}
