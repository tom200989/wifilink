package com.alcatel.wifilink.root.bean;

/**
 * Created by qianli.ma on 2017/6/20.
 */

public class UsageSetting {

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

    private int BillingDay;
    private long MonthlyPlan;
    private double UsedData;
    private int Unit;
    private int TimeLimitFlag;
    private int TimeLimitTimes;
    private int UsedTimes;
    private int AutoDisconnFlag;

    public int getBillingDay() {
        return BillingDay;
    }

    public void setBillingDay(int billingDay) {
        BillingDay = billingDay;
    }

    public long getMonthlyPlan() {
        return MonthlyPlan;
    }

    public void setMonthlyPlan(long monthlyPlan) {
        MonthlyPlan = monthlyPlan;
    }

    public double getUsedData() {
        return UsedData;
    }

    public void setUsedData(double usedData) {
        UsedData = usedData;
    }

    public int getUnit() {
        return Unit;
    }

    public void setUnit(int unit) {
        Unit = unit;
    }

    public int getTimeLimitFlag() {
        return TimeLimitFlag;
    }

    public void setTimeLimitFlag(int timeLimitFlag) {
        TimeLimitFlag = timeLimitFlag;
    }

    public int getTimeLimitTimes() {
        return TimeLimitTimes;
    }

    public void setTimeLimitTimes(int timeLimitTimes) {
        TimeLimitTimes = timeLimitTimes;
    }

    public int getUsedTimes() {
        return UsedTimes;
    }

    public void setUsedTimes(int usedTimes) {
        UsedTimes = usedTimes;
    }

    public int getAutoDisconnFlag() {
        return AutoDisconnFlag;
    }

    public void setAutoDisconnFlag(int autoDisconnFlag) {
        AutoDisconnFlag = autoDisconnFlag;
    }
}
