package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class GetUsageRecordBean implements Serializable {

    /**
     * TConnTimes : 0
     * CurrConnTimes : 0
     * HCurrUseUL : 0
     * HCurrUseDL : 0
     * HUseData : 0
     * RCurrUseUL : 0
     * RCurrUseDL : 0
     * RoamUseData : 0
     * MonthlyPlan : 0
     * clear_time : 0
     */

    private int TConnTimes;//总共连接网络的时间,单位秒
    private int CurrConnTimes;//当前连接网络的时间，单位秒
    private int HCurrUseUL;//家庭网络下每月上传的数据量，单位比特 b
    private int HCurrUseDL;//家庭网络下每月下载的数据量，单位比特 b
    private int HUseData;//家庭网络下每月使用的数据量，单位比特 b
    private int RCurrUseUL;//漫游网络下每月上传的数据量，单位比特 b
    private int RCurrUseDL;//漫游网络下每月下载的数据量，单位比特 b
    private int RoamUseData;//漫游网络下每月使用的数据量，单位比特 b
    private int MonthlyPlan;//每月的最大使用量，单位比特 b
    private int clear_time;//clear hisotry record time.

    public GetUsageRecordBean() {
    }

    public int getTConnTimes() {
        return TConnTimes;
    }

    public void setTConnTimes(int TConnTimes) {
        this.TConnTimes = TConnTimes;
    }

    public int getCurrConnTimes() {
        return CurrConnTimes;
    }

    public void setCurrConnTimes(int CurrConnTimes) {
        this.CurrConnTimes = CurrConnTimes;
    }

    public int getHCurrUseUL() {
        return HCurrUseUL;
    }

    public void setHCurrUseUL(int HCurrUseUL) {
        this.HCurrUseUL = HCurrUseUL;
    }

    public int getHCurrUseDL() {
        return HCurrUseDL;
    }

    public void setHCurrUseDL(int HCurrUseDL) {
        this.HCurrUseDL = HCurrUseDL;
    }

    public int getHUseData() {
        return HUseData;
    }

    public void setHUseData(int HUseData) {
        this.HUseData = HUseData;
    }

    public int getRCurrUseUL() {
        return RCurrUseUL;
    }

    public void setRCurrUseUL(int RCurrUseUL) {
        this.RCurrUseUL = RCurrUseUL;
    }

    public int getRCurrUseDL() {
        return RCurrUseDL;
    }

    public void setRCurrUseDL(int RCurrUseDL) {
        this.RCurrUseDL = RCurrUseDL;
    }

    public int getRoamUseData() {
        return RoamUseData;
    }

    public void setRoamUseData(int RoamUseData) {
        this.RoamUseData = RoamUseData;
    }

    public int getMonthlyPlan() {
        return MonthlyPlan;
    }

    public void setMonthlyPlan(int MonthlyPlan) {
        this.MonthlyPlan = MonthlyPlan;
    }

    public int getClear_time() {
        return clear_time;
    }

    public void setClear_time(int clear_time) {
        this.clear_time = clear_time;
    }
}
