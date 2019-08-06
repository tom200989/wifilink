package com.p_xhelper_smart.p_xhelper_smart.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class GetSMSContactListBean implements Serializable {

    /**
     * SMSContactList : [{"ContactId":1,"PhoneNumber":["18617092862"],"SMSId":65538,"SMSType":1,"ReportStatus":0,"SMSContent":"88888888554","SMSTime":"2016-06-30 10:02:22","UnreadCount":2,"TSMSCount":2}]
     * Page : 0
     * TotalPageCount : 1
     */
    @JSONField(name = "Page")
    private int Page;
    @JSONField(name = "TotalPageCount")
    private int TotalPageCount;
    @JSONField(name = "SMSContactList")
    private List<SMSContacBean> SMSContactList;

    public GetSMSContactListBean() {
    }

    public int getPage() {
        return Page;
    }

    public void setPage(int Page) {
        this.Page = Page;
    }

    public int getTotalPageCount() {
        return TotalPageCount;
    }

    public void setTotalPageCount(int TotalPageCount) {
        this.TotalPageCount = TotalPageCount;
    }

    public List<SMSContacBean> getSMSContactList() {
        return SMSContactList;
    }

    public void setSMSContactList(List<SMSContacBean> SMSContactList) {
        this.SMSContactList = SMSContactList;
    }

    public static class SMSContacBean implements Serializable{
        /**
         * ContactId : 1
         * PhoneNumber : ["18617092862"]
         * SMSId : 65538
         * SMSType : 1
         * ReportStatus : 0
         * SMSContent : 88888888554
         * SMSTime : 2016-06-30 10:02:22
         * UnreadCount : 2
         * TSMSCount : 2
         */

        private int ContactId;//The list ID
        private int SMSId;//最新的短信id
        private int SMSType;//短信类型 0: read   1: unread   2: sent
                            // 3: sent failed   4: report  5: flash 6: draft
        private int ReportStatus;
        private String SMSContent;//最新短信信息
        private String SMSTime;//最新一条短信的时间，格式YYYY-MM-DD hh:mm: ss
        private int UnreadCount;//未读短信的数量
        private int TSMSCount;//总共短信数量
        private List<String> PhoneNumber;//短信联系人信息，可能1个或多个

        public static final int CONS_SMS_TYPE_READ = 0;
        public static final int CONS_SMS_TYPE_UNREAD = 1;
        public static final int CONS_SMS_TYPE_SENT = 2;
        public static final int CONS_SMS_TYPE_SENT_FAIL = 3;
        public static final int CONS_SMS_TYPE_REPORT = 4;
        public static final int CONS_SMS_TYPE_FLASH = 5;
        public static final int CONS_SMS_TYPE_DRAFT = 6;

        public SMSContacBean() {
        }

        public int getContactId() {
            return ContactId;
        }

        public void setContactId(int ContactId) {
            this.ContactId = ContactId;
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

        public int getUnreadCount() {
            return UnreadCount;
        }

        public void setUnreadCount(int UnreadCount) {
            this.UnreadCount = UnreadCount;
        }

        public int getTSMSCount() {
            return TSMSCount;
        }

        public void setTSMSCount(int TSMSCount) {
            this.TSMSCount = TSMSCount;
        }

        public List<String> getPhoneNumber() {
            return PhoneNumber;
        }

        public void setPhoneNumber(List<String> PhoneNumber) {
            this.PhoneNumber = PhoneNumber;
        }
    }
}
