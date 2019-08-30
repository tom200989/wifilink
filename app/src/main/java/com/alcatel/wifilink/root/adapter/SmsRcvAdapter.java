package com.alcatel.wifilink.root.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.SMSContactBean;
import com.alcatel.wifilink.root.helper.SMSContactSortHelper;
import com.alcatel.wifilink.root.helper.SmsCountHelper;
import com.alcatel.wifilink.root.ue.frag.SmsFrag;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSMSContactListBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSmsContentListParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSMSContentListHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSingleSMSHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
@SuppressLint("UseSparseArrays")
public class SmsRcvAdapter extends RecyclerView.Adapter<SmsHolder> {

    public static final int SELETE_ALL = 1;
    public static final int DESELETE_ALL = -1;
    
    private HashMap<Long, Integer> smsUnreadMap = new HashMap<>();
    private Context context;
    private List<SMSContactBean> smsContactList;
    private OnRcvLongClickListener onRcvLongClickListener;
    private Drawable check_on;
    private Drawable check_off;
    private List<Long> contactIdClickList;

    public SmsRcvAdapter(Context context, List<SMSContactBean> smsContactList) {
        this.context = context;
        this.smsContactList = smsContactList;
        Collections.sort(smsContactList, new SMSContactSortHelper());
        check_on = context.getResources().getDrawable(R.drawable.checkbox_android_on);
        check_off = context.getResources().getDrawable(R.drawable.checkbox_android_off);
        contactIdClickList = new ArrayList<>();
        check_on = context.getResources().getDrawable(R.drawable.checkbox_android_on);
        check_off = context.getResources().getDrawable(R.drawable.checkbox_android_off);
    }

    public void notifys(List<SMSContactBean> smsContactList) {
        this.smsContactList = smsContactList;
        if (contactIdClickList != null & contactIdClickList.size() > 0) {
            contactIdClickList.clear();
        }
        // sort by date
        Collections.sort(this.smsContactList, new SMSContactSortHelper());
        notifyDataSetChanged();
    }

    /**
     * 全选或全不选
     */
    public void selectOrDeSelectAll(boolean isSelectAll) {
        contactIdClickList.clear();// 1.清空
        if (isSelectAll) {
            for (SMSContactBean scf : smsContactList) {
                scf.setState(SELETE_ALL);// 2.修改全选标记位
                contactIdClickList.add(scf.getSmscontact().getContactId());
            }
        } else {
            for (SMSContactBean scf : smsContactList) {
                scf.setState(DESELETE_ALL);
            }
        }
        //Collections.sort(smsContactList, new SMSContactSortHelper());
        //notifys(smsContactList);// 3.刷新
        notifyDataSetChanged();
        selectAllOrNotNext(contactIdClickList);// 4.接口
    }

