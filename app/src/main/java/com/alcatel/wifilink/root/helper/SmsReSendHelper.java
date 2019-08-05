package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.bean.SMSContentList;
import com.alcatel.wifilink.root.bean.SMSSendParam;
import com.alcatel.wifilink.root.bean.SMSSendResult;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseObject;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.utils.DataUtils;

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
        SMSSendParam ssp = new SMSSendParam(-1, scb.getSMSContent(), DataUtils.getCurrent(), phoneNums);
        RX.getInstant().sendSMS(ssp, new ResponseObject() {
            @Override
            protected void onSuccess(Object result) {
                getSmsSendStatus();
            }

            private void getSmsSendStatus() {
                RX.getInstant().GetSendSMSResult(new ResponseObject<SMSSendResult>() {
                    @Override
                    protected void onSuccess(SMSSendResult result) {
                        int sendStatus = result.getSendStatus();
                        if (sendStatus == Cons.SENDING) {
                            getSmsSendStatus();
                        } else if (sendStatus == Cons.SUCCESS) {
                            success();
                        } else {
                            failed();
                        }
                    }
                });
            }
        });
    }

}
