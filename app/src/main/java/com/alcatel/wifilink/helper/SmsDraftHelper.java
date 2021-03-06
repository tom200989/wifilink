package com.alcatel.wifilink.helper;

import android.app.Activity;

import com.alcatel.wifilink.utils.RootUtils;
import com.p_xhelper_smart.p_xhelper_smart.bean.DeleteSmsParam;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSMSContactListBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSMSContentListBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSmsContentListParam;
import com.p_xhelper_smart.p_xhelper_smart.bean.SaveSmsParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.DeleteSMSHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSMSContentListHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SaveSMSHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by qianli.ma on 2017/7/12.
 */

public class SmsDraftHelper {

    private Activity activity;
    private long contactId;
    private OnNoSimListener onNoSimListener;
    private OnGetDraftListener onGetDraftListener;
    private OnClearDraftListener onClearDraftListener;
    private OnSaveDraftListener onSaveDraftListener;

    public SmsDraftHelper(Activity activity, long contactId) {
        this.activity = activity;
        this.contactId = contactId;
    }

    public interface OnNoSimListener {
        void noSim();
    }

    public void setOnNoSimListener(OnNoSimListener onNoSimListener) {
        this.onNoSimListener = onNoSimListener;
    }

    public interface OnGetDraftListener {
        void getDraft(String draft);
    }

    public void setOnGetDraftListener(OnGetDraftListener onGetDraftListener) {
        this.onGetDraftListener = onGetDraftListener;
    }

    public interface OnClearDraftListener {
        void clear();
    }

    public void setOnClearDraftListener(OnClearDraftListener onClearDraftListener) {
        this.onClearDraftListener = onClearDraftListener;
    }

    public interface OnSaveDraftListener {
        void success();
    }

    public void setOnSaveDraftListener(OnSaveDraftListener onSaveDraftListener) {
        this.onSaveDraftListener = onSaveDraftListener;
    }

    /* **** getDraftSms: 获取草稿短信--> 只执行1次 **** */
    public void getDraftSms() {

        GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
        xGetSimStatusHelper.setOnGetSimStatusSuccessListener(result -> {
            if (result.getSIMState() != GetSimStatusBean.CONS_SIM_CARD_READY) {// no sim
                if (onNoSimListener != null) {
                    onNoSimListener.noSim();
                }
            } else {// normal
                getDraftContent(activity, contactId);
            }
        });
        xGetSimStatusHelper.getSimStatus();
    }

    /* 获取草稿短信 */
    private void getDraftContent(Activity activity, long contactId) {
        GetSmsContentListParam getSmsContentListParam = new GetSmsContentListParam();
        getSmsContentListParam.setPage(0);
        getSmsContentListParam.setContactId((int) contactId);
        GetSMSContentListHelper xGetSMSContentListHelper = new GetSMSContentListHelper();
        xGetSMSContentListHelper.setOnGetSmsContentListSuccessListener(bean -> {
            List<GetSMSContentListBean.SMSContentListBean> scbs = new ArrayList<>();
            for (GetSMSContentListBean.SMSContentListBean scb : bean.getSMSContentList()) {
                if (scb.getSMSType() == GetSMSContactListBean.SMSContacBean.CONS_SMS_TYPE_DRAFT) {// add draft sms
                    GetSMSContentListBean.SMSContentListBean tempSmsContentBean = new GetSMSContentListBean.SMSContentListBean();
                    tempSmsContentBean.setReportStatus(scb.getReportStatus());
                    tempSmsContentBean.setSMSContent(scb.getSMSContent());
                    tempSmsContentBean.setSMSId(scb.getSMSId());
                    tempSmsContentBean.setSMSTime(scb.getSMSTime());
                    tempSmsContentBean.setSMSType(scb.getSMSType());
                    scbs.add(tempSmsContentBean);
                }
            }
            activity.runOnUiThread(() -> {
                String draft = "";
                if (scbs.size() > 0) {
                    Collections.sort(scbs, new SmsContentSortHelper());
                    GetSMSContentListBean.SMSContentListBean scb = scbs.get(0);
                    draft = scb.getSMSContent();
                }
                if (onGetDraftListener != null) {
                    onGetDraftListener.getDraft(draft);
                }
            });
        });
        xGetSMSContentListHelper.getSMSContentList(getSmsContentListParam);
    }

    /* 清空草稿短信 */
    public void clearDraft() {

        GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
        xGetSimStatusHelper.setOnGetSimStatusSuccessListener(result -> {
            if (result.getSIMState() != GetSimStatusBean.CONS_SIM_CARD_READY) {
                if (onNoSimListener != null) {
                    onNoSimListener.noSim();
                }
            } else {
                clearSms(contactId);
            }
        });
        xGetSimStatusHelper.getSimStatus();
    }

    /* 清空指定的草稿短信 */
    private void clearSms(long contactId) {
        // 先清空原先的草稿
        List<Long> draftList = new ArrayList<>();

        //用smart框架内部param代替旧的Param
        GetSmsContentListParam getSmsContentListParam = new GetSmsContentListParam();
        getSmsContentListParam.setPage(0);
        getSmsContentListParam.setContactId((int) contactId);

        GetSMSContentListHelper xGetSMSContentListHelper = new GetSMSContentListHelper();
        xGetSMSContentListHelper.setOnGetSmsContentListSuccessListener(bean -> {
            for (GetSMSContentListBean.SMSContentListBean scb : bean.getSMSContentList()) {
                if (scb.getSMSType() == GetSMSContactListBean.SMSContacBean.CONS_SMS_TYPE_DRAFT) {
                    draftList.add((long) scb.getSMSId());
                }
            }
            // 删除全部草稿短信
            DeleteSmsParam xDeleteSmsParam = new DeleteSmsParam();
            xDeleteSmsParam.setDelFlag(DeleteSmsParam.CONS_DELETE_MORE);
            xDeleteSmsParam.setSMSArray(draftList);
            DeleteSMSHelper xDeleteSMSHelper = new DeleteSMSHelper();
            xDeleteSMSHelper.setOnDeleteSmsSuccessListener(object -> {
                if (onClearDraftListener != null) {
                    onClearDraftListener.clear();
                }
            });
            xDeleteSMSHelper.setOnDeleteSmsFailListener(() -> {
                if (onClearDraftListener != null) {
                    onClearDraftListener.clear();
                }
            });
            xDeleteSMSHelper.setOnDeleteFailListener(() -> {
                if (onClearDraftListener != null) {
                    onClearDraftListener.clear();
                }
            });
            xDeleteSMSHelper.deleteSms(xDeleteSmsParam);

        });
        xGetSMSContentListHelper.getSMSContentList(getSmsContentListParam);
    }

    /* 保存草稿短信 */
    public void saveDraftSms(List<String> phoneNum, String content) {
        SaveSmsParam xSaveSmsParam = new SaveSmsParam();
        xSaveSmsParam.setSMSId(-1);
        xSaveSmsParam.setSMSContent(content);
        xSaveSmsParam.setSMSTime(RootUtils.getCurrentDate());
        xSaveSmsParam.setPhoneNumber(phoneNum);
        SaveSMSHelper xSaveSMSHelper = new SaveSMSHelper();
        xSaveSMSHelper.setOnSaveSMSSuccessListener(() -> {
            if (onSaveDraftListener != null) {
                onSaveDraftListener.success();
            }
        });
        xSaveSMSHelper.saveSms(xSaveSmsParam);
    }

}