    @Override
    public SmsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SmsHolder(LayoutInflater.from(context).inflate(R.layout.hh70_item_sms_update, parent, false));
    }

    @Override
    public void onBindViewHolder(SmsHolder holder, int position) {
        // TOAT: 总方法汇聚
        if (smsContactList != null) {
            // set ui
            setSmsPoint(holder, position);// set sms point
            setSmsLongClickPoint(holder, position);// set sms longclick point
            setPhoneNum(holder, position);// set telphone
            setSmsCount(holder, position);// set sms count
            setSmsContent(holder, position);// set sms simple content
            // setSmsSendFailed(holder, position);// set sms send failed logo (keep it)
            setSmsDate(holder, position);// set date
            setSmsClick(holder, position);// to sms detail
            setSmsLongClick(holder, position);// to show delete pop
        }
    }

    @Override
    public int getItemCount() {
        return smsContactList != null ? smsContactList.size() : 0;
    }

    /* **** setSmsPoint **** */
    private void setSmsPoint(SmsHolder holder, int position) {
        GetSMSContactListBean.SMSContacBean smsContact = smsContactList.get(position).getSmscontact();
        int smsType = smsContact.getSMSType();
        int unreadCount = smsContact.getUnreadCount();
        /*  如果检测到未读, 但是未读的数量又为0, 则是FW未做处理, 其实是不存在未读短信, 直接设置为已读 */
        smsType = smsType == GetSMSContactListBean.SMSContacBean.CONS_SMS_TYPE_UNREAD & unreadCount == 0 ? GetSMSContactListBean.SMSContacBean.CONS_SMS_TYPE_READ : smsType;
        // 查看缓冲区是否有当前contactid对应的未读短信数量
        int unreadCache = SmsCountHelper.getUnreadCache(smsContact.getContactId());
        // 以下4种情况均需要显示对应的点
        boolean pointShow = smsType == GetSMSContactListBean.SMSContacBean.CONS_SMS_TYPE_UNREAD //
                                    || smsType == GetSMSContactListBean.SMSContacBean.CONS_SMS_TYPE_SENT_FAIL //
                                    || smsType == GetSMSContactListBean.SMSContacBean.CONS_SMS_TYPE_DRAFT //
                                    || unreadCache > 0;//
        holder.iv_smsPoint.setVisibility(pointShow ? VISIBLE : View.INVISIBLE);
        if (smsType == GetSMSContactListBean.SMSContacBean.CONS_SMS_TYPE_UNREAD) {
            holder.iv_smsPoint.setImageResource(R.drawable.sms_point_unread);
        } else if (smsType == GetSMSContactListBean.SMSContacBean.CONS_SMS_TYPE_DRAFT) {
            holder.iv_smsPoint.setImageResource(R.drawable.sms_edit);
        } else if (smsType == GetSMSContactListBean.SMSContacBean.CONS_SMS_TYPE_SENT_FAIL) {
            holder.iv_smsPoint.setImageResource(R.drawable.sms_prompt);
        } else if (unreadCache > 0) {
            holder.iv_smsPoint.setImageResource(R.drawable.sms_point_unread);
        }
    }

    /* **** setSmsLongClickPoint **** */
    private void setSmsLongClickPoint(SmsHolder holder, int position) {
        // 初始化状态
        holder.iv_smsLongClickPoint.setVisibility(SmsFrag.isLongClick ? VISIBLE : GONE);
        holder.iv_smsLongClickPoint.setImageDrawable(check_off);
        // 判断是否处于全选|全不选状态
        if (smsContactList.get(position).getState() == SELETE_ALL) {
            holder.iv_smsLongClickPoint.setImageDrawable(check_on);
        } else {
            holder.iv_smsLongClickPoint.setImageDrawable(check_off);
        }
    }

    /* **** setPhoneNum **** */
    private void setPhoneNum(SmsHolder holder, int position) {
        GetSMSContactListBean.SMSContacBean smsContact = smsContactList.get(position).getSmscontact();
        List<String> phoneNumber = smsContact.getPhoneNumber();
        String phone = RootUtils.stitchPhone(context, phoneNumber);
        holder.tv_smsPhone.setText(phone);
    }

    /* **** setSmsCount **** */
    private void setSmsCount(SmsHolder holder, int position) {
        GetSMSContactListBean.SMSContacBean smsContact = smsContactList.get(position).getSmscontact();
        holder.tv_smsCount.setText(String.valueOf(smsContact.getTSMSCount()));
    }

    /* **** setSmsContent **** */
    private void setSmsContent(SmsHolder holder, int position) {
        GetSMSContactListBean.SMSContacBean smsContact = smsContactList.get(position).getSmscontact();
        holder.tv_smsContent.setText(smsContact.getSMSContent());
    }

    /* **** setSmsDate **** */
    private void setSmsDate(SmsHolder holder, int position) {
        GetSMSContactListBean.SMSContacBean smsContact = smsContactList.get(position).getSmscontact();
        String date = RootUtils.transferDate(smsContact.getSMSTime());
        holder.tv_smsDate.setText(date);
    }

    /* **** setSmsClick **** */
    private void setSmsClick(SmsHolder holder, int position) {

        // 1.切换
        GetSMSContactListBean.SMSContacBean smsContact = smsContactList.get(position).getSmscontact();
        holder.rl_sms.setOnClickListener(v -> {
            // 2.把所有的全选标记位复位为CLICK
            for (SMSContactBean scf : smsContactList) {
                scf.setState(SMSContactBean.CLICK);
            }
            if (!SmsFrag.isLongClick) {/* 普通模式下 */
                smsNormalClickNext(smsContact);
                // 1.1.设置为已读
                setReaded(smsContact);
            } else {/* 长按模式下 */
                // 1.1.切换UI
                holder.iv_smsLongClickPoint.setImageDrawable(holder.iv_smsLongClickPoint.getDrawable() == check_on ? check_off : check_on);
                // 1.2.增删集合中元素
                if (holder.iv_smsLongClickPoint.getDrawable() == check_on) {
                    contactIdClickList.add(smsContact.getContactId());
                } else {
                    contactIdClickList.remove(contactIdClickList.indexOf(smsContact.getContactId()));
                }
                smsWhenlongclickAfterNext(smsContact, contactIdClickList);
            }

        });
    }

    /* **** setSmsLongClick **** */
    private void setSmsLongClick(SmsHolder holder, int position) {
        holder.rl_sms.setOnLongClickListener(v -> {
            if (onRcvLongClickListener != null) {
                onRcvLongClickListener.getPosition(position);
            }
            return true;
        });
    }

    /* **** setSmsSendFailed **** */
    private void setSmsSendFailed(SmsHolder holder, int position) {
        GetSMSContactListBean.SMSContacBean smsContact = smsContactList.get(position).getSmscontact();
        holder.iv_smsSendFailed.setVisibility(smsContact.getSMSType() == GetSMSContactListBean.SMSContacBean.CONS_SMS_TYPE_SENT_FAIL ? VISIBLE : GONE);
    }


    /* 调用此方法, 路由器自动设置为已读 */
    private void setReaded(GetSMSContactListBean.SMSContacBean smsContact) {
        // 清空缓冲区短信未读数量
        smsUnreadMap.put(smsContact.getContactId(), 0);

        // 调用此接口的目的是为了告知路由器该ID下的短信已读

        //用smart框架内部param代替旧的Param
        GetSmsContentListParam getSmsContentListParam = new GetSmsContentListParam();
        getSmsContentListParam.setPage(0);
        getSmsContentListParam.setContactId((int) smsContact.getContactId());
        GetSMSContentListHelper xGetSMSContentListHelper = new GetSMSContentListHelper();
        xGetSMSContentListHelper.setOnGetSmsContentListSuccessListener(bean -> {

        });
        xGetSMSContentListHelper.getSMSContentList(getSmsContentListParam);

        // 触发路由器 -- 使得短信修改成［已读］状态
        GetSingleSMSHelper xGetSingleSMSHelper = new GetSingleSMSHelper();
        xGetSingleSMSHelper.getSingleSMS(smsContact.getSMSId());
    }

    private OnSelectAllOrNotListener onSelectAllOrNotListener;

    // 接口OnSelectAllOrNotListener
    public interface OnSelectAllOrNotListener {
        void selectAllOrNot(List<Long> attr);
    }

    // 对外方式setOnSelectAllOrNotListener
    public void setOnSelectAllOrNotListener(OnSelectAllOrNotListener onSelectAllOrNotListener) {
        this.onSelectAllOrNotListener = onSelectAllOrNotListener;
    }

    // 封装方法selectAllOrNotNext
    private void selectAllOrNotNext(List<Long> attr) {
        if (onSelectAllOrNotListener != null) {
            onSelectAllOrNotListener.selectAllOrNot(attr);
        }
    }

    private OnSMSWhenLongClickAfterListener onSMSWhenLongClickAfterListener;

    // 接口OnSMSWhenLongClickAfterListener
    public interface OnSMSWhenLongClickAfterListener {
        void smsWhenlongclickAfter(GetSMSContactListBean.SMSContacBean attr, List<Long> contactIdList);
    }

    // 对外方式setOnSMSWhenLongClickAfterListener
    public void setOnSMSWhenLongClickAfterListener(OnSMSWhenLongClickAfterListener onSMSWhenLongClickAfterListener) {
        this.onSMSWhenLongClickAfterListener = onSMSWhenLongClickAfterListener;
    }

    // 封装方法smsWhenlongclickAfterNext
    private void smsWhenlongclickAfterNext(GetSMSContactListBean.SMSContacBean attr, List<Long> contactIdList) {
        if (onSMSWhenLongClickAfterListener != null) {
            onSMSWhenLongClickAfterListener.smsWhenlongclickAfter(attr, contactIdList);
        }
    }

    private OnSMSNormalClickListener onSMSNormalClickListener;

    // 接口OnSMSNormalClickListener
    public interface OnSMSNormalClickListener {
        void smsNormalClick(GetSMSContactListBean.SMSContacBean attr);
    }

    // 对外方式setOnSMSNormalClickListener
    public void setOnSMSNormalClickListener(OnSMSNormalClickListener onSMSNormalClickListener) {
        this.onSMSNormalClickListener = onSMSNormalClickListener;
    }

    // 封装方法smsNormalClickNext
    private void smsNormalClickNext(GetSMSContactListBean.SMSContacBean attr) {
        if (onSMSNormalClickListener != null) {
            onSMSNormalClickListener.smsNormalClick(attr);
        }
    }

    public interface OnRcvLongClickListener {
        void getPosition(int position);
    }

    public void setOnRcvLongClickListener(OnRcvLongClickListener onRcvLongClickListener) {
        this.onRcvLongClickListener = onRcvLongClickListener;
    }

}
