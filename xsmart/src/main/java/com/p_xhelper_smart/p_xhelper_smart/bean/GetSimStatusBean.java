package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
public class GetSimStatusBean implements Serializable {

    /**
     * SIMState : 7
     * PinState : 3
     * PinRemainingTimes : 3
     * PukRemainingTimes : 3
     * SIMLockState : -1
     * SIMLockRemainingTimes : 0
     * PLMN : 46001
     * SPN :
     */

    public static int CONS_NOWN = 0;// 未知状态
    public static int CONS_SIM_CARD_DETECTED = 1;// 移除状态
    public static int CONS_PIN_REQUIRED = 2;// PIN码请求状态
    public static int CONS_PUK_REQUIRED = 3;// PUK码请求状态
    public static int CONS_SIM_LOCK_REQUIRED = 4;// SIM卡锁定状态
    public static int CONS_PUK_TIMES_USED_OUT = 5;// PUK码次数超限
    public static int CONS_SIM_CARD_ILLEGAL = 6;// 非法状态
    public static int CONS_SIM_CARD_READY = 7;// 准备状态
    public static int CONS_SIM_CARD_IS_INITING = 11;// 初始化状态

    private int SIMState;// SIM卡状态
    private int PinState;// PIN码状态
    private int PinRemainingTimes;// PIN码剩余次数
    private int PukRemainingTimes;// PUK剩余次数
    private int SIMLockState;// SIM卡锁定状态
    private int SIMLockRemainingTimes;// SIM卡锁定剩余时间
    private String PLMN;// 公共移动网络
    private String SPN;// 服务供应商名称

    public GetSimStatusBean() {
    }

    public int getSIMState() {
        return SIMState;
    }

    public void setSIMState(int SIMState) {
        this.SIMState = SIMState;
    }

    public int getPinState() {
        return PinState;
    }

    public void setPinState(int PinState) {
        this.PinState = PinState;
    }

    public int getPinRemainingTimes() {
        return PinRemainingTimes;
    }

    public void setPinRemainingTimes(int PinRemainingTimes) {
        this.PinRemainingTimes = PinRemainingTimes;
    }

    public int getPukRemainingTimes() {
        return PukRemainingTimes;
    }

    public void setPukRemainingTimes(int PukRemainingTimes) {
        this.PukRemainingTimes = PukRemainingTimes;
    }

    public int getSIMLockState() {
        return SIMLockState;
    }

    public void setSIMLockState(int SIMLockState) {
        this.SIMLockState = SIMLockState;
    }

    public int getSIMLockRemainingTimes() {
        return SIMLockRemainingTimes;
    }

    public void setSIMLockRemainingTimes(int SIMLockRemainingTimes) {
        this.SIMLockRemainingTimes = SIMLockRemainingTimes;
    }

    public String getPLMN() {
        return PLMN;
    }

    public void setPLMN(String PLMN) {
        this.PLMN = PLMN;
    }

    public String getSPN() {
        return SPN;
    }

    public void setSPN(String SPN) {
        this.SPN = SPN;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("GetSimStatusBean{");
        sb.append("\n").append("\t").append("SIMState =").append(SIMState);
        sb.append("\n").append("\t").append("PinState =").append(PinState);
        sb.append("\n").append("\t").append("PinRemainingTimes =").append(PinRemainingTimes);
        sb.append("\n").append("\t").append("PukRemainingTimes =").append(PukRemainingTimes);
        sb.append("\n").append("\t").append("SIMLockState =").append(SIMLockState);
        sb.append("\n").append("\t").append("SIMLockRemainingTimes =").append(SIMLockRemainingTimes);
        sb.append("\n").append("\t").append("PLMN ='").append(PLMN).append('\'');
        sb.append("\n").append("\t").append("SPN ='").append(SPN).append('\'');
        sb.append("\n}");
        return sb.toString();
    }
}
