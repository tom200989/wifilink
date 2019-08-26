package com.alcatel.wifilink.root.ue.frag;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.adapter.SmsDetatilAdapter;
import com.alcatel.wifilink.root.bean.SMSContactList;
import com.alcatel.wifilink.root.bean.SMSContentList;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.helper.SmsContentSortHelper;
import com.alcatel.wifilink.root.helper.SmsDeletePop;
import com.alcatel.wifilink.root.helper.SmsDraftHelper;
import com.alcatel.wifilink.root.helper.SmsSendHelper;
import com.alcatel.wifilink.root.helper.SmsWatcher;
import com.alcatel.wifilink.root.ue.activity.HomeActivity;
import com.alcatel.wifilink.root.utils.RootCons;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.hiber.cons.TimerState;
import com.hiber.impl.RootEventListener;
import com.hiber.tools.ShareUtils;
import com.p_xhelper_smart.p_xhelper_smart.bean.DeleteSmsParam;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSMSContactListBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSMSContentListBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSmsContentListParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.DeleteSMSHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSMSContactListHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSMSContentListHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSmsInitStateHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmsDetailFrag extends BaseFrag {

    @BindView(R.id.tv_smsdetail_date)
    TextView tvSmsdetailDate;// 路由器日期
    @BindView(R.id.rcv_smsdetail)
    RecyclerView rcvSmsdetail;// 消息列表
    @BindView(R.id.rv_smsdetail_send)
    Button rvSmsdetailSend;// 发送按钮
    @BindView(R.id.et_smsdetail_send)
    EditText etSmsdetailSend;// 内容编辑区
    @BindView(R.id.ib_smsdetail_back)
    ImageButton ib_back;
    @BindView(R.id.tv_smsdetail_title)
    TextView tv_title;
    @BindView(R.id.iv_smsdetail_delete)
    ImageView iv_delete;

    private SmsDetatilAdapter adapter;
    private SMSContentList smsContentList;
    private SMSContactList.SMSContact smsContact;
    private List<Long> smsIds = new ArrayList<>();
    private boolean isLongClick;// 处于长按状态
    private SmsDeletePop deletePop;
    private LinearLayoutManager linearLayoutManager;
    private String dateTimebanner = "";
    private int current_sms_num = 0;// 最近一次获取到的短信条数

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_sms_detail;
    }

    @Override
    public void initViewFinish(View inflateView) {
        super.initViewFinish(inflateView);
        initEvent();
        initView();
        initData();
        initClick();
        timerState = TimerState.ON_BUT_OFF_WHEN_HIDE_AND_PAUSE;
    }

    /**
     * 初始化监听
     */
    private void initEvent() {
        //  添加短信字数限制监听
        new SmsWatcher(activity, etSmsdetailSend);
        setEventListener(SMSContactList.SMSContact.class, new RootEventListener<SMSContactList.SMSContact>() {
            @Override
            public void getData(SMSContactList.SMSContact smsContact) {
                SmsDetailFrag.this.smsContact = smsContact;
                tv_title.setText(RootUtils.stitchPhone(activity, smsContact.getPhoneNumber()));
            }
        });
    }

    private void initView() {
        // set recycle view
        setRecycleView();
        // set recycle touch listener
        setRecycleListener();
    }

    private void initData() {
        // 读取草稿短信
        getDraftSms();
    }

    /**
     * 初始化点击事件
     */
    public void initClick() {
        ib_back.setOnClickListener(v -> clickBack());
        iv_delete.setVisibility(View.GONE);
        iv_delete.setOnClickListener(v -> deleteSmsPop());
        rvSmsdetailSend.setOnClickListener(v -> sendSms());//发送按钮
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o, view, s);
        getSmsContents(true);
    }

    @Override
    public boolean onBackPressed() {
        clickBack();
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        current_sms_num = 0;
    }

    /* **** setRecycleView **** */
    private void setRecycleView() {
        linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rcvSmsdetail.setLayoutManager(linearLayoutManager);
        adapter = new SmsDetatilAdapter(activity, linearLayoutManager, smsContentList, smsContact.getPhoneNumber());
        adapter.setOnSmsSelectedListener(smsIds1 -> {  /* 短信被选中 */
            // 被选中的短信ID集合
            this.smsIds = smsIds;
        });// 监听item被选中
        adapter.setOnSmsLongClickListener(() -> {
            // 短信被长按
            isLongClick = true;
            iv_delete.setVisibility(View.VISIBLE);
            etSmsdetailSend.setEnabled(!isLongClick);
            rvSmsdetailSend.setClickable(!isLongClick);
        });

        adapter.setOnSendSuccessListener(() -> {
            // 重新获取短信
            getSmsContents(true);
        });
        rcvSmsdetail.setAdapter(adapter);
    }

    /* **** setRecycleListener: 滑动监听并改变router time ui **** */
    private void setRecycleListener() {
        rcvSmsdetail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 1. according the coodinary of top to set the alpha
                    if (!rcvSmsdetail.canScrollVertically(-1)) {// 已经到顶
                        tvSmsdetailDate.setAlpha(0.2f);
                    } else {
                        tvSmsdetailDate.setAlpha(0.8f);
                    }
                } else {
                    tvSmsdetailDate.setAlpha(0.3f);
                }
                // 2. set time according the first item who can see
                setPositionTextTime();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    /* **** setPositionTextTime: 根据当前第一个可视的SMS显示对应的短信时间 **** */
    private void setPositionTextTime() {
        activity.runOnUiThread(() -> {
            List<SMSContentList.SMSContentBean> list = filterDraft(smsContentList);
            int pos = linearLayoutManager.findFirstVisibleItemPosition();
            dateTimebanner = list.get(pos).getSMSTime();
            String currentLanguage = ShareUtils.get(RootCons.LOCALE_LANGUAGE_COUNTRY,"");
            if (currentLanguage.contains(RootCons.LANGUAGES.RUSSIAN)) {
                dateTimebanner = dateTimebanner.replace("-", ".");
            }
            tvSmsdetailDate.setText(dateTimebanner);
        });
    }

    /* 提出草稿短信后按日期排序 */
    public List<SMSContentList.SMSContentBean> filterDraft(SMSContentList smsContentList) {
        List<SMSContentList.SMSContentBean> list = new ArrayList();
        for (SMSContentList.SMSContentBean scb : smsContentList.getSMSContentList()) {
            if (scb.getSMSType() == Cons.DRAFT) {
                continue;
            }
            list.add(scb);
        }
        Collections.sort(list, new SmsContentSortHelper());
        return list;
    }

    /* **** setRecyclePositionToLast: 定位到最后 **** */
    private void setRecyclePositionToLast(int position) {
        linearLayoutManager.scrollToPositionWithOffset(position, 0);
    }

    @Override
    public void setTimerTask() {
        super.setTimerTask();
        if (!isLongClick) {
            getSmsContents(false);
        }
    }

    /* **** timertask: getSmsContents **** */
    private void getSmsContents(boolean isSetRcvToLast) {
        // 检测sim卡是否有插入

        GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
        xGetSimStatusHelper.setOnGetSimStatusSuccessListener(result -> {
            if (result.getSIMState() == GetSimStatusBean.CONS_SIM_CARD_READY) {// no sim
                getContent(isSetRcvToLast);
            } else {
                toFrag(getClass(), SmsFrag.class, null, false);
            }
        });
        xGetSimStatusHelper.getSimStatus();
    }

    /**
     * @param isSetRcvToLast 是否需要定位rcv到底部
     */
    /* **** getContent **** */
    private void getContent(boolean isSetRcvToLast) {
        GetSmsInitStateHelper xGetSmsInitStateHelper = new GetSmsInitStateHelper();
        xGetSmsInitStateHelper.setOnGetSmsInitStateSuccessListener(SmsInitStateBean -> {
            if (SmsInitStateBean.getState() == GetSmsInitStateHelper.SMS_COMPLETE) {
                // 重新获取当前号码是否有未读消息
                GetSMSContactListHelper xGetSMSContactListHelper = new GetSMSContactListHelper();
                xGetSMSContactListHelper.setOnGetSmsContactListSuccessListener(SmsContactListBean -> {
                    // 1.1.判断最新获取的contact id集合是否包含传进来的id--> 是:回退
                    List<Long> contactIds = new ArrayList<>();
                    for (GetSMSContactListBean.SMSContacBean scc : SmsContactListBean.getSMSContactList()) {
                        contactIds.add((long) scc.getContactId());
                    }
                    if (!contactIds.contains(smsContact.getContactId())) {
                        clickBack();
                        return;
                    }

                    // 1.2.否:获取最新的短信列表
                    for (GetSMSContactListBean.SMSContacBean scc : SmsContactListBean.getSMSContactList()) {
                        long contactId = scc.getContactId();
                        // 1.是当前的contact id
                        if (contactId == smsContact.getContactId()) {
                            // 2.获取当前号码的未读消息数
                            int unreadCount = scc.getUnreadCount();
                            if (unreadCount > 0) {/* 如果有未读消息 */
                                if (!isHidden()) {
                                    // 4.向接口发起请求
                                    realToGetContent();
                                } else {
                                    // 3.用户离开
                                    // 4.把未读消息数量保存到MAP中
                                    HomeActivity.smsUnreadMap.put(contactId, unreadCount);
                                }
                                // 3.用户是否停留在短信详情页
                                realToGetContent();
                            } else {/* 没有未读消息, 直接获取内容--> 正常显示已读的消息 */
                                realToGetContent();// 向接口发起请求
                            }
                        }
                    }
                });
                xGetSMSContactListHelper.getSMSContactList(0);
            }
        });
        xGetSmsInitStateHelper.getSmsInitState();
    }

    /**
     * 向接口发起请求
     */
    private void realToGetContent() {
        long contactId = smsContact.getContactId();
        //用xsmart内部的Param替换旧的Param
        GetSmsContentListParam getSmsContentListParam = new GetSmsContentListParam();
        getSmsContentListParam.setPage(0);
        getSmsContentListParam.setContactId((int) contactId);

        GetSMSContentListHelper xGetSMSContentListHelper = new GetSMSContentListHelper();
        xGetSMSContentListHelper.setOnGetSmsContentListSuccessListener(bean -> {
            //用xsmart内部Bean转化为旧的bean，定义容器
            SMSContentList tempSmsContentList = new SMSContentList();
            tempSmsContentList.setPage(bean.getPage());
            tempSmsContentList.setContactId(bean.getContactId());
            tempSmsContentList.setPhoneNumber(bean.getPhoneNumber());
            tempSmsContentList.setTotalPageCount(bean.getTotalPageCount());

            List<GetSMSContentListBean.SMSContentBean> smsContentBeans = bean.getSMSContentList();
            if (smsContentBeans != null && smsContentBeans.size() > 0) {
                //旧的Bean列表容器
                List<SMSContentList.SMSContentBean> tempSmsContentBeans = new ArrayList<>();
                for (GetSMSContentListBean.SMSContentBean smsContentBean : smsContentBeans) {
                    //旧的Bean容器
                    SMSContentList.SMSContentBean tempSmsContentBean = new SMSContentList.SMSContentBean();
                    tempSmsContentBean.setReportStatus(smsContentBean.getReportStatus());
                    tempSmsContentBean.setSMSContent(smsContentBean.getSMSContent());
                    tempSmsContentBean.setSMSId(smsContentBean.getSMSId());
                    tempSmsContentBean.setSMSTime(smsContentBean.getSMSTime());
                    tempSmsContentBean.setSMSType(smsContentBean.getSMSType());
                    //填充旧的Bean
                    tempSmsContentBeans.add(tempSmsContentBean);
                }
                //填充旧的Bean列表
                tempSmsContentList.setSMSContentList(tempSmsContentBeans);
            }
            boolean isLast = false;
            // 1. refresh the list
            smsContentList = tempSmsContentList;
            // 1.1.获取最新信息数
            int newSize = smsContentList.getSMSContentList().size();
            if (newSize > current_sms_num) {
                isLast = true;// 如果最新获取到的信息数比临时缓存的大, 则修改[滚动到最后一条]标记
            }
            // 更新临时最新消息数
            current_sms_num = newSize;
            // 刷新适配器
            if (adapter != null) {
                adapter.notifys(smsContentList, isLast);
            }
            // 2. refresh the router time
            setPositionTextTime();
            // 3. force to  set rcv position to last
            if (isLast) {
                setRecyclePositionToLast(adapter.getItemCount() - 1);
            }
        });
        // when the current number have no sms, then close it
        xGetSMSContentListHelper.setOnGetSmsContentListFailListener(() -> toFrag(getClass(), SmsFrag.class, null, false));
        xGetSMSContentListHelper.getSMSContentList(getSmsContentListParam);
    }

    /* **** 获取草稿短信(只执行1次)  **** */
    private void getDraftSms() {

        SmsDraftHelper sdfp = new SmsDraftHelper(activity, smsContact.getContactId());
        sdfp.setOnNoSimListener(() -> {
            toast(R.string.home_no_sim, 2000);
            toFrag(getClass(), SmsFrag.class, null, false);
        });
        sdfp.setOnGetDraftListener(draft -> {
            etSmsdetailSend.setText(draft);
            etSmsdetailSend.setSelection(draft.length());
        });
        sdfp.getDraftSms();
    }

    /* **** clickBack **** */
    private void clickBack() {
        if (isLongClick) {// if long click status
            resetLongClickFlag();
        } else {
            // 查看编辑域是否有内容--> 保存为草稿
            draftSms();
            toFrag(getClass(), SmsFrag.class, null, false);
        }
    }

    /* **** sendSms **** */
    private void sendSms() {
        // 1. getInstant send sms content from et
        String content = RootUtils.getEDText(etSmsdetailSend, true);
        if (TextUtils.isEmpty(content)) {
            toast(R.string.home_no_sim, 2000);
            return;
        }
        // 2. send sms
        new SmsSendHelper(activity, smsContact.getPhoneNumber(), content) {
            @Override
            public void sendFinish(int status) {/* 发送完成 */
                if (status == Cons.SUCCESS) {
                    // 注意此处, 一调用即为标记为已读
                    getSmsContents(true);
                } else {
                    toFrag(getClass(), SmsFrag.class, null, false);
                }
            }
        };
        // 3. clear the et
        etSmsdetailSend.setText("");
    }


    /* **** resetLongClickFlag **** */
    private void resetLongClickFlag() {
        // the ori long click flag maybe true--> then set false
        isLongClick = false;// 1. reset this flag to set the selected logo gone
        adapter.isLongClick = isLongClick;// 2. and deliver this to the adapter flag
        iv_delete.setVisibility(View.GONE);// 3. set deleted button gone
        etSmsdetailSend.setEnabled(!isLongClick);// 4. set et enable
        rvSmsdetailSend.setClickable(!isLongClick);// 5. set send button enable
        if (deletePop != null) {// 6.pop dismiss if had
            deletePop.getPop().dismiss();
        }
    }

    /* **** draftSms **** */
    private void draftSms() {
        // et have no text
        if (TextUtils.isEmpty(RootUtils.getEDText(etSmsdetailSend, true))) {
            clearDraft(false);
        } else {
            clearDraft(true);
        }
    }

    /* **** clearDraft **** */
    public void clearDraft(boolean isSaveDraft) {
        // 1.first deleted the draft sms in router
        SmsDraftHelper sdfp = new SmsDraftHelper(activity, smsContact.getContactId());
        sdfp.setOnNoSimListener(() -> {
            toast(R.string.home_no_sim, 2000);
            toFrag(getClass(), SmsFrag.class, null, false);
        });
        sdfp.setOnClearDraftListener(() -> {
            if (isSaveDraft) {
                // 2.then save sms to router
                saveDraft();
            }
        });
        sdfp.clearDraft();
    }

    /* **** 保存草稿 **** */
    private void saveDraft() {
        String content = RootUtils.getEDText(etSmsdetailSend, true);
        List<String> phoneNumber = smsContact.getPhoneNumber();
        SmsDraftHelper sdfp = new SmsDraftHelper(activity, smsContact.getContactId());
        sdfp.setOnSaveDraftListener(() -> {
            toast(R.string.home_no_sim, 2000);
            toFrag(getClass(), SmsFrag.class, null, false);

        });
        sdfp.saveDraftSms(phoneNumber, content);
    }

    /* 弹出删除窗口 */
    private void deleteSmsPop() {
        // show the delete pop
        deletePop = new SmsDeletePop(activity) {
            @Override
            public void getView(View inflate) {
                TextView tv_delete_cancel = inflate.findViewById(R.id.tv_smsdetail_detele_cancel);
                TextView tv_delete_confirm = inflate.findViewById(R.id.tv_smsdetail_detele_confirm);
                tv_delete_cancel.setOnClickListener(v -> {
                    resetLongClickFlag();// 1. reset the status ui
                    getSmsContents(false);// 2. getInstant data again
                });
                tv_delete_confirm.setOnClickListener(v -> deleteSms());
            }
        };
    }

    /* 真正删除短信 */
    private void deleteSms() {
        // reset the long status
        resetLongClickFlag();
        //用新的Param替换旧的Param
        DeleteSmsParam xDeleteSmsParam = new DeleteSmsParam();
        xDeleteSmsParam.setDelFlag(Cons.DELETE_MORE_SMS);
        xDeleteSmsParam.setSMSArray(smsIds);

        DeleteSMSHelper xDeleteSMSHelper = new DeleteSMSHelper();
        xDeleteSMSHelper.setOnDeleteSmsSuccessListener(result -> {
            if (isLongClick) {
                toast(R.string.home_no_sim, 2000);
            }
            resetLongClickFlag();
            getSmsContents(false);/* 删除短信后无需跳到最后一条 */
        });
        xDeleteSMSHelper.setOnDeleteSmsFailListener(() -> {
            toast(R.string.home_no_sim, 2000);
        });
        xDeleteSMSHelper.setOnDeleteFailListener(() -> {
            toast(R.string.home_no_sim, 2000);
        });
        xDeleteSMSHelper.deleteSms(xDeleteSmsParam);
    }

}
