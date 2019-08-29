package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wzhiqiang on 2019/7/31
 */
public class GetSMSContentListBean implements Serializable {

    private int Page;
    private int ContactId;
    private int TotalPageCount;
    private List<SMSContentListBean> SMSContentList;
    private List<String> PhoneNumber;

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

    public List<SMSContentListBean> getSMSContentList() {
        return SMSContentList;
    }

    public void setSMSContentList(List<SMSContentListBean> SMSContentList) {
        this.SMSContentList = SMSContentList;
    }

    public List<String> getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(List<String> PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

    public static class SMSContentListBean implements Serializable{
        
        /**
         * SMSId : 65537
         * SMSType : 2
         * sms_report : 0
         * report_id : 0
         * ReportStatus : 0
         * SMSContent : 窗前明月光疑是地上霜举头望明月 低头思故乡
         * SMSTime : 29-08-2019 13:52:31
         * report_time : 06-01-1980 00:00:00
         * SMSTimezone : 0
         */

        public static final int CONS_SMS_TYPE_READ = 0;
        public static final int CONS_SMS_TYPE_UNREAD = 1;
        public static final int CONS_SMS_TYPE_SENT = 2;
        public static final int CONS_SMS_TYPE_SENT_FAIL = 3;
        public static final int CONS_SMS_TYPE_REPORT = 4;
        public static final int CONS_SMS_TYPE_FLASH = 5;
        public static final int CONS_SMS_TYPE_DRAFT = 6;

        private int SMSId;
        private int SMSType;
        private int sms_report;
        private int report_id;
        private int ReportStatus;
        private String SMSContent;
        private String SMSTime;
        private String report_time;
        private int SMSTimezone;

        public SMSContentListBean() {
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

        public int getSms_report() {
            return sms_report;
        }

        public void setSms_report(int sms_report) {
            this.sms_report = sms_report;
        }

        public int getReport_id() {
            return report_id;
        }

        public void setReport_id(int report_id) {
            this.report_id = report_id;
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

        public String getReport_time() {
            return report_time;
        }

        public void setReport_time(String report_time) {
            this.report_time = report_time;
        }

        public int getSMSTimezone() {
            return SMSTimezone;
        }

        public void setSMSTimezone(int SMSTimezone) {
            this.SMSTimezone = SMSTimezone;
        }
    }
}
