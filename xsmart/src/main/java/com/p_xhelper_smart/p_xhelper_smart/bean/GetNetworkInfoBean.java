package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/7/31 0031.
 */
public class GetNetworkInfoBean implements Serializable {

    /**
     * PLMN : 00000
     * NetworkType : 0
     * NetworkName : N/A
     * SpnName : N/A
     * LAC : 0
     * CellId : 0
     * RncId : reserved
     * Roaming : 1
     * Domestic_Roaming : 1
     * SignalStrength : 0
     * mcc : 0
     * mnc : 0
     * SINR : FF
     * RSRP : -1
     * RSSI : -48
     * eNBID : 0
     * CGI : 000
     * CenterFreq : 0.000000
     * TxPWR : 0
     * LTE_state : 0
     * PLMN_name : N/A
     * Band : 44
     * DL_channel : reserved
     * UL_channel : reserved
     * RSRQ : 2
     * EcIo : 0
     * RSCP : -1
     */

    private String PLMN;
    private int NetworkType;
    private String NetworkName;
    private String SpnName;
    private String LAC;
    private String CellId;
    private String RncId;
    private int Roaming;
    private int Domestic_Roaming;
    private int SignalStrength;
    private String mcc;
    private String mnc;
    private String SINR;
    private String RSRP;
    private String RSSI;
    private String eNBID;
    private String CGI;
    private String CenterFreq;
    private String TxPWR;
    private int LTE_state;
    private String PLMN_name;
    private int Band;
    private String DL_channel;
    private String UL_channel;
    private String RSRQ;
    private int EcIo;
    private int RSCP;

    public static int CONS_NO_SERVER = 0;
    public static int CONS_GPRS = 1;
    public static int CONS_EDGE = 2;
    public static int CONS_HSPA = 3;
    public static int CONS_HSUPA = 4;
    public static int CONS_UMTS = 5;
    public static int CONS_HSPA_PLUS = 6;
    public static int CONS_DC_HSPA_PLUS = 7;
    public static int CONS_LTE = 8;
    public static int CONS_LTE_PLUS = 9;
    public static int CONS_CDMA = 10;
    public static int CONS_GSM = 11;
    public static int CONS_EVDO = 12;
    public static int CONS_LTE_FDD = 13;
    public static int CONS_LTE_TDD = 14;
    public static int CONS_CDMA_EHRPD = 15;
    
    public static int CONS_ROAMING = 0;
    public static int CONS_NO_ROAMING = 1;
    
    public static int CONS_NO_SERVICE = -1;
    public static int CONS_LEVEL_0 = 0;
    public static int CONS_LEVEL_1 = 1;
    public static int CONS_LEVEL_2 = 2;
    public static int CONS_LEVEL_3 = 3;
    public static int CONS_LEVEL_4 = 4;

    public GetNetworkInfoBean() {
    }

    public String getPLMN() {
        return PLMN;
    }

    public void setPLMN(String PLMN) {
        this.PLMN = PLMN;
    }

    public int getNetworkType() {
        return NetworkType;
    }

    public void setNetworkType(int NetworkType) {
        this.NetworkType = NetworkType;
    }

    public String getNetworkName() {
        return NetworkName;
    }

    public void setNetworkName(String NetworkName) {
        this.NetworkName = NetworkName;
    }

    public String getSpnName() {
        return SpnName;
    }

    public void setSpnName(String SpnName) {
        this.SpnName = SpnName;
    }

    public String getLAC() {
        return LAC;
    }

    public void setLAC(String LAC) {
        this.LAC = LAC;
    }

    public String getCellId() {
        return CellId;
    }

    public void setCellId(String CellId) {
        this.CellId = CellId;
    }

    public String getRncId() {
        return RncId;
    }

    public void setRncId(String RncId) {
        this.RncId = RncId;
    }

    public int getRoaming() {
        return Roaming;
    }

    public void setRoaming(int Roaming) {
        this.Roaming = Roaming;
    }

    public int getDomestic_Roaming() {
        return Domestic_Roaming;
    }

    public void setDomestic_Roaming(int Domestic_Roaming) {
        this.Domestic_Roaming = Domestic_Roaming;
    }

    public int getSignalStrength() {
        return SignalStrength;
    }

