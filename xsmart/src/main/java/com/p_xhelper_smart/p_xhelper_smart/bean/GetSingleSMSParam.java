package com.p_xhelper_smart.p_xhelper_smart.bean;

import java.io.Serializable;

/*
 * Created by qianli.ma on 2019/8/9 0009.
 */
public class GetSingleSMSParam implements Serializable {

    private long SMSId;

    public GetSingleSMSParam() {
    }

    public GetSingleSMSParam(long SMSId) {
        this.SMSId = SMSId;
    }

    public long getSMSId() {
        return SMSId;
    }

    public void setSMSId(long SMSId) {
        this.SMSId = SMSId;
    }
}
