package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.bean.SMSContentList;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSendSMSResultBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.SendSmsParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSendSMSResultHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SendSMSHelper;

import java.util.List;

/**
 * Created by qianli.ma on 2017/7/12.
 */

public abstract class SmsReSendHelper {

    private SMSContentList.SMSContentBean scb;
    private List<String> phoneNums;

    public SmsReSendHelper(SMSContentList.SMSContentBean scb, List<String> phoneNums) {
        this.scb = scb;
        this.phoneNums = phoneNums;
        send();
    }

    public abstract void success();
    public abstract void failed();

    public void send() {
        SendSmsParam xSendSmsParam = new SendSmsParam();
        xSendSmsParam.setSMSId(-1);
        xSendSmsParam.setSMSContent(scb.getSMSContent());
        xSendSmsParam.setSMSTime(RootUtils.getCurrentDate());
        xSendSmsParam.setPhoneNumber(phoneNums);
        SendSMSHelper xSendSMSHelper = new SendSMSHelper();
        xSendSMSHelper.setOnSendSmsSuccessListener(this::getSmsSendStatus);
        xSendSMSHelper.sendSms(xSendSmsParam);
    }

    private void getSmsSendStatus() {
        GetSendSMSResultHelper xGetSendSMSResultHelper = new GetSendSMSResultHelper();
        xGetSendSMSResultHelper.setOnGetSendSmsResultSuccessListener(bean -> {
            int sendStatus = bean.getSendStatus();
            if (sendStatus == GetSendSMSResultBean.CONS_SEND_STATUS_SENDING) {
                getSmsSendStatus();
            } else if (sendStatus == GetSendSMSResultBean.CONS_SEND_STATUS_SUCCESS) {
                success();
            } else {
                failed();
            }
        });
        xGetSendSMSResultHelper.getSendSmsResult();
    }
}
