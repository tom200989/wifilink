package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/7/31 0031.
 */
public class SetNetworkSettingsParam implements Serializable {

    /**
     * NetworkMode : 0
     * NetselectionMode : 0
     * NetworkBand : 0
     * DomesticRoam : 0
     * DomesticRoamGuard : 0
     */

    public static int CONS_AUTO_MODE = 0;
    public static int CONS_ONLY_2G = 1;
    public static int CONS_ONLY_3G = 2;
    public static int CONS_ONLY_LTE = 3;
    public static int CONS_GSM_LTE = 4;
    public static int CONS_UMTS_LTE = 5;
    public static int CONS_GSM_UMTS = 6;
    public static int CONS_CDMA_EVDO_FOR_Y856_SPRINT = 7;
    public static int CONS_LTE_CDMA_EVDO_FOR_Y856 = 8;
    public static int CONS_ONLY_EVDO = 9;
    public static int CONS_CDMA_EHRPD = 10;
    public static int CONS_CDMA_ONLY_1X_SPRINT = 11;

    public static int CONS_AUTO = 0;
    public static int CONS_MANUAL = 1;

    public static int CONS_ALL_BAND_1_6_10_GROUP = 0;
    public static int CONS_GSM_ALL_BAND_2_3_4_5_GROUP = 1;
    public static int CONS_GSM850 = 2;
    public static int CONS_GSM900 = 3;
    public static int CONS_GSM1800 = 4;
    public static int CONS_GSM1900 = 5;
    public static int CONS_WCDMA_ALL_BAND_7_8_9_GROUP = 6;
    public static int CONS_WCDMA_900 = 7;
    public static int CONS_WCDMA1800 = 8;
    public static int CONS_WCDMA2100 = 9;
    public static int CONS_LTE_ALL_BAND_11_12_13_14_GROUP = 10;
    public static int CONS_LTE800_LTE_BAND20 = 11;
    public static int CONS_LTE900_LTE_BAND8 = 12;
    public static int CONS_LTE1800_LTE_BAND3 = 13;
    public static int CONS_LTE2600_LTE_BAND7 = 14;
    public static int CONS_LTE850_LTE_BAND26 = 15;
    public static int CONS_LTE1900_LTE_BAND25 = 16;
    public static int CONS_LTE2600_LTE_BAND41 = 17;
    public static int CONS_CDMA_BC0 = 18;
    public static int CONS_CDMA_BC1 = 19;
    public static int CONS_CDMA_BC10 = 20;
    public static int CONS_LTE_BAND25_BAND26_BAND41 = 21;

    public static int CONS_DOMESTIC_ROAM_OFF = 0;
    public static int CONS_DOMESTIC_ROAM_ON = 1;

    public static int CONS_DOMESTIC_ROAMGUARD_OFF = 0;
    public static int CONS_DOMESTIC_ROAMGUARD_ON = 1;

    private int NetworkMode;
    private int NetselectionMode;
    private int NetworkBand;
    private int DomesticRoam;
    private int DomesticRoamGuard;

    public SetNetworkSettingsParam() {
    }

    public SetNetworkSettingsParam(int networkMode, int netselectionMode, int networkBand, int domesticRoam, int domesticRoamGuard) {
        NetworkMode = networkMode;
        NetselectionMode = netselectionMode;
        NetworkBand = networkBand;
        DomesticRoam = domesticRoam;
        DomesticRoamGuard = domesticRoamGuard;
    }

    public int getNetworkMode() {
        return NetworkMode;
    }

    public void setNetworkMode(int NetworkMode) {
        this.NetworkMode = NetworkMode;
    }

    public int getNetselectionMode() {
        return NetselectionMode;
    }

    public void setNetselectionMode(int NetselectionMode) {
        this.NetselectionMode = NetselectionMode;
    }

    public int getNetworkBand() {
        return NetworkBand;
    }

    public void setNetworkBand(int NetworkBand) {
        this.NetworkBand = NetworkBand;
    }

    public int getDomesticRoam() {
        return DomesticRoam;
    }

    public void setDomesticRoam(int DomesticRoam) {
        this.DomesticRoam = DomesticRoam;
    }

    public int getDomesticRoamGuard() {
        return DomesticRoamGuard;
    }

    public void setDomesticRoamGuard(int DomesticRoamGuard) {
        this.DomesticRoamGuard = DomesticRoamGuard;
    }
}
