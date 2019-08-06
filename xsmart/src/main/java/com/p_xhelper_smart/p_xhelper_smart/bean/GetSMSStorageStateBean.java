package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
public class GetSMSStorageStateBean implements Serializable {


    /**
     * UnreadReport : 0
     * LeftCount : 98
     * MaxCount : 100
     * TUseCount : 2
     * UnreadSMSCount : 0
     */

    public static int CONS_NO_NEW_STATUS_REPORT = 0;
    public static int CONS_EXIT_NEW_STATUS_REPORT = 1;

    private int UnreadReport;// 未读状态
    private int LeftCount;// sms存储剩余空间
    private int MaxCount;// 存储sms的当前最大空间(sms计数)
    private int TUseCount;// 当前存储短信总数
    private int UnreadSMSCount;// 当前存储未读短信计数

    public GetSMSStorageStateBean() {
    }

    public int getUnreadReport() {
        return UnreadReport;
    }

    public void setUnreadReport(int UnreadReport) {
        this.UnreadReport = UnreadReport;
    }

    public int getLeftCount() {
        return LeftCount;
    }

    public void setLeftCount(int LeftCount) {
        this.LeftCount = LeftCount;
    }

    public int getMaxCount() {
        return MaxCount;
    }

    public void setMaxCount(int MaxCount) {
        this.MaxCount = MaxCount;
    }

    public int getTUseCount() {
        return TUseCount;
    }

    public void setTUseCount(int TUseCount) {
        this.TUseCount = TUseCount;
    }

    public int getUnreadSMSCount() {
        return UnreadSMSCount;
    }

    public void setUnreadSMSCount(int UnreadSMSCount) {
        this.UnreadSMSCount = UnreadSMSCount;
    }
}
