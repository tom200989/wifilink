package com.alcatel.wifilink.root.ue.frag;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.SmsWatcher;
import com.alcatel.wifilink.root.utils.DataUtils;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.alcatel.wifilink.root.widget.HH70_LoadWidget;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSendSMSResultBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.SaveSmsParam;
import com.p_xhelper_smart.p_xhelper_smart.bean.SendSmsParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSendSMSResultHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SaveSMSHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SendSMSHelper;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmsNewFrag extends BaseFrag {

    @BindView(R.id.send_btn)
    Button m_btnSend;
    @BindView(R.id.edit_number)
    EditText m_etNumber;
    @BindView(R.id.edit_content)
    EditText m_etContent;
    @BindView(R.id.ib_newsms_back)
    ImageButton ivBack;
    @BindView(R.id.sms_new_waiting_progress)
    HH70_LoadWidget loadWidget;

    private static final String NUMBER_REG_Ex = "^([+*#\\d;]){1}(\\d|[;*#]){0,}$";
    private String m_preMatchNumber = "";

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_sms_new;
    }

    @Override
    public void initViewFinish(View inflateView) {
        super.initViewFinish(inflateView);
        initClick();
        m_btnSend.setEnabled(false);
        m_etNumber.setText("");
        m_etContent.setText("");
        new SmsWatcher(activity, m_etContent);
        setEditTextChangedListener();
    }

    /**
     * 初始化点击事件
     */
    public void initClick() {
        ivBack.setOnClickListener(v -> toFrag(getClass(), SmsFrag.class, null, false));
        m_btnSend.setOnClickListener(v -> {
            OnBtnSend();// 发送
            m_etContent.setText("");// 清空编辑域
        });
    }

    /**
     * 监听Edittext
     */
    private void setEditTextChangedListener() {
        m_etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String editable = RootUtils.getEDText(m_etNumber,true);
                String str = stringFilter(editable);
                if (!editable.equals(str)) {
                    m_etNumber.setText(str);
                    m_etNumber.setSelection(start);
                }

                String strNumber = str;
                String strContent = RootUtils.getEDText(m_etContent,true);
                if (strNumber != null && strNumber.length() != 0 && strContent.length() != 0) {
                    m_btnSend.setEnabled(true);
                } else {
                    m_btnSend.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        m_etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strNumber = RootUtils.getEDText(m_etNumber,true) == null ? null : RootUtils.getEDText(m_etNumber,true);
                String strContent = s == null ? null : s.toString();
                if (strNumber != null && strContent != null && strNumber.length() != 0 && strContent.length() != 0) {
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
                    String newStr = null;
                    if (strContent != null) {
                        newStr = strContent.substring(0, nMax);
                    }
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
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private String stringFilter(String str) {
        Pattern p = Pattern.compile(NUMBER_REG_Ex);
        Matcher m = p.matcher(str);
        if ((m.matches() || str == null || str.length() == 0) && checkAllNumber(str)) {
            if (str == null)
                str = "";

            m_preMatchNumber = str;
        }
        return m_preMatchNumber;
    }

    private boolean checkAllNumber(String strNumber) {
        ArrayList<String> numberLst = getNumberFromString(strNumber);
        for (int i = 0; i < numberLst.size(); i++) {
            if (numberLst.get(i).length() > 20) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        loadWidget.setVisibility(View.GONE);
        if (RootUtils.getEDText(m_etNumber,true).length() != 0 && RootUtils.getEDText(m_etContent,true).length() != 0) {
            m_btnSend.setEnabled(true);
        }
        m_etNumber.setEnabled(true);
        m_etContent.setEnabled(true);
    }

    @Override
    public boolean onBackPresss() {
        onBtnCancel();
        return true;
    }

    private void onBtnCancel() {
        String strContent = RootUtils.getEDText(m_etContent,true);
        String strNumber = RootUtils.getEDText(m_etNumber,true);
        strContent = strContent.trim();
        if (strContent.length() > 0 && strNumber.length() > 0) {
            if (checkNumbers()) {
                String num = RootUtils.getEDText(m_etNumber,true);
                ArrayList<String> numList = new ArrayList<>();
                numList.add(num);
                //用xsmart框架内部的Param代替旧的Param
                SaveSmsParam xSaveSmsParam = new SaveSmsParam();
                xSaveSmsParam.setSMSId(-1);
                xSaveSmsParam.setSMSContent(RootUtils.getEDText(m_etContent,true));
                xSaveSmsParam.setSMSTime(DataUtils.getCurrent());
                xSaveSmsParam.setPhoneNumber(numList);

                SaveSMSHelper xSaveSMSHelper = new SaveSMSHelper();
                xSaveSMSHelper.setOnSaveSMSSuccessListener(() -> {
                    toast(R.string.sms_save_success, 2000);
                    toFrag(getClass(), SmsFrag.class, null, false);
                });
                xSaveSMSHelper.setOnSaveSmsFailListener(() -> toFrag(getClass(), SmsFrag.class, null, false));
                xSaveSMSHelper.setOnSaveFailListener(() -> toast(R.string.sms_save_error, 2000));
                xSaveSMSHelper.setOnSpaceFullListener(() -> toast(R.string.sms_error_message_full_storage, 2000));
                xSaveSMSHelper.saveSms(xSaveSmsParam);
            } else {
                String msgRes = activity.getString(R.string.sms_number_invalid);
                toast(msgRes, 2000);
                m_etNumber.requestFocus();
            }
        } else {
            toFrag(getClass(), SmsFrag.class, null, false);
        }
    }

    private void OnBtnSend() {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if (checkNumbers()) {
            String num = RootUtils.getEDText(m_etNumber,true);
            ArrayList<String> numList = new ArrayList<>();
            numList.add(num);

            SendSmsParam xSendSmsParam = new SendSmsParam();
            xSendSmsParam.setSMSId(-1);
            xSendSmsParam.setSMSContent(RootUtils.getEDText(m_etContent,true));
            xSendSmsParam.setSMSTime(DataUtils.getCurrent());
            xSendSmsParam.setPhoneNumber(numList);

            SendSMSHelper xSendSMSHelper = new SendSMSHelper();
            xSendSMSHelper.setOnSendSmsSuccessListener(this::getSendSMSResult);
            xSendSMSHelper.setOnSendSmsFailListener(this::resetUI);
            xSendSMSHelper.setOnSendFailListener(() -> {
                toast(R.string.send_sms_failed, 2000);
            });
            xSendSMSHelper.setOnLastMessageListener(() -> {
                toast(R.string.fail_still_sending_last_message, 2000);
            });
            xSendSMSHelper.setOnSpaceFullListener(() -> {
                toast(R.string.fail_with_store_space_full, 2000);
            });
            xSendSMSHelper.sendSms(xSendSmsParam);

            loadWidget.setVisibility(View.VISIBLE);
            m_btnSend.setEnabled(false);
            m_etNumber.setEnabled(false);
            m_etContent.setEnabled(false);
        } else {

            String msgRes = this.getString(R.string.sms_number_invalid);
            toast(msgRes, 2000);
            m_etNumber.requestFocus();
        }
    }

    /**
     * 刷新UI
     */
    private void resetUI() {
        loadWidget.setVisibility(View.GONE);
        m_btnSend.setEnabled(true);
        m_etNumber.setEnabled(true);
        m_etContent.setEnabled(true);
    }

    /**
     * 获取短信结果
     */
    private void getSendSMSResult() {
        GetSendSMSResultHelper xGetSendSMSResultHelper = new GetSendSMSResultHelper();
        xGetSendSMSResultHelper.setOnGetSendSmsResultSuccessListener(bean -> {
            //0 : none 1 : sending 2 : sendAgainSuccess 3: fail still sending last message 4 : fail with Memory full 5: fail
            resetUI();
            int sendStatus = bean.getSendStatus();
            if (sendStatus == GetSendSMSResultBean.CONS_SEND_STATUS_NONE) {
                loadWidget.setVisibility(View.GONE);
                toast(R.string.none, 2000);
                toFrag(getClass(), SmsFrag.class, null, false);
            } else if (sendStatus == GetSendSMSResultBean.CONS_SEND_STATUS_SENDING) {
                getSendSMSResult();
            } else if (sendStatus == GetSendSMSResultBean.CONS_SEND_STATUS_SUCCESS) {
                loadWidget.setVisibility(View.GONE);
                toast(R.string.succeed, 2000);
                toFrag(getClass(), SmsFrag.class, null, false);
            } else if (sendStatus == GetSendSMSResultBean.CONS_SEND_STATUS_FAIL_LAST_MSG) {
                loadWidget.setVisibility(View.GONE);
                getSendSMSResult();
            } else if (sendStatus == GetSendSMSResultBean.CONS_SEND_STATUS_FAIL_MEMORY_FULL) {
                loadWidget.setVisibility(View.GONE);
                toast(R.string.fail_with_memory_full, 2000);
                toFrag(getClass(), SmsFrag.class, null, false);
            } else if (sendStatus == GetSendSMSResultBean.CONS_SEND_STATUS_FAIL) {
                loadWidget.setVisibility(View.GONE);
                toast(R.string.fail, 2000);
                toFrag(getClass(), SmsFrag.class, null, false);
            }
        });
        xGetSendSMSResultHelper.setOnGetSendSmsResultFailListener(() -> {
            resetUI();
        });
        xGetSendSMSResultHelper.getSendSmsResult();
    }

    /**
     * 检查号码合法性
     *
     * @return T:合法
     */
    private boolean checkNumbers() {
        String strNumber = RootUtils.getEDText(m_etNumber,true);
        ArrayList<String> numberLst = getNumberFromString(strNumber);
        return numberLst.size() <= 3;
    }

    /**
     * 获取号码数组
     *
     * @param number 一长串号码字符
     * @return 号码数组
     */
    private ArrayList<String> getNumberFromString(String number) {
        if (number == null)
            number = "";
        String[] listNumbers = number.split(";");
        ArrayList<String> phoneNumberLst = new ArrayList<String>();
        for (String listNumber : listNumbers) {
            if (null == listNumber || listNumber.length() == 0)
                continue;
            phoneNumberLst.add(listNumber);
        }

        return phoneNumberLst;
    }
}