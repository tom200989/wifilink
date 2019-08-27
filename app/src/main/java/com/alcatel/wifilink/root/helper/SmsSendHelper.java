package com.alcatel.wifilink.root.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.ProgressUtils;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.alcatel.wifilink.root.utils.ToastTool;
import com.p_xhelper_smart.p_xhelper_smart.bean.SendSmsParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSendSMSResultHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SendSMSHelper;

import java.util.List;

public abstract class SmsSendHelper {

    private Context context;
    private List<String> phoneNums;
    private String content;
    private ProgressDialog pop;
    private Handler handler;
    private int count = 0;

    public SmsSendHelper(Context context, List<String> phoneNums, String content) {
        this.context = context;
        this.phoneNums = phoneNums;
        this.content = content;
        handler = new Handler();
        send();
    }

    /**
     * include the success or failed
     */
    public abstract void sendFinish(int status);

    public void send() {
        popDismiss();
        pop = new ProgressUtils(context).getProgressPop(context.getString(R.string.sms_sending));

        SendSmsParam xSendSmsParam = new SendSmsParam();
        xSendSmsParam.setSMSId(-1);
        xSendSmsParam.setSMSContent(content);
        xSendSmsParam.setSMSTime(RootUtils.getCurrentDate());
        xSendSmsParam.setPhoneNumber(phoneNums);
        SendSMSHelper xSendSMSHelper = new SendSMSHelper();
        xSendSMSHelper.setOnSendSmsSuccessListener(() -> {
            handler.postDelayed(this::getSendStatus,3000);/* 发送完毕获取短信状态(延迟3秒) */
            // getSendStatus();/* 发送完毕获取短信状态 */
        });
        xSendSMSHelper.setOnSendFailListener(this::popDismiss);
        xSendSMSHelper.setOnSendSmsFailListener(this::popDismiss);
        xSendSMSHelper.setOnLastMessageListener(this::popDismiss);
        xSendSMSHelper.setOnSpaceFullListener(this::popDismiss);
        xSendSMSHelper.sendSms(xSendSmsParam);
    }

    /**
     * 发送完毕获取短信状态
     */
    private void getSendStatus() {
        GetSendSMSResultHelper xGetSendSMSResultHelper = new GetSendSMSResultHelper();
        xGetSendSMSResultHelper.setOnGetSendSmsResultSuccessListener(bean -> {
            int sendStatus = bean.getSendStatus();
            if (sendStatus == Cons.NONE) {
                ToastTool.show(context, context.getString(R.string.none));
            } else if (sendStatus == Cons.SENDING) {
                noCostCheck();// 间隔5秒,获取5次,如仍是sending则认为欠费
            } else if (sendStatus == Cons.SUCCESS) {
                ToastTool.show(context, context.getString(R.string.succeed));
            } else if (sendStatus == Cons.FAIL_STILL_SENDING_LAST_MSG) {
                noCostCheck();// 间隔5秒,获取5次,如仍是sending则认为欠费
            } else if (sendStatus == Cons.FAIL_WITH_MEMORY_FULL) {
                ToastTool.show(context, context.getString(R.string.fail_with_memory_full));
            } else if (sendStatus == Cons.FAIL) {
                ToastTool.show(context, context.getString(R.string.fail));
            }
            sendFinish(bean.getSendStatus());
            // 临时计数清零
            if (sendStatus != Cons.SENDING & sendStatus != Cons.FAIL_STILL_SENDING_LAST_MSG) {
                popDismiss();
            }
        });
        xGetSendSMSResultHelper.setOnGetSendFailListener(this::popDismiss);
        xGetSendSMSResultHelper.setOnGetSendSmsResultFailListener(this::popDismiss);
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
            ToastTool.show(context, context.getString(R.string.check_sim_normal_or_no_cost));
            popDismiss();
        }
    }

    /* -------------------------------------------- helper -------------------------------------------- */
    public void popDismiss() {
        // 计数器清零
        count = 0;
        if (pop != null) {
            pop.dismiss();
            pop = null;
        }
    }

}