    public void setSignalStrength(int SignalStrength) {
        this.SignalStrength = SignalStrength;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getSINR() {
        return SINR;
    }

    public void setSINR(String SINR) {
        this.SINR = SINR;
    }

    public String getRSRP() {
        return RSRP;
    }

    public void setRSRP(String RSRP) {
        this.RSRP = RSRP;
    }

    public String getRSSI() {
        return RSSI;
    }

    public void setRSSI(String RSSI) {
        this.RSSI = RSSI;
    }

    public String getENBID() {
        return eNBID;
    }

    public void setENBID(String eNBID) {
        this.eNBID = eNBID;
    }

    public String getCGI() {
        return CGI;
    }

    public void setCGI(String CGI) {
        this.CGI = CGI;
    }

    public String getCenterFreq() {
        return CenterFreq;
    }

    public void setCenterFreq(String CenterFreq) {
        this.CenterFreq = CenterFreq;
    }

    public String getTxPWR() {
        return TxPWR;
    }

    public void setTxPWR(String TxPWR) {
        this.TxPWR = TxPWR;
    }

    public int getLTE_state() {
        return LTE_state;
    }

    public void setLTE_state(int LTE_state) {
        this.LTE_state = LTE_state;
    }

    public String getPLMN_name() {
        return PLMN_name;
    }

    public void setPLMN_name(String PLMN_name) {
        this.PLMN_name = PLMN_name;
    }

    public int getBand() {
        return Band;
    }

    public void setBand(int Band) {
        this.Band = Band;
    }

    public String getDL_channel() {
        return DL_channel;
    }

    public void setDL_channel(String DL_channel) {
        this.DL_channel = DL_channel;
    }

    public String getUL_channel() {
        return UL_channel;
    }

    public void setUL_channel(String UL_channel) {
        this.UL_channel = UL_channel;
    }

    public String getRSRQ() {
        return RSRQ;
    }

    public void setRSRQ(String RSRQ) {
        this.RSRQ = RSRQ;
    }

    public int getEcIo() {
        return EcIo;
    }

    public void setEcIo(int EcIo) {
        this.EcIo = EcIo;
    }

    public int getRSCP() {
        return RSCP;
    }

    public void setRSCP(int RSCP) {
        this.RSCP = RSCP;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("GetNetworkInfoBean{");
        sb.append("\n").append("\t").append("PLMN ='").append(PLMN).append('\'');
        sb.append("\n").append("\t").append("NetworkType =").append(NetworkType);
        sb.append("\n").append("\t").append("NetworkName ='").append(NetworkName).append('\'');
        sb.append("\n").append("\t").append("SpnName ='").append(SpnName).append('\'');
        sb.append("\n").append("\t").append("LAC ='").append(LAC).append('\'');
        sb.append("\n").append("\t").append("CellId ='").append(CellId).append('\'');
        sb.append("\n").append("\t").append("RncId ='").append(RncId).append('\'');
        sb.append("\n").append("\t").append("Roaming =").append(Roaming);
        sb.append("\n").append("\t").append("Domestic_Roaming =").append(Domestic_Roaming);
        sb.append("\n").append("\t").append("SignalStrength =").append(SignalStrength);
        sb.append("\n").append("\t").append("mcc ='").append(mcc).append('\'');
        sb.append("\n").append("\t").append("mnc ='").append(mnc).append('\'');
        sb.append("\n").append("\t").append("SINR ='").append(SINR).append('\'');
        sb.append("\n").append("\t").append("RSRP ='").append(RSRP).append('\'');
        sb.append("\n").append("\t").append("RSSI ='").append(RSSI).append('\'');
        sb.append("\n").append("\t").append("eNBID ='").append(eNBID).append('\'');
        sb.append("\n").append("\t").append("CGI ='").append(CGI).append('\'');
        sb.append("\n").append("\t").append("CenterFreq ='").append(CenterFreq).append('\'');
        sb.append("\n").append("\t").append("TxPWR ='").append(TxPWR).append('\'');
        sb.append("\n").append("\t").append("LTE_state =").append(LTE_state);
        sb.append("\n").append("\t").append("PLMN_name ='").append(PLMN_name).append('\'');
        sb.append("\n").append("\t").append("Band =").append(Band);
        sb.append("\n").append("\t").append("DL_channel ='").append(DL_channel).append('\'');
        sb.append("\n").append("\t").append("UL_channel ='").append(UL_channel).append('\'');
        sb.append("\n").append("\t").append("RSRQ ='").append(RSRQ).append('\'');
        sb.append("\n").append("\t").append("EcIo =").append(EcIo);
        sb.append("\n").append("\t").append("RSCP =").append(RSCP);
        sb.append("\n}");
        return sb.toString();
    }
}
