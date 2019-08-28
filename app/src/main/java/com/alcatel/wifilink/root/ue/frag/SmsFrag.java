package com.alcatel.wifilink.root.ue.frag;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.adapter.SmsRcvAdapter;
import com.alcatel.wifilink.root.bean.SMSContactList;
import com.alcatel.wifilink.root.bean.SMSContactSelf;
import com.alcatel.wifilink.root.helper.ClickDoubleHelper;
import com.alcatel.wifilink.root.helper.SmsDeleteSessionHelper;
import com.alcatel.wifilink.root.ue.activity.SplashActivity;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.alcatel.wifilink.root.widget.HH70_SmsDeleteWidget;
import com.hiber.cons.TimerState;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSMSContactListBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSMSContactListHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.LogoutHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/19 0019.
 */
public class SmsFrag extends BaseFrag {

    @BindView(R.id.rcv_sms)
    RecyclerView rcvSms;
    @BindView(R.id.no_sms)
    TextView noSms;
    @BindView(R.id.tv_sms_batchDeteled)
    TextView tvSmsBatchDeteled;
    @BindView(R.id.ll_sms_batchDeteled)
    LinearLayout llSmsBatchDeteled;
    @BindView(R.id.tv_sms_batchSelectAll)
    TextView tvSmsBatchSelectAll;
    @BindView(R.id.iv_homeRx_smsNew)
    ImageView ivSmsNew;
    @BindView(R.id.wd_sms_delete)
    HH70_SmsDeleteWidget smsDeleteWidget;

