package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/8/9 0009.
 */
public class GetSingleSMSBean implements Serializable {
    
    private String PhoneNumber;
    private int SMSType;
    private String SMSContent;
    private String SMSId;
    private String SMSTime;

    public GetSingleSMSBean() {
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public int getSMSType() {
        return SMSType;
    }

    public void setSMSType(int SMSType) {
        this.SMSType = SMSType;
    }

    public String getSMSContent() {
        return SMSContent;
    }

    public void setSMSContent(String SMSContent) {
        this.SMSContent = SMSContent;
    }

    public String getSMSId() {
        return SMSId;
    }

    public void setSMSId(String SMSId) {
        this.SMSId = SMSId;
    }

    public String getSMSTime() {
        return SMSTime;
    }

    public void setSMSTime(String SMSTime) {
        this.SMSTime = SMSTime;
    }
}
