package com.alcatel.wifilink.root.helper;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.ue.root_activity.HomeRxActivity;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSMSContactListBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSMSContactListHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSmsInitStateHelper;

import java.util.Set;

/**
 * Created by qianli.ma on 2017/6/17.
 */

public class SmsCountHelper {

    private static Activity activity;
    private static TextView tv;

    /**
     * 显示主页消息数量
     *
     * @param tv
     */
    public static void setSmsCount(Activity activity, TextView tv) {
        // TODO: 2019/8/6 这个activity是否可删
        SmsCountHelper.activity = activity;
        // check the init state
        GetSmsInitStateHelper xGetSmsInitStateHelper = new GetSmsInitStateHelper();
        xGetSmsInitStateHelper.setOnGetSmsInitStateSuccessListener(bean -> {
            if (bean.getState() == GetSmsInitStateHelper.SMS_COMPLETE) {
                // getInstant sms contents
                getSmsContactList(tv);
            }
        });
        xGetSmsInitStateHelper.getSmsInitState();
    }

    private static void getSmsContactList(TextView mTvSmsCount) {
        GetSMSContactListHelper xGetSMSContactListHelper = new GetSMSContactListHelper();
        xGetSMSContactListHelper.setOnGetSmsContactListSuccessListener(bean -> {
            // caculate the sms count
            int unReadCount = 0;
            for (GetSMSContactListBean.SMSContacBean smsContact : bean.getSMSContactList()) {

                // 到缓冲区去查看是否有缓冲的未读短信数量
                int unreadCache = getUnreadCache(smsContact.getContactId());
                int unreadCount = smsContact.getUnreadCount();
                // 取得两者当中数量不为0的那一个值
                if (unreadCache > 0) {
                    unReadCount += unreadCache;
                } else if (unreadCount > 0) {
                    unReadCount += unreadCount;
                } else {
                    unReadCount += unreadCount;
                }
            }
            // show sms ui according the count
            if (unReadCount <= 0) {
                mTvSmsCount.setVisibility(View.GONE);
            } else if (unReadCount < 10) {
                mTvSmsCount.setVisibility(View.VISIBLE);
                mTvSmsCount.setText(String.valueOf(unReadCount));
                int nDrawable = R.drawable.tab_sms_new;
                mTvSmsCount.setBackgroundResource(nDrawable);
            } else {
                mTvSmsCount.setVisibility(View.VISIBLE);
                mTvSmsCount.setText("");
                int nDrawable = R.drawable.tab_sms_new_9_plus;
                mTvSmsCount.setBackgroundResource(nDrawable);
            }
        });
        xGetSMSContactListHelper.getSMSContactList(0);
    }

    /**
     * 获取缓冲区里的未读短信
     *
     * @param contactId
     * @return
     */
    public static int getUnreadCache(long contactId) {
        int unreadCache = 0;
        Set<Long> contactIds = HomeRxActivity.smsUnreadMap.keySet();
        for (Long id : contactIds) {
            if (id == contactId) {
                unreadCache = HomeRxActivity.smsUnreadMap.get(id);
            }
        }
        return unreadCache;
    }

}
