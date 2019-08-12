package com.alcatel.wifilink.root.ue.frag;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.widget.PopupWindows;
import com.alcatel.wifilink.root.bean.SMSContactList;
import com.alcatel.wifilink.root.bean.Other_SMSContactSelf;
import com.alcatel.wifilink.root.ue.activity.HomeRxActivity;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.alcatel.wifilink.root.adapter.SmsRcvAdapter;
import com.alcatel.wifilink.root.helper.SmsDeleteSessionHelper;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.ScreenSize;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.alcatel.wifilink.root.utils.fraghandler.FragmentBackHandler;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSMSContactListBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSMSContactListHelper;;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

@SuppressLint("ValidFragment")
public class SmsFragments extends Fragment implements FragmentBackHandler {

    @BindView(R.id.rcv_sms)
    RecyclerView rcvSms;
    Unbinder unbinder;
    @BindView(R.id.no_sms)
    TextView noSms;
    @BindView(R.id.tv_sms_batchDeteled)
    TextView tvSmsBatchDeteled;
    @BindView(R.id.ll_sms_batchDeteled)
    LinearLayout llSmsBatchDeteled;
    @BindView(R.id.tv_sms_batchSelectAll)
    TextView tvSmsBatchSelectAll;

    private View inflate;
    private HomeRxActivity activity;
    private TimerHelper timerHelper;
    private SmsRcvAdapter smsRcvAdapter;
    private SMSContactList smsContactList;// 不带有长按标记的联系人集合
    private List<Other_SMSContactSelf> otherSmsContactSelfList = new ArrayList<>();// 带有长按标记的联系人集合
    private List<Long> contactIdList;// 长按模式下存放被点击的短信条目的联系人ID
    private PopupWindows pop_deleted_sms;
    public static boolean isLongClick = false;
    private String select_all;
    private String deselect_all;

    public SmsFragments() {

    }

    public SmsFragments(Activity activity) {
        this.activity = (HomeRxActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = View.inflate(getActivity(), R.layout.fragment_sms_update, null);
        unbinder = ButterKnife.bind(this, inflate);
        initRes();
        isLongClick = false;
        resetUi();
        initView();
        initEvent();
        return inflate;
    }

    private void initRes() {
        select_all = getString(R.string.operation_selectall);
        deselect_all = getString(R.string.operation_cancel_selectall);
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimer();
    }

    private void startTimer() {
        timerHelper = new TimerHelper(getActivity()) {
            @Override
            public void doSomething() {
                getSmsContactList();
            }
        };
        timerHelper.start(5000);
        OtherUtils.timerList.add(timerHelper);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            isLongClick = false;
            resetUi();
            startTimer();
        } else {
            stopTimer();
        }
        // 删除按钮的显示与否
        llSmsBatchDeteled.setVisibility(isLongClick ? View.VISIBLE : View.GONE);

    }

    /**
     * 重置banner & navigation
     */
    private void resetUi() {
        if (activity == null) {
            activity = (HomeRxActivity) getActivity();
        }
        activity.tabFlag = Cons.TAB_SMS;
        activity.llNavigation.setVisibility(View.VISIBLE);
        activity.rlBanner.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        rcvSms.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        smsRcvAdapter = new SmsRcvAdapter(getActivity(), otherSmsContactSelfList);
        rcvSms.setAdapter(smsRcvAdapter);
    }

