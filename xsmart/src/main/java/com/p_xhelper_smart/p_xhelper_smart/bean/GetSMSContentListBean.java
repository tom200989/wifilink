package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wzhiqiang on 2019/7/31
 */
public class GetSMSContentListBean implements Serializable {


    /**
     * SMSContentList : [{"SMSId":65538,"SMSType":0,"ReportStatus":0,"SMSContent":"88888888554","SMSTime":"2016-06-30 10:02:22"},{"SMSId":65537,"SMSType":0,"ReportStatus":0,"SMSContent":"874651","SMSTime":"2016-06-30 10:01:48"}]
     * Page : 0
     * ContactId : 1
     * PhoneNumber : ["18617092862"]
     * TotalPageCount : 1
     */

    private int Page;
    private int ContactId;
    private int TotalPageCount;
    private List<SMSContentBean> SMSContentList;
    private List<String> PhoneNumber;////发送短信给谁，可能一个或更多人

    public GetSMSContentListBean() {
    }

    public int getPage() {
        return Page;
    }

    public void setPage(int Page) {
        this.Page = Page;
    }

    public int getContactId() {
        return ContactId;
    }

    public void setContactId(int ContactId) {
        this.ContactId = ContactId;
    }

    public int getTotalPageCount() {
        return TotalPageCount;
    }

    public void setTotalPageCount(int TotalPageCount) {
        this.TotalPageCount = TotalPageCount;
    }

    public List<SMSContentBean> getSMSContentList() {
        return SMSContentList;
    }

    public void setSMSContentList(List<SMSContentBean> SMSContentList) {
        this.SMSContentList = SMSContentList;
    }

    public List<String> getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(List<String> PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

    public static class SMSContentBean implements Serializable{
        /**
         * SMSId : 65538
         * SMSType : 0
         * ReportStatus : 0
         * SMSContent : 88888888554
         * SMSTime : 2016-06-30 10:02:22
         */

        private int SMSId;
        private int SMSType;//短信类型 0: read   1: unread   2: sent
                            // 3: sent failed   4: report  5: flash 6: draft
        private int ReportStatus;
        private String SMSContent;//最新短信信息，截前40个字符
        private String SMSTime;//最新一条短信的时间，格式YYYY-MM-DD hh:mm: ss

        public static final int CONS_SMS_TYPE_READ = 0;
        public static final int CONS_SMS_TYPE_UNREAD = 1;
        public static final int CONS_SMS_TYPE_SENT = 2;
        public static final int CONS_SMS_TYPE_SENT_FAIL = 3;
        public static final int CONS_SMS_TYPE_REPORT = 4;
        public static final int CONS_SMS_TYPE_FLASH = 5;
        public static final int CONS_SMS_TYPE_DRAFT = 6;

        public SMSContentBean() {
        }

        public int getSMSId() {
            return SMSId;
        }

        public void setSMSId(int SMSId) {
            this.SMSId = SMSId;
        }

        public int getSMSType() {
            return SMSType;
        }

        public void setSMSType(int SMSType) {
            this.SMSType = SMSType;
        }

        public int getReportStatus() {
            return ReportStatus;
        }

        public void setReportStatus(int ReportStatus) {
            this.ReportStatus = ReportStatus;
        }

        public String getSMSContent() {
            return SMSContent;
        }

        public void setSMSContent(String SMSContent) {
            this.SMSContent = SMSContent;
        }

        public String getSMSTime() {
            return SMSTime;
        }

        public void setSMSTime(String SMSTime) {
            this.SMSTime = SMSTime;
        }
    }
}
