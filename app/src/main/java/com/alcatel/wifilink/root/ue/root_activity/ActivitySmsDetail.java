package com.alcatel.wifilink.root.ue.root_activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.SMSContentList;
import com.alcatel.wifilink.root.bean.SMSDetailItem;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.helper.SmsContentSortHelper;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.alcatel.wifilink.root.ue.root_activity.InquireDialog.OnInquireApply;
import com.alcatel.wifilink.root.utils.ActionbarSetting;
import com.alcatel.wifilink.root.utils.C_DataUti;
import com.alcatel.wifilink.root.utils.C_DensityUtils;
import com.alcatel.wifilink.root.utils.C_ENUM.SendStatus;
import com.alcatel.wifilink.root.utils.DataUtils;
import com.alcatel.wifilink.root.utils.ToastUtil;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.p_xhelper_smart.p_xhelper_smart.bean.DeleteSmsParam;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSMSContentListBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSmsContentListParam;
import com.p_xhelper_smart.p_xhelper_smart.bean.SaveSmsParam;
import com.p_xhelper_smart.p_xhelper_smart.bean.SendSmsParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.DeleteSMSHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSMSContentListHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSendSMSResultHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSmsInitStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SaveSMSHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SendSMSHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import static android.widget.RelativeLayout.ALIGN_LEFT;
import static android.widget.RelativeLayout.CENTER_VERTICAL;
import static android.widget.RelativeLayout.LEFT_OF;
import static com.alcatel.wifilink.R.id.sms_sent_fail_left_iv;

public class ActivitySmsDetail extends BaseActivityWithBack implements OnClickListener, OnScrollListener, TextWatcher {
    public static final String INTENT_EXTRA_SMS_NUMBER = "sms_number";
    public static final String INTENT_EXTRA_CONTACT_ID = "contact_id";

    private Timer m_timer = new Timer();

    // private TextView m_tvTitle = null;
    private Button m_btnSend = null;
    //private Button m_btnDelete = null;
    //private LinearLayout m_btnBack = null;
    private EditText m_etContent = null;

    private ListView m_smsListView = null;
    private List<SMSDetailItem> m_smsListData = new ArrayList<>();
    private String m_smsNumber = new String();
    private int m_nContactID = 0;

    private ProgressBar m_progressWaiting = null;

    private boolean m_bDeleteContact = true;
    private boolean m_bIsLastOneMessage = false;

    private boolean m_bDeleteSingleEnable = true;
    private boolean m_bDeleteEnd = false;
    private boolean m_bSendEnd = false;
    private SendStatus m_sendStatus = SendStatus.None;

    private boolean m_bFristGet = true;
    private TimerHelper timerHelper;

    private ImageButton ib_back;// back button
    private TextView tv_title;// title
    private ImageView iv_delete;// deleted

