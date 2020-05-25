package com.alcatel.wifilink.helper;

import android.content.Context;
import android.os.Handler;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.utils.RootUtils;
import com.alcatel.wifilink.utils.ToastTool;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSendSMSResultBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.SendSmsParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSendSMSResultHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SendSMSHelper;

import java.util.List;

public abstract class SmsSendHelper {

    private Context context;
    private List<String> phoneNums;
    private String content;
    private Handler handler;
    private int count = 0;
    private String lastMsgTime;

    public SmsSendHelper(Context context, List<String> phoneNums, String content,String lastMsgTime) {
        this.context = context;
        this.phoneNums = phoneNums;
        this.content = content;
        this.lastMsgTime = lastMsgTime;
        handler = new Handler();
        send();
    }

    /**
     * include the success or failed
     */
    public abstract void sendFinish(int status);

    public void send() {
        prepare();
        SendSmsParam xSendSmsParam = new SendSmsParam();
        xSendSmsParam.setSMSId(-1);
        xSendSmsParam.setSMSContent(content);
        xSendSmsParam.setSMSTime(RootUtils.getSendTime(lastMsgTime));
        xSendSmsParam.setPhoneNumber(phoneNums);
        SendSMSHelper xSendSMSHelper = new SendSMSHelper();
        xSendSMSHelper.setOnSendSmsSuccessListener(() -> {
            handler.postDelayed(this::getSendStatus, 3000);/* 发送完毕获取短信状态(延迟3秒) */
            // getSendStatus();/* 发送完毕获取短信状态 */
        });
        xSendSMSHelper.setOnSendFailListener(this::done);
        xSendSMSHelper.setOnSendSmsFailListener(this::done);
        xSendSMSHelper.setOnLastMessageListener(this::done);
        xSendSMSHelper.setOnSpaceFullListener(this::done);
        xSendSMSHelper.sendSms(xSendSmsParam);
    }

    /**
     * 发送完毕获取短信状态
     */
    private void getSendStatus() {
        GetSendSMSResultHelper xGetSendSMSResultHelper = new GetSendSMSResultHelper();
        xGetSendSMSResultHelper.setOnGetSendSmsResultSuccessListener(bean -> {
            int sendStatus = bean.getSendStatus();
            if (sendStatus == GetSendSMSResultBean.CONS_SEND_STATUS_NONE) {
                ToastTool.show(context, context.getString(R.string.hh70_none));
            } else if (sendStatus == GetSendSMSResultBean.CONS_SEND_STATUS_SENDING) {
                noCostCheck();// 间隔5秒,获取5次,如仍是sending则认为欠费
            } else if (sendStatus == GetSendSMSResultBean.CONS_SEND_STATUS_SUCCESS) {
                ToastTool.show(context, context.getString(R.string.hh70_succeed));
            } else if (sendStatus == GetSendSMSResultBean.CONS_SEND_STATUS_FAIL_LAST_MSG) {
                noCostCheck();// 间隔5秒,获取5次,如仍是sending则认为欠费
            } else if (sendStatus == GetSendSMSResultBean.CONS_SEND_STATUS_FAIL_MEMORY_FULL) {
                ToastTool.show(context, context.getString(R.string.hh70_memory_full));
            } else if (sendStatus == GetSendSMSResultBean.CONS_SEND_STATUS_FAIL) {
                ToastTool.show(context, context.getString(R.string.hh70_fail));
            }
            sendFinish(bean.getSendStatus());
            // 临时计数清零
            if (sendStatus != GetSendSMSResultBean.CONS_SEND_STATUS_SENDING & sendStatus != GetSendSMSResultBean.CONS_SEND_STATUS_FAIL_LAST_MSG) {
                done();
            }
        });
        xGetSendSMSResultHelper.setOnGetSendFailListener(this::done);
        xGetSendSMSResultHelper.setOnGetSendSmsResultFailListener(this::done);
        xGetSendSMSResultHelper.getSendSmsResult();
    }

    /**
     * 间隔5秒,获取5次,如仍是sending则认为欠费
     */
    private void noCostCheck() {
        // 5秒后重新获取-获取5次失败认为欠费
        if (count <= 7) {
            handler.postDelayed(() -> {
                count++;
                getSendStatus();
            }, 5000);
        } else {
            ToastTool.show(context, context.getString(R.string.hh70_check_sim_cost));
            done();
        }
    }

    /**
     * 发送前
     */
    public abstract void prepare();

    /**
     * 发送后
     */
    public abstract void done();

}