    private SmsRcvAdapter smsRcvAdapter;
    private SMSContactList smsContactList;// 不带有长按标记的联系人集合
    private List<SMSContactSelf> otherSmsContactSelfList = new ArrayList<>();// 带有长按标记的联系人集合
    private List<Long> contactIdList;// 长按模式下存放被点击的短信条目的联系人ID
    public static boolean isLongClick = false;
    private String select_all;
    private String deselect_all;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_sms;
    }

    @Override
    public void initViewFinish(View inflateView) {
        super.initViewFinish(inflateView);
        initRes();
        initView();
        initEvent();
        initClick();
        timerState = TimerState.ON;
        timer_period = 5000;
        isLongClick = false;
    }

    private void initRes() {
        select_all = getString(R.string.operation_selectall);
        deselect_all = getString(R.string.operation_cancel_selectall);
    }

    @Override
    public void setTimerTask() {
        super.setTimerTask();
        if (!isLongClick) {
            getSmsContactList();
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        rcvSms.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        smsRcvAdapter = new SmsRcvAdapter(activity, otherSmsContactSelfList);
        rcvSms.setAdapter(smsRcvAdapter);
    }

    /**
     * 初始化适配器事件
     */
    private void initEvent() {

        /* 长按短信列表item */
        smsRcvAdapter.setOnRcvLongClickListener(position -> {
            if (!isLongClick) {
                isLongClick = true;// 进入允许删除状态
                tvSmsBatchSelectAll.setText(select_all);// 初始文本为selected all
                ivSmsNew.setVisibility(View.GONE);// 短信新建暂时不可用
                otherSmsContactSelfList = RootUtils.modifySMSContactSelf(otherSmsContactSelfList, true);
                smsRcvAdapter.notifys(otherSmsContactSelfList);
                llSmsBatchDeteled.setVisibility(isLongClick ? View.VISIBLE : View.GONE);
            }
        });

        /* 长按模式下点击单条短信 */
        smsRcvAdapter.setOnSMSWhenLongClickAfterListener((attr, contactIdList) -> {
            SmsFrag.this.contactIdList = contactIdList;
            if (contactIdList.size() == 0) {
                tvSmsBatchSelectAll.setText(select_all);
            }
        });
        smsRcvAdapter.setOnSMSNormalClickListener(smsContact -> {
            // 1.2.跳转
            sendEvent(smsContact, true);
            toFrag(getClass(), SmsDetailFrag.class, null, true);
        });
        /* 全选|全不选 */
        smsRcvAdapter.setOnSelectAllOrNotListener(contactIdList -> {
            SmsFrag.this.contactIdList = contactIdList;
        });
    }

    /**
     * 初始化点击事件
     */
    public void initClick() {
        tvSmsBatchSelectAll.setOnClickListener(v -> {// 全选按钮
            selectedAll();
        });
        tvSmsBatchDeteled.setOnClickListener(v -> {// 删除按钮
            if (contactIdList != null && contactIdList.size() > 0) {
                // 显示弹窗
                showDelPop();
            }
        });
        ivSmsNew.setOnClickListener(v -> {
            toFrag(getClass(), SmsNewFrag.class, null, false);
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            isLongClick = false;
        }
        // 删除按钮的显示与否
        llSmsBatchDeteled.setVisibility(isLongClick ? View.VISIBLE : View.GONE);
    }

    /**
     * 选中全部短信
     */
    private void selectedAll() {
        if (isLongClick) {
            // 修改全选或者全不选
            smsRcvAdapter.selectOrDeSelectAll(tvSmsBatchSelectAll.getText() == select_all ? true : false);
            tvSmsBatchSelectAll.setText(tvSmsBatchSelectAll.getText() == select_all ? deselect_all : select_all);
        }
    }

    /**
     * 显示删除弹窗
     */
    private void showDelPop() {
        smsDeleteWidget.setOnConfirmClickListener(() -> {
            deleteSessionSms(contactIdList);
            smsDeleteWidget.setVisibility(View.GONE);
        });
        smsDeleteWidget.setOnCancelClickListener(() -> smsDeleteWidget.setVisibility(View.GONE));
        smsDeleteWidget.setVisibility(View.VISIBLE);
    }

    /* -------------------------------------------- HELPER -------------------------------------------- */

    /**
     * 获取会话列表
     */
    private void getSmsContactList() {
        // get sms list
        GetSMSContactListHelper xGetSMSContactListHelper = new GetSMSContactListHelper();
        xGetSMSContactListHelper.setOnGetSmsContactListSuccessListener(bean -> {
            SMSContactList tempSmsContactList = new SMSContactList();
            tempSmsContactList.setPage(bean.getPage());
            tempSmsContactList.setTotalPageCount(bean.getTotalPageCount());

            List<SMSContactList.SMSContact> tempSmsContact = new ArrayList<>();

            List<GetSMSContactListBean.SMSContacBean> smsContacBeans = bean.getSMSContactList();
            if (smsContacBeans != null && smsContacBeans.size() > 0) {
                for (GetSMSContactListBean.SMSContacBean smsContacBean : smsContacBeans) {
                    SMSContactList.SMSContact smsContact = new SMSContactList.SMSContact();
                    smsContact.setContactId(smsContacBean.getContactId());
                    smsContact.setPhoneNumber(smsContacBean.getPhoneNumber());
                    smsContact.setReportStatus(smsContacBean.getReportStatus());
                    smsContact.setSMSContent(smsContacBean.getSMSContent());
                    smsContact.setSMSId(smsContacBean.getSMSId());
                    smsContact.setSMSTime(smsContacBean.getSMSTime());
                    smsContact.setSMSType(smsContacBean.getSMSType());
                    smsContact.setTSMSCount(smsContacBean.getTSMSCount());
                    smsContact.setUnreadCount(smsContacBean.getUnreadCount());
                    tempSmsContact.add(smsContact);
                }
            }
            tempSmsContactList.setSMSContactList(tempSmsContact);
            smsContactList = tempSmsContactList;
            otherSmsContactSelfList = RootUtils.getSMSSelfList(smsContactList);
            smsRcvAdapter.notifys(otherSmsContactSelfList);
            noSms.setVisibility(smsContactList.getSMSContactList().size() > 0 ? View.GONE : View.VISIBLE);
        });
        xGetSMSContactListHelper.getSMSContactList(0);
    }

    /**
     * 删除会话短信
     *
     * @param contactIds
     */
    private void deleteSessionSms(List<Long> contactIds) {
        SmsDeleteSessionHelper sdsh = new SmsDeleteSessionHelper();
        sdsh.setOnResultErrorListener(attr -> delFailedReset());
        sdsh.setOnErrorListener(attr -> delSuccessReset());
        sdsh.setOnDeteledMoreSessionSuccessListener(attr -> delSuccessReset());
        sdsh.deletedOneOrMoreSessionSms(contactIds);
    }

    /**
     * 删除成功后恢复UI
     */
    private void delSuccessReset() {
        isLongClick = false;// 恢复标记位
        ivSmsNew.setVisibility(View.VISIBLE);
        llSmsBatchDeteled.setVisibility(isLongClick ? View.VISIBLE : View.GONE);
        toast(R.string.sms_delete_success, 2000);
    }

    /**
     * 删除失败后恢复UI
     */
    private void delFailedReset() {
        isLongClick = false;// 恢复标记位
        if (activity != null) {
            ivSmsNew.setVisibility(View.VISIBLE);
            if (llSmsBatchDeteled != null) {
                llSmsBatchDeteled.setVisibility(isLongClick ? View.VISIBLE : View.GONE);
            }
            toast(R.string.sms_delete_error, 2000);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (isLongClick) {// 如果处于长按状态下
            isLongClick = false;
            ivSmsNew.setVisibility(View.VISIBLE);
            llSmsBatchDeteled.setVisibility(isLongClick ? View.VISIBLE : View.GONE);
            return isLongClick;
        } else if (smsDeleteWidget.getVisibility() == View.VISIBLE) {
            smsDeleteWidget.setVisibility(View.GONE);
            return true;
        } else {
            // 登出
            ClickDoubleHelper clickDouble = new ClickDoubleHelper();
            clickDouble.setOnClickOneListener(() -> toast(R.string.home_exit_app, 3000));
            clickDouble.setOnClickDoubleListener(this::logOut);
            clickDouble.click();
            return true;
        }
    }

    /**
     * 登出
     */
    private void logOut() {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGIN) {
                LogoutHelper xLogoutHelper = new LogoutHelper();
                xLogoutHelper.setOnLogoutSuccessListener(() -> {
                    toast(R.string.login_logout_successful, 3000);
                    toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true);
                });
                xLogoutHelper.setOnLogOutFailedListener(() -> toast(R.string.login_logout_failed, 3000));
                xLogoutHelper.logout();
            } else {
                toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true);
            }
        });
        xGetLoginStateHelper.getLoginState();
    }
}
