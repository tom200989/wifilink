package com.alcatel.wifilink.root.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.SMSContentListBean;
import com.alcatel.wifilink.root.helper.SmsContentSortHelper;
import com.alcatel.wifilink.root.helper.SmsReSendHelper;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSMSContactListBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.alcatel.wifilink.R.drawable.checkbox_android_off;
import static com.alcatel.wifilink.R.drawable.checkbox_android_on;

public class SmsDetatilAdapter extends RecyclerView.Adapter<SmsDetailHolder> {

    private Context context;
    private LinearLayoutManager lm;
    private SMSContentListBean smsContentListBean;
    private List<String> phoneNums;
    public boolean isLongClick;// 是否被长按

    private List<SMSContentListBean.SMSContentBean> sortScbList;// 分类集合(去除草稿)
    private List<NewSMSContentBean> newScbList;// 带有标记位集合
    private List<Long> smsIds;// 选中的smsId

    private OnSmsSelectedListener onSmsSelectedListener;
    private OnSmsLongClickListener onSmsLongClickListener;
    private OnSendSuccessListener onSendSuccessListener;

    public SmsDetatilAdapter(Context context, LinearLayoutManager lm, SMSContentListBean smsContentListBean, List<String> phoneNums) {
        this.context = context;
        this.lm = lm;
        this.smsContentListBean = smsContentListBean;
        this.phoneNums = phoneNums;
        sortScbList = new ArrayList<>();
        newScbList = new ArrayList<>();
        smsIds = new ArrayList<>();
        refreshAll();// refresh all info
    }


    /**
     * @param smsContentListBean 信息列表
     * @param toLast         是否需要recycle定位到最后？
     */
    public void notifys(SMSContentListBean smsContentListBean, boolean toLast) {
        clearAll();// 1. clear all first
        this.smsContentListBean = smsContentListBean;// 2.deliver data
        refreshAll();// 3.operate all data ui
        notifyDataSetChanged();// 4.refresh ui
        if (toLast) {
            lm.scrollToPositionWithOffset(getItemCount() - 1, 0);
        }
    }

    @Override
    public int getItemCount() {
        return newScbList.size();
    }

