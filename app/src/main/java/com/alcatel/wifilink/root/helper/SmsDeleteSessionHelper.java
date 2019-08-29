package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.bean.SMSContentListBean;
import com.alcatel.wifilink.root.utils.RootUtils;
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
                SMSContentListBean smsContentListBean = new SMSContentListBean();
                smsContentListBean.setPage(bean.getPage());
                smsContentListBean.setContactId(bean.getContactId());
                smsContentListBean.setPhoneNumber(bean.getPhoneNumber());
                smsContentListBean.setTotalPageCount(bean.getTotalPageCount());
                List<SMSContentListBean.SMSContentBean> tempSMSContentList = new ArrayList<>();
                List<GetSMSContentListBean.SMSContentBean> smsContentBeans= bean.getSMSContentList();
                if(smsContentBeans != null && smsContentBeans.size() > 0){
                    for(GetSMSContentListBean.SMSContentBean smsContentBean : smsContentBeans){
                        SMSContentListBean.SMSContentBean tempSmsContentBean = new SMSContentListBean.SMSContentBean();
                        tempSmsContentBean.setReportStatus(smsContentBean.getReportStatus());
                        tempSmsContentBean.setSMSContent(smsContentBean.getSMSContent());
                        tempSmsContentBean.setSMSId(smsContentBean.getSMSId());
                        tempSmsContentBean.setSMSTime(smsContentBean.getSMSTime());
                        tempSmsContentBean.setSMSType(smsContentBean.getSMSType());
                        tempSMSContentList.add(tempSmsContentBean);
                    }
                }
                smsContentListBean.setSMSContentList(tempSMSContentList);
                smsIdsAll.addAll(RootUtils.getAllSmsIdByOneSession(smsContentListBean));
                index++;
                getAllSmsIds(contactIds);
            });
            xGetSMSContentListHelper.setOnGetSmsContentListFailListener(() -> {
                appErrorNext();
                fwErrorNext();
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
        xDeleteSmsParam.setDelFlag(DeleteSmsParam.CONS_DELETE_MORE);
        xDeleteSmsParam.setSMSArray(smsIdsAll);
        DeleteSMSHelper xDeleteSMSHelper = new DeleteSMSHelper();
        xDeleteSMSHelper.setOnDeleteSmsSuccessListener(this::DelMoreSessionSuccessNext);
        xDeleteSMSHelper.setOnDeleteSmsFailListener(this::appErrorNext);
        xDeleteSMSHelper.setOnDeleteFailListener(this::fwErrorNext);
        xDeleteSMSHelper.deleteSms(xDeleteSmsParam);
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
                xDeleteSmsParam.setDelFlag(DeleteSmsParam.CONS_DELETE_MORE);
                xDeleteSmsParam.setSMSArray(contactIds);

                DeleteSMSHelper xDeleteSMSHelper = new DeleteSMSHelper();
                // 删除多个会话成功
                xDeleteSMSHelper.setOnDeleteSmsSuccessListener(this::DelMoreSessionSuccessNext);
                xDeleteSMSHelper.setOnDeleteSmsFailListener(this::appErrorNext);
                xDeleteSMSHelper.setOnDeleteFailListener(this::fwErrorNext);
                xDeleteSMSHelper.deleteSms(xDeleteSmsParam);
            }else{
                deleteMoreSessionSms(contactIds);
            }
        });
        xGetSmsInitStateHelper.setOnGetSmsInitStateFailListener(() -> {
            appErrorNext();
            fwErrorNext();
        });
        xGetSmsInitStateHelper.getSmsInitState();
    }

    /* ************************************* SMSIds ************************************ */

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

    /* ************************************* DeletedMoreSession Success ************************************ */

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

    /* ************************************* DeletedOneSession Success ************************************ */

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

    /* ************************************* AppError ************************************ */

    private OnAppErrorListener onAppErrorListener;

    // Inteerface--> 接口OnAppErrorListener
    public interface OnAppErrorListener {
        void appError();
    }

    // 对外方式setOnAppErrorListener
    public void setOnAppErrorListener(OnAppErrorListener onAppErrorListener) {
        this.onAppErrorListener = onAppErrorListener;
    }

    // 封装方法appErrorNext
    private void appErrorNext() {
        if (onAppErrorListener != null) {
            onAppErrorListener.appError();
        }
    }

    /* ************************************* FwError ************************************ */

    private OnFwErrorListener onFwErrorListener;

    // Inteerface--> 接口OnFwErrorListener
    public interface OnFwErrorListener {
        void fwError();
    }

    // 对外方式setOnFwErrorListener
    public void setOnFwErrorListener(OnFwErrorListener onFwErrorListener) {
        this.onFwErrorListener = onFwErrorListener;
    }

    // 封装方法fwErrorNext
    private void fwErrorNext() {
        if (onFwErrorListener != null) {
            onFwErrorListener.fwError();
        }
    }
}