    public boolean deletedbutton_worked = false;// 删除按钮是否显示(显示则: 可删)
    public List<String> posList = new ArrayList<>();// 选择多条短信时的临时位标集合

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_details_views);
        getWindow().setBackgroundDrawable(null);
        // Edit Text
        m_etContent = (EditText) this.findViewById(R.id.ID_SMS_DETAIL_EDIT_CONTENT);
        m_etContent.addTextChangedListener(this);

        String text = getOneSmsLenth(new String()) + "/1";
        // Button
        m_btnSend = (Button) this.findViewById(R.id.ID_SMS_DETAIL_BUTTON_SEND);
        m_btnSend.setOnClickListener(this);

        m_progressWaiting = (ProgressBar) this.findViewById(R.id.sms_waiting_progress);

        m_smsNumber = this.getIntent().getStringExtra(INTENT_EXTRA_SMS_NUMBER);
        m_nContactID = this.getIntent().getIntExtra(INTENT_EXTRA_CONTACT_ID, 0);

        m_smsListView = (ListView) this.findViewById(R.id.sms_detail_list_view);
        SmsDetailListAdapter smsListAdapter = new SmsDetailListAdapter(this);
        m_smsListView.setAdapter(smsListAdapter);
        m_smsListView.setOnScrollListener(this);

        initActionbar();

    }

    /* **** initActionbar **** */
    private void initActionbar() {
        new ActionbarSetting() {
            @Override
            public void findActionbarView(View view) {
                ib_back = (ImageButton) view.findViewById(R.id.ib_smsdetail_back);
                ib_back.setOnClickListener(ActivitySmsDetail.this);

                tv_title = (TextView) view.findViewById(R.id.tv_smsdetail_title);
                tv_title.setText(m_smsNumber);

                iv_delete = (ImageView) view.findViewById(R.id.iv_smsdetail_delete);
                iv_delete.setVisibility(View.GONE);
                iv_delete.setOnClickListener(ActivitySmsDetail.this);
            }
        }.settingActionbarAttr(this, getSupportActionBar(), R.layout.actionbar_smsdetail);
    }

    @Override
    public void onResume() {
        super.onResume();
        /* **** timerHelper **** */
        timerHelper = new TimerHelper(this) {
            @Override
            public void doSomething() {
                getAllStatus();
            }
        };
        timerHelper.start(5000);
    }

    /* **** getAllstatus **** */
    private void getAllStatus() {
        GetSmsInitStateHelper xGetSmsInitStateHelper = new GetSmsInitStateHelper();
        xGetSmsInitStateHelper.setOnGetSmsInitStateSuccessListener(bean -> {
            if (bean.getState() == GetSmsInitStateHelper.SMS_COMPLETE) {
                // getInstant sms contents
                getSMSContentList();
            }
        });
        xGetSmsInitStateHelper.getSmsInitState();
    }

    /* **** getSMSContentList **** */
    private void getSMSContentList() {
        //用smart框架内部param代替旧的Param
        GetSmsContentListParam getSmsContentListParam = new GetSmsContentListParam();
        getSmsContentListParam.setPage(0);
        getSmsContentListParam.setContactId(m_nContactID);

        GetSMSContentListHelper xGetSMSContentListHelper = new GetSMSContentListHelper();
        xGetSMSContentListHelper.setOnGetSmsContentListSuccessListener(bean -> {
            //将xsmart框架内部的Bean转为旧的Bean
            SMSContentList smsContentList = new SMSContentList();
            smsContentList.setPage(bean.getPage());
            smsContentList.setContactId(bean.getContactId());
            smsContentList.setPhoneNumber(bean.getPhoneNumber());
            smsContentList.setTotalPageCount(bean.getTotalPageCount());
            //xsmart框架内部Bean列表
            List<GetSMSContentListBean.SMSContentBean> smsContentBeans = bean.getSMSContentList();
            if(smsContentBeans != null && smsContentBeans.size() > 0){
                //旧的Bean列表容器
                List<SMSContentList.SMSContentBean> tempSMSContentList = new ArrayList<>();
                for(GetSMSContentListBean.SMSContentBean smsContentBean : smsContentBeans){
                    //旧Bean容器
                    SMSContentList.SMSContentBean tempSmsContentBean = new SMSContentList.SMSContentBean();
                    tempSmsContentBean.setReportStatus(smsContentBean.getReportStatus());
                    tempSmsContentBean.setSMSContent(smsContentBean.getSMSContent());
                    tempSmsContentBean.setSMSId(smsContentBean.getSMSId());
                    tempSmsContentBean.setSMSTime(smsContentBean.getSMSTime());
                    tempSmsContentBean.setSMSType(smsContentBean.getSMSType());
                    //填充旧的Bean
                    tempSMSContentList.add(tempSmsContentBean);
                }
                //填充旧的Bean列表容器
                smsContentList.setSMSContentList(tempSMSContentList);
            }
            getSuccessful(smsContentList);
        });
        xGetSMSContentListHelper.setOnGetListFailListener(ActivitySmsDetail.this::finish);
        xGetSMSContentListHelper.getSMSContentList(getSmsContentListParam);
    }

    /* **** getSuccessful **** */
    private void getSuccessful(SMSContentList result) {
        // hibernater the sms data
        getSmsListData(result);
        /* session 1 */
        iv_delete.setEnabled(true);
        if (m_bFristGet) {
            m_bFristGet = false;
            m_progressWaiting.setVisibility(View.GONE);
        }
        /* session 2 */
        if (m_bDeleteEnd) {
            m_bDeleteEnd = false;
            m_progressWaiting.setVisibility(View.GONE);
            m_etContent.setEnabled(true);
            if (m_etContent.getText().toString() != null && m_etContent.getText().toString().length() > 0)
                m_btnSend.setEnabled(true);
            else
                m_btnSend.setEnabled(false);
            iv_delete.setEnabled(true);
            m_bDeleteSingleEnable = true;
        } else if (m_bSendEnd) {
            m_bSendEnd = false;
            m_progressWaiting.setVisibility(View.GONE);
            m_etContent.setEnabled(true);
            iv_delete.setEnabled(true);
            m_bDeleteSingleEnable = true;
            if (m_sendStatus == SendStatus.Success) {
                m_etContent.setText("");
                m_btnSend.setEnabled(false);
            } else if (m_sendStatus == SendStatus.Fail || m_sendStatus == SendStatus.Fail_Memory_Full) {
                m_btnSend.setEnabled(true);
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        m_progressWaiting.setVisibility(View.GONE);
        m_etContent.setEnabled(true);
        m_bDeleteSingleEnable = true;
        m_bSendEnd = false;
        m_bDeleteEnd = false;
        timerHelper.stop();
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ID_SMS_DETAIL_BUTTON_SEND:// send button
                OnBtnSend();
                break;
            case R.id.ib_smsdetail_back:// back button
                onBackPressed();
                break;
            case R.id.iv_smsdetail_delete:// deleted button
                // when user have long click action , then deletedbutton_worked will be true
                if (deletedbutton_worked) {
                    OnBtnDelete();
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        /* 如果是删除短信状态--> 恢复原短信界面 */
        if (deletedbutton_worked) {
            // reback flag
            deletedbutton_worked = !deletedbutton_worked;
            iv_delete.setVisibility(deletedbutton_worked ? View.VISIBLE : View.GONE);
            // the sms list cant be empty
            if (m_smsListData.size() > 0) {
                // set all sms is not check
                for (SMSDetailItem item : m_smsListData) {
                    item.setSelectFlag(false);
                }
                // set ui gone
                for (int i = 0; i < m_smsListData.size(); i++) {
                    if (i != 0) {
                        View childAt = m_smsListView.getChildAt(i);
                        childAt.findViewById(R.id.iv_smsdetail_checkbox).setVisibility(View.GONE);
                    }

                }
            }

        } else {/* 否则返回上一页Activity */
            OnBtnBack();
            super.onBackPressed();
        }


    }

    /* **** OnBtnBack **** */
    private void OnBtnBack() {
        String strContent = m_etContent.getText().toString();
        String strNumber = m_smsNumber;
        if (strContent != null)
            strContent = strContent.trim();
        if (strContent != null && strContent.length() > 0 && strNumber != null && strNumber.length() > 0) {
            SMSDetailItem item = m_smsListData.get(m_smsListData.size() - 1);

            List<String> phones = new ArrayList<>();
            phones.add(strNumber);
            //用xsmart内部的Param替换旧的Param
            SaveSmsParam xSaveSmsParam = new SaveSmsParam();
            xSaveSmsParam.setSMSId((int) item.nSMSID);
            xSaveSmsParam.setSMSContent(strContent);
            xSaveSmsParam.setSMSTime(DataUtils.getCurrent());
            xSaveSmsParam.setPhoneNumber(phones);
            SaveSMSHelper xSaveSMSHelper = new SaveSMSHelper();
            xSaveSMSHelper.setOnSaveSMSSuccessListener(() -> {
                ToastUtil_m.show(ActivitySmsDetail.this, getString(R.string.sms_save_success));
            });
            xSaveSMSHelper.setOnSaveFailListener(() -> {
                ToastUtil_m.show(ActivitySmsDetail.this, getString(R.string.sms_save_error));
                ActivitySmsDetail.this.finish();
            });
            xSaveSMSHelper.setOnSpaceFullListener(() -> {
                ToastUtil_m.show(ActivitySmsDetail.this, getString(R.string.sms_error_message_full_storage));
                ActivitySmsDetail.this.finish();
            });
            xSaveSMSHelper.saveSms(xSaveSmsParam);
        } else {
            this.finish();
            if (m_smsListData.size() == 0)
                return;
            SMSDetailItem item = m_smsListData.get(m_smsListData.size() - 1);
            if (null != item && item.eSMSType == Cons.DRAFT) {
                ArrayList<Long> smsids = new ArrayList<>();
                smsids.add(item.nSMSID);
                //deletedSmsFuntion(Cons.DELETE_MORE_SMS, smsids, m_smsListData, item);
            }
        }
    }

    /* **** OnBtnDelete **** */
    private void OnBtnDelete() {
        final InquireDialog inquireDlg = new InquireDialog(ActivitySmsDetail.this);
        inquireDlg.m_titleTextView.setText(R.string.dialog_delete_title);
        inquireDlg.m_contentTextView.setText(R.string.dialog_delete_content);
        inquireDlg.m_contentDescriptionTextView.setText(R.string.dialog_delete_content_description);
        inquireDlg.m_confirmBtn.setText(R.string.delete);
        inquireDlg.showDialog(new OnInquireApply() {
            @Override
            public void onInquireApply() {

                m_progressWaiting.setVisibility(View.VISIBLE);
                m_etContent.setEnabled(false);
                m_btnSend.setEnabled(false);
                iv_delete.setEnabled(false);
                m_bDeleteSingleEnable = false;

                boolean bHaveSms = false;
                for (int i = 0; i < m_smsListData.size(); i++) {
                    SMSDetailItem item = m_smsListData.get(i);
                    if (item.bIsDateItem == false) {
                        bHaveSms = true;
                        break;
                    }
                }

                if (bHaveSms == true) {
                    // prepare a list for smsId
                    List<Long> smsids = new ArrayList<>();
                    // foreach poslist for who have selected
                    for (String position : posList) {
                        smsids.add(m_smsListData.get(Integer.valueOf(position)).getnSMSID());
                    }
                    // real to del
                    deletedSmsFuntion(Cons.DELETE_MORE_SMS, smsids, m_smsListData, null);
                }
                inquireDlg.closeDialog();
            }
        });
    }

    /* **** OnBtnSend **** */
    private void OnBtnSend() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(m_etContent.getWindowToken(), 0);

        List<String> phones = new ArrayList<>();
        phones.add(tv_title.getText().toString());
        //用xsmart内部的Param替换旧的Param
        SendSmsParam xSendSmsParam = new SendSmsParam();
        xSendSmsParam.setSMSId(-1);
        xSendSmsParam.setSMSContent(m_etContent.getText().toString());
        xSendSmsParam.setSMSTime(DataUtils.getCurrent());
        xSendSmsParam.setPhoneNumber(phones);
        SendSMSHelper xSendSMSHelper = new SendSMSHelper();
        xSendSMSHelper.setOnSendSmsSuccessListener(() -> {
            m_progressWaiting.setVisibility(View.GONE);
            /* getSendResult */
            getSendResult();
        });
        xSendSMSHelper.setOnSendFailListener(() -> {
            String msgRes = ActivitySmsDetail.this.getString(R.string.sms_error_send_error);
            Toast.makeText(ActivitySmsDetail.this, msgRes, Toast.LENGTH_SHORT).show();
            m_progressWaiting.setVisibility(View.GONE);
            m_etContent.setEnabled(true);
            InputMethodManager imm0 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm0.hideSoftInputFromWindow(m_etContent.getWindowToken(), 0);
            m_btnSend.setEnabled(true);
            iv_delete.setEnabled(true);
            m_bDeleteSingleEnable = true;
            m_bSendEnd = true;
            m_sendStatus = SendStatus.Fail;
            getAllStatus();
        });
        xSendSMSHelper.setOnLastMessageListener(() -> {
            String msgRes = ActivitySmsDetail.this.getString(R.string.sms_error_send_error);
            Toast.makeText(ActivitySmsDetail.this, msgRes, Toast.LENGTH_SHORT).show();
            m_progressWaiting.setVisibility(View.GONE);
            m_etContent.setEnabled(true);
            InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm1.hideSoftInputFromWindow(m_etContent.getWindowToken(), 0);
            m_btnSend.setEnabled(true);
            iv_delete.setEnabled(true);
            m_bDeleteSingleEnable = true;
            m_bSendEnd = true;
            m_sendStatus = SendStatus.Fail;
            getAllStatus();
        });
        xSendSMSHelper.setOnSpaceFullListener(() -> {
            String msgRes = ActivitySmsDetail.this.getString(R.string.sms_error_message_full_storage);
            ToastUtil.showMessage(ActivitySmsDetail.this, msgRes, Toast.LENGTH_SHORT);
            m_progressWaiting.setVisibility(View.GONE);
            m_etContent.setEnabled(true);
            m_btnSend.setEnabled(true);
            iv_delete.setEnabled(true);
            m_bDeleteSingleEnable = true;
            m_bSendEnd = true;
            m_sendStatus = SendStatus.Fail_Memory_Full;
            getAllStatus();
        });
        xSendSMSHelper.sendSms(xSendSmsParam);

        m_progressWaiting.setVisibility(View.VISIBLE);
        m_btnSend.setEnabled(false);
        m_etContent.setEnabled(false);
        iv_delete.setEnabled(false);
        m_bDeleteSingleEnable = false;
    }

    /* **** getSendResult **** */
    private void getSendResult() {
        GetSendSMSResultHelper xGetSendSMSResultHelper = new GetSendSMSResultHelper();
        xGetSendSMSResultHelper.setOnGetSendSmsResultSuccessListener(bean -> {
            m_progressWaiting.setVisibility(View.GONE);
            int resultCode = bean.getSendStatus();
            SendStatus sendStatus = SendStatus.build(resultCode);
            m_sendStatus = sendStatus;
            boolean bEnd = false;
            if (sendStatus == SendStatus.Fail) {
                String msgRes = ActivitySmsDetail.this.getString(R.string.sms_error_send_error);
                Toast.makeText(ActivitySmsDetail.this, msgRes, Toast.LENGTH_SHORT).show();
                bEnd = true;
            }
            if (sendStatus == SendStatus.Fail_Memory_Full) {
                String msgRes = ActivitySmsDetail.this.getString(R.string.sms_error_message_full_storage);
                ToastUtil.showMessage(ActivitySmsDetail.this, msgRes, Toast.LENGTH_SHORT);
                bEnd = true;
            }
            if (sendStatus == SendStatus.Success) {
                String msgRes = ActivitySmsDetail.this.getString(R.string.sms_send_success);
                Toast.makeText(ActivitySmsDetail.this, msgRes, Toast.LENGTH_SHORT).show();
                bEnd = true;
            }

            /* attesion: if the first state is sending, then request again */
            if (sendStatus == SendStatus.Sending) {
                getSendResult();
            }

            if (bEnd == true) {
                m_bSendEnd = true;
                getAllStatus();
            }
        });
        xGetSendSMSResultHelper.setOnGetSendSmsResultFailListener(() -> {
            m_progressWaiting.setVisibility(View.GONE);
            m_etContent.setEnabled(true);
            m_btnSend.setEnabled(true);
            iv_delete.setEnabled(true);
            m_bDeleteSingleEnable = true;
        });
        xGetSendSMSResultHelper.setOnGetSendFailListener(() -> {
            ToastUtil_m.show(ActivitySmsDetail.this, getString(R.string.sms_error_send_error));
        });
        xGetSendSMSResultHelper.getSendSmsResult();
    }

    /* **** getOneSmsLenth **** */
    public static int getOneSmsLenth(String strSmsContent) {
        if (null == strSmsContent)
            return 160;

        if (strSmsContent.length() < strSmsContent.getBytes().length) {
            return 67;
        } else {
            return 160;
        }

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        String strInfo = s == null ? null : s.toString();
        if (s != null && strInfo.length() > 0) {
            m_btnSend.setEnabled(true);

        } else {
            m_btnSend.setEnabled(false);
        }

        int sms[] = SmsMessage.calculateLength(s, false);
        int msgCount = sms[0];
        int codeUnitCount = sms[1];
        int codeUnitsRemaining = sms[2];
        int codeUnitSize = sms[3];

        if (msgCount > 10) {
            int nMax = 670;
            if (codeUnitSize == 1) {
                nMax = 1530;
            }
            int selEndIndex = Selection.getSelectionEnd(s);
            String newStr = strInfo.substring(0, nMax);
            m_etContent.setText(newStr);

            Editable editable = m_etContent.getText();
            int newLen = editable.length();

            if (selEndIndex > newLen) {
                selEndIndex = editable.length();
            }

            Selection.setSelection(editable, selEndIndex);
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    // private void getSmsListData(SmsContentMessagesModel smsContent) {
    /* **** getSmsListData **** */
    private void getSmsListData(SMSContentList result) {
        Collections.sort(result.getSMSContentList(), new SmsContentSortHelper());
        m_smsListData.clear();

        List<SMSContentList.SMSContentBean> smsContentList = result.getSMSContentList();
        for (int i = 0; i < smsContentList.size(); i++) {
            SMSContentList.SMSContentBean smsContentBean = smsContentList.get(i);
            Date smsDate = C_DataUti.formatDateFromString(smsContentBean.getSMSTime());

            if (i == 0) {
                SMSDetailItem item = new SMSDetailItem();
                item.bIsDateItem = true;
                if (smsDate != null) {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    item.strTime = format.format(smsDate);
                }

                m_smsListData.add(item);
            } else {
                Date preSmsDate = C_DataUti.formatDateFromString(smsContentList.get(i - 1).getSMSTime());
                if (!(smsDate.getYear() == preSmsDate.getYear() && smsDate.getMonth() == preSmsDate.getMonth() && smsDate.getDate() == preSmsDate.getDate())) {
                    SMSDetailItem item = new SMSDetailItem();
                    item.bIsDateItem = true;
                    if (smsDate != null) {
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        item.strTime = format.format(smsDate);
                    }

                    m_smsListData.add(item);
                }
            }

            SMSDetailItem item = new SMSDetailItem();
            item.bIsDateItem = false;
            item.strContent = smsContentBean.getSMSContent();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            item.strTime = format.format(smsDate);
            item.eSMSType = smsContentBean.getSMSType();
            item.nContactID = m_nContactID;
            item.nSMSID = smsContentBean.getSMSId();
            item.selectFlag = false;
            m_smsListData.add(item);
        }

        // draft ui
        DarftSMSDisplay();
        // refreshContext adapter
        ((SmsDetailListAdapter) m_smsListView.getAdapter()).notifyDataSetChanged();
        // set on long click and item click
        setOnSmsClickAndLongClick();
    }

    /* **** setOnSmsClickAndLongClick **** */
    private void setOnSmsClickAndLongClick() {
        /* **** set on long click **** */
        // 1. first user will on long click to the layout
        m_smsListView.setOnItemLongClickListener((parent, view, position, id) -> {
            if (position != 0) {// exclude the position == 0 because is the date layout
                // set delete function is worked
                rebackStatus(true);
            }
            return true;
        });
        
        /* **** set on click **** */
        m_smsListView.setOnItemClickListener((parent, view, position, id) -> {
            if (deletedbutton_worked && position != 0) {
                // init boolean
                boolean temp = false;

                // add position to poslit or if the postion exist then remove
                String postrs = String.valueOf(position);
                if (posList.contains(postrs)) {
                    posList.remove(postrs);
                    temp = false;
                } else {
                    posList.add(postrs);
                    temp = true;
                }
                // show checkbox ui
                ImageView sms_checkbox = (ImageView) view.findViewById(R.id.iv_smsdetail_checkbox);
                sms_checkbox.setImageResource(temp ? R.drawable.checkbox_android_on : R.drawable.checkbox_android_off);
            }
        });
    }

    /* **** DarftSMSDisplay **** */
    private void DarftSMSDisplay() {
        if (m_smsListData.size() == 0)
            return;
        SMSDetailItem item = m_smsListData.get(m_smsListData.size() - 1);
        if (item.eSMSType == Cons.DRAFT && !m_etContent.isFocused()) {
            m_etContent.setEnabled(true);
            m_etContent.setText(item.strContent);
        }
    }

    /* **** SmsDetailListAdapter **** */
    private class SmsDetailListAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public SmsDetailListAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return m_smsListData.size();
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public final class ViewHolder {

            public ImageView sms_checkbox;

            public FrameLayout dateLayout;
            public TextView date;
            public RelativeLayout smsContentLayout;
            public TextView smsContent;
            public TextView smsDate;
            public ImageView sentFail;
            public ImageView sentFailLeft;
            public TextView sendFailText;
            public View placeHolder;
        }

        /* **** getView **** */
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(ActivitySmsDetail.this).inflate(R.layout.sms_detail_item, null);

                // check box
                holder.sms_checkbox = (ImageView) convertView.findViewById(R.id.iv_smsdetail_checkbox);

                holder.dateLayout = (FrameLayout) convertView.findViewById(R.id.sms_detail_date_layout);
                holder.date = (TextView) convertView.findViewById(R.id.sms_detail_date_textview);
                holder.smsContentLayout = (RelativeLayout) convertView.findViewById(R.id.sms_detail_content_layout);
                holder.smsDate = (TextView) convertView.findViewById(R.id.sms_detail_date);
                holder.smsContent = (TextView) convertView.findViewById(R.id.sms_detail_content);
                holder.sentFail = (ImageView) convertView.findViewById(R.id.sms_sent_fail_image);
                holder.sentFailLeft = (ImageView) convertView.findViewById(sms_sent_fail_left_iv);
                holder.sendFailText = (TextView) convertView.findViewById(R.id.sms_sent_fail_text);
                holder.placeHolder = (View) convertView.findViewById(R.id.place_holder);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == m_smsListData.size() - 1)
                holder.placeHolder.setVisibility(View.VISIBLE);
            else
                holder.placeHolder.setVisibility(View.GONE);


            if (m_smsListData.get(position).bIsDateItem) {
                holder.date.setText(m_smsListData.get(position).strTime);
                holder.dateLayout.setVisibility(View.VISIBLE);
                holder.smsContentLayout.setVisibility(View.GONE);

                holder.sentFailLeft.setVisibility(View.GONE);
            } else {
                holder.dateLayout.setVisibility(View.GONE);
                holder.smsContentLayout.setVisibility(View.VISIBLE);
                holder.smsDate.setText(m_smsListData.get(position).strTime);

                holder.smsContent.setText((String) m_smsListData.get(position).strContent);

                LayoutParams contentLayout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                final int type = m_smsListData.get(position).eSMSType;
                int nContentLayoutBg = R.drawable.selector_sms_detail_receive;
                holder.smsContentLayout.setVisibility(View.VISIBLE);
                switch (type) {
                    case Cons.REPORT:
                        holder.smsContent.setText(R.string.sms_report_message);
                        holder.smsContentLayout.setLayoutParams(contentLayout);
                        holder.smsContentLayout.setBackgroundResource(nContentLayoutBg);
                    case Cons.READ:
                    case Cons.UNREAD:
                        contentLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT, R.id.sms_layout);
                        nContentLayoutBg = R.drawable.selector_sms_detail_receive;
                        contentLayout.setMargins(30, 10, 80, 10);
                        holder.smsContent.setTextColor(ActivitySmsDetail.this.getResources().getColor(R.color.color_black));

                        holder.smsDate.setTextColor(ActivitySmsDetail.this.getResources().getColor(R.color.color_grey));
                        holder.sendFailText.setTextColor(ActivitySmsDetail.this.getResources().getColor(R.color.color_grey));
                        holder.sentFail.setImageResource(R.drawable.warning_blue_bg);
                        holder.smsContentLayout.setLayoutParams(contentLayout);
                        holder.smsContentLayout.setBackgroundResource(nContentLayoutBg);
                        holder.smsContentLayout.setPadding(70, 0, 0, 20);
                        break;
                    case Cons.DRAFT:
                        if (position == m_smsListData.size() - 1) {
                            holder.smsContentLayout.setVisibility(View.GONE);
                        }
                        holder.smsContentLayout.setLayoutParams(contentLayout);
                        holder.smsContentLayout.setBackgroundResource(nContentLayoutBg);
                    default:
                        contentLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, R.id.sms_layout);
                        nContentLayoutBg = R.drawable.selector_sms_detail_out;
                        contentLayout.setMargins(20, 10, 30, 10);
                        holder.smsContent.setTextColor(ActivitySmsDetail.this.getResources().getColor(R.color.color_white));
                        holder.smsDate.setTextColor(ActivitySmsDetail.this.getResources().getColor(R.color.color_sms_detail_send_grey));
                        holder.sendFailText.setTextColor(ActivitySmsDetail.this.getResources().getColor(R.color.color_sms_detail_send_grey));
                        holder.sentFail.setImageResource(R.drawable.sms_failed_white_bg);

                        int leftFailWidthParams = C_DensityUtils.dip2px(ActivitySmsDetail.this, 25f);
                        LayoutParams sentFailLeftParams = new LayoutParams(leftFailWidthParams, leftFailWidthParams);
                        if (holder.smsContent.getLineCount() > 1 || holder.smsContent.getText().toString().length() >= 12) {//POP 4+型号时15是极限
                            //多行或者当行过长时
                            int leftFailMarginParams = C_DensityUtils.dip2px(ActivitySmsDetail.this, 10f);
                            sentFailLeftParams.setMargins(leftFailMarginParams, 0, 0, 0);
                            sentFailLeftParams.addRule(ALIGN_LEFT);
                            contentLayout.addRule(RelativeLayout.RIGHT_OF, R.id.sms_sent_fail_left_iv);
                        } else {
                            //单行够短时
                            sentFailLeftParams.addRule(LEFT_OF, R.id.sms_detail_content_layout);
                        }
                        sentFailLeftParams.addRule(CENTER_VERTICAL);
                        holder.sentFailLeft.setLayoutParams(sentFailLeftParams);

                        holder.smsContentLayout.setLayoutParams(contentLayout);
                        holder.smsContentLayout.setBackgroundResource(nContentLayoutBg);
                        holder.smsContentLayout.setPadding(20, 0, 70, 20);
                        break;
                }

                if (type == Cons.SENT_FAILED) {
                    holder.sentFail.setVisibility(View.GONE);
                    holder.sendFailText.setVisibility(View.GONE);

                    holder.sentFailLeft.setVisibility(View.VISIBLE);
                } else {
                    holder.sentFail.setVisibility(View.GONE);
                    holder.sendFailText.setVisibility(View.GONE);

                    holder.sentFailLeft.setVisibility(View.GONE);
                }

            }

            return convertView;
        }

    }

    /* **** deletedSmsFuntion **** */
    private void deletedSmsFuntion(int DelFlag, List<Long> smsIds, List<SMSDetailItem> m_smsListData, @Nullable SMSDetailItem
                                                                                                            item) {
        //用xsmart内部的Param替换旧的Param
        DeleteSmsParam xDeleteSmsParam = new DeleteSmsParam();
        xDeleteSmsParam.setDelFlag(DelFlag);
        xDeleteSmsParam.setSMSArray(smsIds);

        DeleteSMSHelper xDeleteSMSHelper = new DeleteSMSHelper();
        xDeleteSMSHelper.setOnDeleteSmsSuccessListener(result-> {
            // when deleted sendAgainSuccess then reset all status
            rebackStatus(false);

            m_bDeleteEnd = true;
            m_progressWaiting.setVisibility(View.GONE);
            Toast.makeText(ActivitySmsDetail.this, getString(R.string.sms_delete_multi_success), Toast.LENGTH_SHORT).show();

            /* if item is not null it means that just del the single sms */
            if (item != null && m_smsListData.size() > 0 && m_smsListData.contains(item)) {
                m_smsListData.remove(item);
            }

            if (m_smsListData.size() <= 0) {
                ActivitySmsDetail.this.finish();
            } else {
                // 重新获取内容
                getAllStatus();
            }
        });
        xDeleteSMSHelper.setOnDeleteSmsFailListener(() -> {
            rebackStatus(false);
            m_progressWaiting.setVisibility(View.GONE);
            Toast.makeText(ActivitySmsDetail.this, getString(R.string.sms_delete_error), Toast.LENGTH_SHORT).show();
            // 重新获取内容
            getAllStatus();
        });
        xDeleteSMSHelper.setOnDeleteFailListener(() -> m_progressWaiting.setVisibility(View.GONE));
        xDeleteSMSHelper.deleteSms(xDeleteSmsParam);
    }

    /* **** rebackStatus **** */
    private void rebackStatus(boolean isDeleteWork) {
        // 1. flag
        deletedbutton_worked = isDeleteWork;
        // 2. delete button visible or gone
        iv_delete.setVisibility(isDeleteWork ? View.VISIBLE : View.GONE);
        // 3. temp list clear
        posList.clear();
        // check box visible or gone
        for (int i = 0; i < m_smsListData.size(); i++) {
            if (i == 0) {
                continue;
            }
            View childAt = m_smsListView.getChildAt(i);
            ImageView sms_checkbox = (ImageView) childAt.findViewById(R.id.iv_smsdetail_checkbox);
            sms_checkbox.setImageResource(R.drawable.checkbox_android_off);
            sms_checkbox.setVisibility(isDeleteWork ? View.VISIBLE : View.GONE);
        }
    }

    /* **** FailedSMSClick **** */
    private void FailedSMSClick(SMSDetailItem item) {
        m_etContent.setEnabled(true);
        m_etContent.setText(item.strContent);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (visibleItemCount <= totalItemCount) {
            m_smsListView.setStackFromBottom(true);
        } else {
            m_smsListView.setStackFromBottom(false);
        }
    }

}