    @Override
    public SmsDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SmsDetailHolder(LayoutInflater.from(context).inflate(R.layout.hh70_item_smsdetail_update, parent, false));
    }

    // TOAT: 主要方法
    @Override
    public void onBindViewHolder(SmsDetailHolder holder, int position) {
        if (newScbList.size() > 0) {
            // nscb = newScbList.getInstant(position);// 带选中|未选中标记位
            // scb = nscb.smsContentBean;// 不带标记位

            setSelectLogo(holder, position);// set selected or not logo

            setReceiver(holder, position);// set recevier layout
            setReceiverText(holder, position);// set receiver text
            setReceiverDate(holder, position);// set receiver date

            setSend(holder, position);// set send layout
            setSendFailLogo(holder, position);// set send failed logo
            setSendText(holder, position);// set send text
            setSendDate(holder, position);// set send date

            setLongClick(holder, position);// set layout long click
            setItemClick(holder, position);// set sms item click
            setFailedClick(holder, position);// set send failed logo click
        }

    }


    /* **** setSelectLogo **** */
    private void setSelectLogo(SmsDetailHolder holder, int position) {
        NewSMSContentBean nscb = newScbList.get(position);// 带选中|未选中标记位
        SMSContentListBean.SMSContentBean scb = nscb.smsContentBean;
        holder.iv_smsdetail_selected.setVisibility(isLongClick ? VISIBLE : GONE);
        int selecedUi = nscb.isSelected ? checkbox_android_on : checkbox_android_off;
        holder.iv_smsdetail_selected.setImageResource(selecedUi);
    }

    /* **** setReceiver **** */
    private void setReceiver(SmsDetailHolder holder, int position) {
        NewSMSContentBean nscb = newScbList.get(position);// 带选中|未选中标记位
        SMSContentListBean.SMSContentBean scb = nscb.smsContentBean;
        int smsType = scb.getSMSType();
        boolean receiver = smsType == GetSMSContactListBean.SMSContacBean.CONS_SMS_TYPE_READ // 
                                   || smsType == GetSMSContactListBean.SMSContacBean.CONS_SMS_TYPE_UNREAD;//
        holder.rl_smsdetail_receiver.setVisibility(receiver ? VISIBLE : INVISIBLE);
    }

    /* **** setSend **** */
    private void setSend(SmsDetailHolder holder, int position) {
        NewSMSContentBean nscb = newScbList.get(position);// 带选中|未选中标记位
        SMSContentListBean.SMSContentBean scb = nscb.smsContentBean;
        int smsType = scb.getSMSType();
        boolean receiver = smsType == GetSMSContactListBean.SMSContacBean.CONS_SMS_TYPE_SENT || smsType == GetSMSContactListBean.SMSContacBean.CONS_SMS_TYPE_SENT_FAIL;
        holder.rl_smsdetail_send.setVisibility(receiver ? VISIBLE : INVISIBLE);
    }

    /* **** setReceiverText **** */
    private void setReceiverText(SmsDetailHolder holder, int position) {
        NewSMSContentBean nscb = newScbList.get(position);// 带选中|未选中标记位
        SMSContentListBean.SMSContentBean scb = nscb.smsContentBean;
        holder.tv_smsdetail_text_receiver.setText(scb.getSMSContent());
    }

    /* **** setReceiverDate **** */
    private void setReceiverDate(SmsDetailHolder holder, int position) {
        NewSMSContentBean nscb = newScbList.get(position);// 带选中|未选中标记位
        SMSContentListBean.SMSContentBean scb = nscb.smsContentBean;
        String date = RootUtils.transferDate(scb.getSMSTime());
        holder.tv_smsdetail_date_receiver.setText(date);
    }

    /* **** setSendFailLogo **** */
    private void setSendFailLogo(SmsDetailHolder holder, int position) {
        NewSMSContentBean nscb = newScbList.get(position);// 带选中|未选中标记位
        SMSContentListBean.SMSContentBean scb = nscb.smsContentBean;
        holder.iv_smsdetail_failed_send.setVisibility(scb.getSMSType() == GetSMSContactListBean.SMSContacBean.CONS_SMS_TYPE_SENT_FAIL ? VISIBLE : GONE);
    }

    /* **** setSendText **** */
    private void setSendText(SmsDetailHolder holder, int position) {
        NewSMSContentBean nscb = newScbList.get(position);// 带选中|未选中标记位
        SMSContentListBean.SMSContentBean scb = nscb.smsContentBean;
        holder.tv_smsdetail_text_send.setText(scb.getSMSContent());
    }

    /* **** setSendDate **** */
    private void setSendDate(SmsDetailHolder holder, int position) {
        NewSMSContentBean nscb = newScbList.get(position);// 带选中|未选中标记位
        SMSContentListBean.SMSContentBean scb = nscb.smsContentBean;
        String date = RootUtils.transferDate(scb.getSMSTime());
        holder.tv_smsdetail_date_send.setText(date);
    }

    /* **** setLongClick **** */
    private void setLongClick(SmsDetailHolder holder, int position) {
        NewSMSContentBean nscb = newScbList.get(position);// 带选中|未选中标记位
        SMSContentListBean.SMSContentBean scb = nscb.smsContentBean;
        holder.rl_smsdetail.setOnLongClickListener(v -> {
            if (onSmsLongClickListener != null) {
                onSmsLongClickListener.smsLongClick();
            }
            isLongClick = true;
            resetSelected();
            notifyDataSetChanged();
            return true;
        });
    }

    /* **** setItemClick **** */
    private void setItemClick(SmsDetailHolder holder, int position) {
        holder.rl_smsdetail.setOnClickListener(v -> {
            if (isLongClick) {
                NewSMSContentBean nscb = newScbList.get(position);
                nscb.isSelected = !nscb.isSelected;
                long smsId = nscb.smsContentBean.getSMSId();
                if (nscb.isSelected) {// if check then add id list
                    if (!smsIds.contains(smsId)) {
                        smsIds.add(smsId);
                    }
                } else {
                    if (smsIds.contains(smsId)) {
                        smsIds.remove(smsIds.indexOf(smsId));
                    }
                }
                // deliver the list outside
                if (onSmsSelectedListener != null) {
                    onSmsSelectedListener.selected(smsIds);
                }
                // refresh adapter
                notifyItemChanged(position);
            }
        });
    }

    /* **** setFailedClick **** */
    private void setFailedClick(SmsDetailHolder holder, int position) {
        NewSMSContentBean nscb = newScbList.get(position);// 带选中|未选中标记位
        SMSContentListBean.SMSContentBean scb = nscb.smsContentBean;
        holder.iv_smsdetail_failed_send.setOnClickListener(v -> {
            tryAgainNext(scb);
        });
    }


    /* -------------------------------------------- HELPER -------------------------------------------- */

    /* **** A.重新整理短信对象 **** */
    private void refreshAll() {
        filterSms();// 1.过滤草稿短信
        sortSmsByDate();// 2.根据日期排序
        hiberNewbean();// 3.重新封装出新的内容对象(带选中或未选中标记)
    }

    /* A1-过滤掉草稿短信 */
    private void filterSms() {
        if (smsContentListBean != null) {
            for (SMSContentListBean.SMSContentBean scb : smsContentListBean.getSMSContentList()) {
                int smsType = scb.getSMSType();
                if (smsType == GetSMSContactListBean.SMSContacBean.CONS_SMS_TYPE_DRAFT || smsType == GetSMSContactListBean.SMSContacBean.CONS_SMS_TYPE_REPORT) {
                    continue;
                }
                sortScbList.add(scb);
            }
        }
    }

    /* A2-根据日期排序 */
    private void sortSmsByDate() {
        Collections.sort(sortScbList, new SmsContentSortHelper());
    }

    /* A3-重新封装出新的内容对象 */
    private void hiberNewbean() {
        for (SMSContentListBean.SMSContentBean scb : sortScbList) {
            NewSMSContentBean nscb = new NewSMSContentBean();
            nscb.isSelected = false;
            nscb.smsContentBean = scb;
            newScbList.add(nscb);
        }
    }

    /* **** 带有选中|未选中标记的内容对象 **** */
    public class NewSMSContentBean {
        public SMSContentListBean.SMSContentBean smsContentBean;
        public boolean isSelected;
    }

    /* 重新设置被选中标记位为false */
    private void resetSelected() {
        for (NewSMSContentBean nscb : newScbList) {
            nscb.isSelected = false;
        }
    }

    /* 更新前先清空集合 */
    private void clearAll() {
        sortScbList.clear();
        newScbList.clear();
        smsIds.clear();
    }

    /* 点击确定后重新发送 */
    public void sendAgain(SMSContentListBean.SMSContentBean scb) {
        new SmsReSendHelper(scb, phoneNums) {
            @Override
            public void success() {
                if (onSendSuccessListener != null) {
                    onSendSuccessListener.sendAgainSuccess();
                }
            }

            @Override
            public void failed() {

            }
        };
    }

    /* -------------------------------------------- IMPL -------------------------------------------- */
    // sms item have selected
    public interface OnSmsSelectedListener {
        void selected(List<Long> smsIds);
    }

    public void setOnSmsSelectedListener(OnSmsSelectedListener onSmsSelectedListener) {
        this.onSmsSelectedListener = onSmsSelectedListener;
    }

    // sms item long click
    public interface OnSmsLongClickListener {
        void smsLongClick();
    }

    public void setOnSmsLongClickListener(OnSmsLongClickListener onSmsLongClickListener) {
        this.onSmsLongClickListener = onSmsLongClickListener;
    }

    public interface OnSendSuccessListener {
        void sendAgainSuccess();
    }

    public void setOnSendSuccessListener(OnSendSuccessListener onSendSuccessListener) {
        this.onSendSuccessListener = onSendSuccessListener;
    }

    public interface OnShowTrayAgainListener {
        void tryAgain(SMSContentListBean.SMSContentBean bean);
    }

    private OnShowTrayAgainListener onShowTrayAgainListener;

    //对外方式setOnShowTrayAgainListener
    public void setOnShowTrayAgainListener(OnShowTrayAgainListener onShowTrayAgainListener) {
        this.onShowTrayAgainListener = onShowTrayAgainListener;
    }

    //封装方法
    private void tryAgainNext(SMSContentListBean.SMSContentBean bean) {
        if (onShowTrayAgainListener != null) {
            onShowTrayAgainListener.tryAgain(bean);
        }
    }

}
