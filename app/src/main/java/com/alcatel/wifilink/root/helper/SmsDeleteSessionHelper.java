package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.SMSContentList;
import com.alcatel.wifilink.root.bean.SMSContentParam;
import com.alcatel.wifilink.root.bean.SMSDeleteParam;
import com.alcatel.wifilink.root.bean.SmsInitState;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.ue.activity.SmsDetailActivity;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.p_xhelper_smart.p_xhelper_smart.bean.DeleteSmsParam;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSMSContentListBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSmsContentListParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.DeleteSMSHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSMSContentListHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSmsInitStateHelper;

import java.util.ArrayList;
import java.util.List;

public class SmsDeleteSessionHelper {

    private List<Long> smsIdsAll;
    private int index = 0;


    public SmsDeleteSessionHelper() {
        index = 0;
        smsIdsAll = new ArrayList<>();
    }

    /**
     * 删除一个会话
     */
    public void deleteOneSessionSms(long contactId) {
        List<Long> smsIds = new ArrayList<>();// 抽取所有需要删除的短信ID
        GetSmsContentListParam getSmsContentListParam = new GetSmsContentListParam();
        getSmsContentListParam.setPage(0);
        getSmsContentListParam.setContactId((int) contactId);
        GetSMSContentListHelper xGetSMSContentListHelper = new GetSMSContentListHelper();
        xGetSMSContentListHelper.setOnGetSmsContentListSuccessListener(bean -> {
            // getInstant all smsids
            for (GetSMSContentListBean.SMSContentBean scb : bean.getSMSContentList()) {
                smsIds.add((long)scb.getSMSId());
            }
            // deleted it
            DeleteSmsParam xDeleteSmsParam = new DeleteSmsParam();
            xDeleteSmsParam.setDelFlag(Cons.DELETE_MORE_SMS);
            xDeleteSmsParam.setSMSArray(smsIds);
            DeleteSMSHelper xDeleteSMSHelper = new DeleteSMSHelper();
            xDeleteSMSHelper.setOnDeleteSmsSuccessListener(this::deletedOneSessionNext);
            xDeleteSMSHelper.deleteSms(xDeleteSmsParam);
        });
        xGetSMSContentListHelper.getSMSContentList(getSmsContentListParam);
    }

    /* -------------------------------------------- method2 -------------------------------------------- */

    /**
     * 删除一个或者多个会话短信
     *
     * @param contactIds
     */
    public void deletedOneOrMoreSessionSms(List<Long> contactIds) {
        index = 0;
        // 获取所有的smsIds
        getAllSmsIds(contactIds);
    }

    /**
     * 获取所有的smsIds
     *
     * @param contactIds
     */
    private void getAllSmsIds(List<Long> contactIds) {
        if (index < contactIds.size()) {/* 未完成收集前 */
            GetSmsContentListParam getSmsContentListParam = new GetSmsContentListParam();
            getSmsContentListParam.setPage(0);
            getSmsContentListParam.setContactId(contactIds.get(index).intValue());
            GetSMSContentListHelper xGetSMSContentListHelper = new GetSMSContentListHelper();
            xGetSMSContentListHelper.setOnGetSmsContentListSuccessListener(bean -> {
                SMSContentList smsContentList = new SMSContentList();
                smsContentList.setPage(bean.getPage());
                smsContentList.setContactId(bean.getContactId());
                smsContentList.setPhoneNumber(bean.getPhoneNumber());
                smsContentList.setTotalPageCount(bean.getTotalPageCount());
                List<SMSContentList.SMSContentBean> tempSMSContentList = new ArrayList<>();
                List<GetSMSContentListBean.SMSContentBean> smsContentBeans= bean.getSMSContentList();
                if(smsContentBeans != null && smsContentBeans.size() > 0){
                    for(GetSMSContentListBean.SMSContentBean smsContentBean : smsContentBeans){
                        SMSContentList.SMSContentBean tempSmsContentBean = new SMSContentList.SMSContentBean();
                        tempSmsContentBean.setReportStatus(smsContentBean.getReportStatus());
                        tempSmsContentBean.setSMSContent(smsContentBean.getSMSContent());
                        tempSmsContentBean.setSMSId(smsContentBean.getSMSId());
                        tempSmsContentBean.setSMSTime(smsContentBean.getSMSTime());
                        tempSmsContentBean.setSMSType(smsContentBean.getSMSType());
                        tempSMSContentList.add(tempSmsContentBean);
                    }
                }
                smsContentList.setSMSContentList(tempSMSContentList);
                smsIdsAll.addAll(OtherUtils.getAllSmsIdByOneSession(smsContentList));
                index++;
                getAllSmsIds(contactIds);
            });
            xGetSMSContentListHelper.setOnGetSmsContentListFailListener(() -> {
                resultErrorNext(null);
                errorNext(null);
                smsIdsAll.clear();
                index = 0;
            });
            xGetSMSContentListHelper.getSMSContentList(getSmsContentListParam);
        } else {/* 已完成收集 */
            getSmsIdsNext(smsIdsAll);
            index = 0;// 恢复导引
            doDelSms(smsIdsAll);// 执行删除
        }
    }