    /**
     * 初始化适配器事件
     */
    private void initEvent() {
        
        /* 长按短信列表item */
        smsRcvAdapter.setOnRcvLongClickListener(position -> {
            if (!isLongClick) {
                stopTimer();// 停止定时器
                isLongClick = true;// 进入允许删除状态
                tvSmsBatchSelectAll.setText(select_all);// 初始文本为selected all
                ((HomeRxActivity) getActivity()).ivSmsNew.setVisibility(View.GONE);// 短信新建暂时不可用
                otherSmsContactSelfList = OtherUtils.modifySMSContactSelf(otherSmsContactSelfList, true);
                smsRcvAdapter.notifys(otherSmsContactSelfList);
                llSmsBatchDeteled.setVisibility(isLongClick ? View.VISIBLE : View.GONE);
            }
        });
        
        /* 长按模式下点击单条短信 */
        smsRcvAdapter.setOnSMSWhenLongClickAfterListener((attr, contactIdList) -> {
            SmsFragments.this.contactIdList = contactIdList;
            if (contactIdList.size() == 0) {
                tvSmsBatchSelectAll.setText(select_all);
            }
        });
        
        /* 全选|全不选 */
        smsRcvAdapter.setOnSelectAllOrNotListener(contactIdList -> {
            SmsFragments.this.contactIdList = contactIdList;
        });

    }

    @OnClick({R.id.tv_sms_batchSelectAll, R.id.tv_sms_batchDeteled})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sms_batchSelectAll:// 全选按钮
                selectedAll();
                break;
            case R.id.tv_sms_batchDeteled:// 删除按钮
                if (contactIdList != null && contactIdList.size() > 0) {
                    // 显示弹窗
                    showDelPop();
                }
                break;
        }
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
        Drawable pop_bg = getActivity().getResources().getDrawable(R.drawable.bg_pop_conner);
        View inflate = View.inflate(getActivity(), R.layout.pop_smsdetail_deleted, null);
        int width = (int) (ScreenSize.getSize(getActivity()).width * 0.75f);
        int height = (int) (ScreenSize.getSize(getActivity()).height * 0.22f);
        Button tv_delete_cancel =  inflate.findViewById(R.id.tv_smsdetail_detele_cancel);
        Button tv_delete_confirm =  inflate.findViewById(R.id.tv_smsdetail_detele_confirm);
        tv_delete_cancel.setOnClickListener(v -> pop_deleted_sms.dismiss());
        tv_delete_confirm.setOnClickListener(v -> {
            pop_deleted_sms.dismiss();
            deleteSessionSms(contactIdList);
        });
        pop_deleted_sms = new PopupWindows(getActivity(), inflate, width, height, true, pop_bg);
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
            if(smsContacBeans != null && smsContacBeans.size() >0 ){
                for(GetSMSContactListBean.SMSContacBean smsContacBean : smsContacBeans){
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
            otherSmsContactSelfList = OtherUtils.getSMSSelfList(smsContactList);
            smsRcvAdapter.notifys(otherSmsContactSelfList);
            //SmsCountHelper.setSmsCount(getActivity(), HomeActivity.mTvHomeMessageCount);
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
        ((HomeRxActivity) getActivity()).ivSmsNew.setVisibility(View.VISIBLE);
        llSmsBatchDeteled.setVisibility(isLongClick ? View.VISIBLE : View.GONE);
        ToastUtil_m.show(getActivity(), getString(R.string.sms_delete_success));
        startTimer();// 重启定时器
    }

    /**
     * 删除失败后恢复UI
     */
    private void delFailedReset() {
        isLongClick = false;// 恢复标记位
        if (getActivity() != null) {
            ((HomeRxActivity) getActivity()).ivSmsNew.setVisibility(View.VISIBLE);
            if (llSmsBatchDeteled != null) {
                llSmsBatchDeteled.setVisibility(isLongClick ? View.VISIBLE : View.GONE);
            }
            ToastUtil_m.show(getActivity(), getString(R.string.sms_delete_error));
        }
        startTimer();// 重启定时器
    }

    @Override
    public boolean onBackPressed() {
        if (isLongClick) {// 如果处于长按状态下
            isLongClick = false;
            ((HomeRxActivity) getActivity()).ivSmsNew.setVisibility(View.VISIBLE);
            llSmsBatchDeteled.setVisibility(isLongClick ? View.VISIBLE : View.GONE);
            startTimer();// 重启定时器
            return isLongClick;
        } else {
            return true;
        }
    }

    private void stopTimer() {
        timerHelper.stop();
    }


    @Override
    public void onPause() {
        super.onPause();
        timerHelper.stop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
