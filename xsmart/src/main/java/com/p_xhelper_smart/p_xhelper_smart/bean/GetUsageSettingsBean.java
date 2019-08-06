package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class GetUsageSettingsBean implements Serializable {

    /**
     * BillingDay : 1
     * MonthlyPlan : 0
     * UsedData : 0
     * Unit : 1
     * TimeLimitFlag : 0
     * TimeLimitTimes : 5
     * UsedTimes : 0
     * AutoDisconnFlag : 0
     */

    private int BillingDay;//Min:1, Max:31
    private int MonthlyPlan;//每个月的流量限制，如果是0，代表没限制
    private int UsedData;//一个月中总漫游和家庭使用的流量数据
    private int Unit; // 流量单位标识 0: MB  1: GB   2: KB
    private int TimeLimitFlag; //0: disable 1: enable
    private int TimeLimitTimes;//如果打开了时间限制，那就要限制时间，单位为分钟
    private int UsedTimes;//打开了时间限制后，已经使用了了多长时间，单位为分钟
    private int AutoDisconnFlag; //0: disable, not auto disconnect   1: enable, auto disconnect

    public static final int CONS_UNIT_MB = 0;
    public static final int CONS_UNIT_GB = 1;
    public static final int CONS_UNIT_KB = 2;

    public static final int CONS_AUTO_DISCONNECT_DISABLE = 0;
    public static final int CONS_AUTO_DISCONNECT_ABLE = 1;

    public static final int CONS_TIME_LIMIT_DISABLE = 0;
    public static final int CONS_TIME_LIMIT_ABLE = 1;

    public GetUsageSettingsBean() {
    }

    public int getBillingDay() {
        return BillingDay;
    }

    public void setBillingDay(int BillingDay) {
        this.BillingDay = BillingDay;
    }

    public int getMonthlyPlan() {
        return MonthlyPlan;
    }

    public void setMonthlyPlan(int MonthlyPlan) {
        this.MonthlyPlan = MonthlyPlan;
    }

    public int getUsedData() {
        return UsedData;
    }

    public void setUsedData(int UsedData) {
        this.UsedData = UsedData;
    }

    public int getUnit() {
        return Unit;
    }

    public void setUnit(int Unit) {
        this.Unit = Unit;
    }

    public int getTimeLimitFlag() {
        return TimeLimitFlag;
    }

    public void setTimeLimitFlag(int TimeLimitFlag) {
        this.TimeLimitFlag = TimeLimitFlag;
    }

    public int getTimeLimitTimes() {
        return TimeLimitTimes;
    }

    public void setTimeLimitTimes(int TimeLimitTimes) {
        this.TimeLimitTimes = TimeLimitTimes;
    }

    public int getUsedTimes() {
        return UsedTimes;
    }

    public void setUsedTimes(int UsedTimes) {
        this.UsedTimes = UsedTimes;
    }

    public int getAutoDisconnFlag() {
        return AutoDisconnFlag;
    }

    public void setAutoDisconnFlag(int AutoDisconnFlag) {
        this.AutoDisconnFlag = AutoDisconnFlag;
    }
}