    /**
     * 执行删除
     *
     * @param smsIdsAll
     */
    private void doDelSms(List<Long> smsIdsAll) {
        DeleteSmsParam xDeleteSmsParam = new DeleteSmsParam();
        xDeleteSmsParam.setDelFlag(Cons.DELETE_MORE_SMS);
        xDeleteSmsParam.setSMSArray(smsIdsAll);
        DeleteSMSHelper xDeleteSMSHelper = new DeleteSMSHelper();
        xDeleteSMSHelper.setOnDeleteSmsSuccessListener(this::DelMoreSessionSuccessNext);
        xDeleteSMSHelper.setOnDeleteSmsFailListener(() -> errorNext(null));
        xDeleteSMSHelper.setOnDeleteFailListener(() -> resultErrorNext(null));
        xDeleteSMSHelper.deleteSms(xDeleteSmsParam);
    }

    private OnSMSIdsListener onSMSIdsListener;

    // 接口OnSMSIdsListener
    public interface OnSMSIdsListener {
        void getSmsIds(List<Long> attr);
    }

    // 对外方式setOnSMSIdsListener
    public void setOnSMSIdsListener(OnSMSIdsListener onSMSIdsListener) {
        this.onSMSIdsListener = onSMSIdsListener;
    }

    // 封装方法getSmsIdsNext
    private void getSmsIdsNext(List<Long> attr) {
        if (onSMSIdsListener != null) {
            onSMSIdsListener.getSmsIds(attr);
        }
    }

    /* -------------------------------------------- method1 -------------------------------------------- */

    /**
     * 删除多个会话(暂时不可用,推荐使用:deletedOneOrMoreSessionSms())
     *
     * @param contactIds 联系人ID
     */
    @Deprecated
    public void deleteMoreSessionSms(List<Long> contactIds) {
        // 1.检测初始化状态
        GetSmsInitStateHelper xGetSmsInitStateHelper = new GetSmsInitStateHelper();
        xGetSmsInitStateHelper.setOnGetSmsInitStateSuccessListener(bean -> {
            if (bean.getState() == GetSmsInitStateHelper.SMS_COMPLETE) {
                // 2.完成状态下进行删除操作
                DeleteSmsParam xDeleteSmsParam = new DeleteSmsParam();
                xDeleteSmsParam.setDelFlag(Cons.DELETE_MORE_SMS);
                xDeleteSmsParam.setSMSArray(contactIds);

                DeleteSMSHelper xDeleteSMSHelper = new DeleteSMSHelper();
                // 删除多个会话成功
                xDeleteSMSHelper.setOnDeleteSmsSuccessListener(this::DelMoreSessionSuccessNext);
                xDeleteSMSHelper.setOnDeleteSmsFailListener(() -> errorNext(null));
                xDeleteSMSHelper.setOnDeleteFailListener(() -> resultErrorNext(null));
                xDeleteSMSHelper.deleteSms(xDeleteSmsParam);
            }else{
                deleteMoreSessionSms(contactIds);
            }
        });
        xGetSmsInitStateHelper.setOnGetSmsInitStateFailListener(() -> {
            resultErrorNext(null);
            errorNext(null);
        });
        xGetSmsInitStateHelper.getSmsInitState();
    }

    private OnResultErrorListener onResultErrorListener;

    // 接口OnResultErrorListener
    public interface OnResultErrorListener {
        void resultError(ResponseBody.Error attr);
    }

    // 对外方式setOnResultErrorListener
    public void setOnResultErrorListener(OnResultErrorListener onResultErrorListener) {
        this.onResultErrorListener = onResultErrorListener;
    }

    // 封装方法resultErrorNext
    private void resultErrorNext(ResponseBody.Error attr) {
        if (onResultErrorListener != null) {
            onResultErrorListener.resultError(attr);
        }
    }

    private OnErrorListener onErrorListener;

    // 接口OnErrorListener
    public interface OnErrorListener {
        void error(Throwable attr);
    }

    // 对外方式setOnErrorListener
    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    // 封装方法errorNext
    private void errorNext(Throwable attr) {
        if (onErrorListener != null) {
            onErrorListener.error(attr);
        }
    }

    private OnDeteledMoreSessionSuccessListener onDeteledMoreSessionSuccessListener;

    // 接口OnDeteledMoreSessionSuccessListener
    public interface OnDeteledMoreSessionSuccessListener {
        void DelMoreSessionSuccess(Object attr);
    }

    // 对外方式setOnDeteledMoreSessionSuccessListener
    public void setOnDeteledMoreSessionSuccessListener(OnDeteledMoreSessionSuccessListener onDeteledMoreSessionSuccessListener) {
        this.onDeteledMoreSessionSuccessListener = onDeteledMoreSessionSuccessListener;
    }

    // 封装方法DelMoreSessionSuccessNext
    private void DelMoreSessionSuccessNext(Object attr) {
        if (onDeteledMoreSessionSuccessListener != null) {
            onDeteledMoreSessionSuccessListener.DelMoreSessionSuccess(attr);
        }
    }

    private OnDeletedOneSessionListener onDeletedOneSessionListener;

    // 接口OnDeletedOneSessionListener
    public interface OnDeletedOneSessionListener {
        void deletedOneSession(Object attr);
    }

    // 对外方式setOnDeletedOneSessionListener
    public void setOnDeletedOneSessionListener(OnDeletedOneSessionListener onDeletedOneSessionListener) {
        this.onDeletedOneSessionListener = onDeletedOneSessionListener;
    }

    // 封装方法deletedOneSessionNext
    private void deletedOneSessionNext(Object attr) {
        if (onDeletedOneSessionListener != null) {
            onDeletedOneSessionListener.deletedOneSession(attr);
        }
    }


}
