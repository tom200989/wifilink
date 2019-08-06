package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wzhiqiang on 2019/8/1
 */
public class SaveSmsParam implements Serializable {


    /**
     * SMSId : 1
     * PhoneNumber : ["134544555"]
     * SMSContent : hi!
     * SMSTime : 2014-09-11 18:06:00
     */

    private int SMSId; //smsid
    private String SMSContent;//The all content of the SMS.
    private String SMSTime;//发送短信的时间，格式是YYYY-MM-DDhh:mm:ss
    private List<String> PhoneNumber;//发送短信给谁，可能一个或更多人

    public SaveSmsParam() {
    }

    public int getSMSId() {
        return SMSId;
    }

    public void setSMSId(int SMSId) {
        this.SMSId = SMSId;
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

    public List<String> getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(List<String> PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }
}
