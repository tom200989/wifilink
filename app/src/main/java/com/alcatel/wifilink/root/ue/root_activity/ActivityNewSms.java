package com.alcatel.wifilink.root.ue.root_activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.C_ENUM.SendStatus;
import com.alcatel.wifilink.root.helper.SmsWatcher;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.utils.ActionbarSetting;
import com.alcatel.wifilink.root.utils.DataUtils;
import com.alcatel.wifilink.root.utils.ProgressUtils;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.p_xhelper_smart.p_xhelper_smart.bean.SaveSmsParam;
import com.p_xhelper_smart.p_xhelper_smart.bean.SendSmsParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSendSMSResultHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SaveSMSHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SendSMSHelper;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityNewSms extends BaseActivityWithBack implements OnClickListener {
    private Button m_btnSend = null;
    private EditText m_etNumber = null;
    private EditText m_etContent = null;
    private static final String NUMBER_REG_Ex = "^([+*#\\d;]){1}(\\d|[;*#]){0,}$";
    private String m_preMatchNumber = "";
    private ProgressBar m_progressWaiting = null;
    private SendStatus m_sendStatus = SendStatus.None;
    private boolean m_bSendEnd = false;
    private ActionbarSetting actionbarSetting;
    private ActionBar actionBar;
    private ProgressDialog pd;
    private ImageButton ib_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_new_view);
        getWindow().setBackgroundDrawable(null);
        initActionbar();
        m_btnSend = findViewById(R.id.send_btn);
        m_btnSend.setEnabled(false);
        m_btnSend.setOnClickListener(this);
        m_etNumber = findViewById(R.id.edit_number);
        m_etNumber.setText("");
        m_etContent = findViewById(R.id.edit_content);
        new SmsWatcher(this, m_etContent);
        m_etContent.setText("");
        setEditTextChangedListener();

        m_progressWaiting = this.findViewById(R.id.sms_new_waiting_progress);
    }

    private void initActionbar() {
        actionBar = getSupportActionBar();
        actionbarSetting = new ActionbarSetting() {
            @Override
            protected void findActionbarView(View view) {
                ib_back = view.findViewById(R.id.ib_newsms_back);
                ib_back.setOnClickListener(ActivityNewSms.this);
            }
        };
        actionbarSetting.settingActionbarAttr(this, actionBar, R.layout.actionbar_newsms);
    }

    private void setEditTextChangedListener() {
        m_etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String editable = m_etNumber.getText().toString();
                String str = stringFilter(editable);
                if (!editable.equals(str)) {
                    m_etNumber.setText(str);
                    m_etNumber.setSelection(start);
                }

                String strNumber = str;
                String strContent = m_etContent.getText().toString();
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
                String strNumber = m_etNumber.getText() == null ? null : m_etNumber.getText().toString();
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPause() {
        super.onPause();
        m_progressWaiting.setVisibility(View.GONE);
        m_etNumber.getText().toString();
        if (m_etNumber.getText().toString().length() != 0 && m_etContent.getText().toString().length() != 0) {
            m_btnSend.setEnabled(true);
        }
        m_etNumber.setEnabled(true);
        m_etContent.setEnabled(true);
        m_bSendEnd = false;
    }

    public void onClick(View arg0) {
        switch (arg0.getId()) {

            case R.id.ib_newsms_back:
                finish();
                break;
            case R.id.send_btn:/* 发送短信 */
                showSendingDialog();
                OnBtnSend();// 发送
                m_etContent.setText("");// 清空编辑域
                break;
        }
    }

    /* **** showSendingDialog **** */
    private void showSendingDialog() {
        pd = new ProgressUtils(this).getProgressPop(getString(R.string.sms_sending));
    }

    @Override
    public void onBackPressed() {
        OnBtnCancel();
    }

    private void OnBtnCancel() {
        String strContent = m_etContent.getText().toString();
        String strNumber = m_etNumber.getText().toString();
        strContent = strContent.trim();
        if (strContent.length() > 0 && strNumber.length() > 0) {
            if (checkNumbers()) {
                String num = m_etNumber.getText().toString();
                ArrayList<String> numList = new ArrayList<>();
                numList.add(num);
                //用xsmart框架内部的Param代替旧的Param
                SaveSmsParam xSaveSmsParam = new SaveSmsParam();
                xSaveSmsParam.setSMSId(-1);
                xSaveSmsParam.setSMSContent(m_etContent.getText().toString());
                xSaveSmsParam.setSMSTime(DataUtils.getCurrent());
                xSaveSmsParam.setPhoneNumber(numList);

                SaveSMSHelper xSaveSMSHelper = new SaveSMSHelper();
                xSaveSMSHelper.setOnSaveSMSSuccessListener(() -> {
                    ToastUtil_m.show(ActivityNewSms.this, getString(R.string.sms_save_success));
                    finish();
                });
                xSaveSMSHelper.setOnSaveSmsFailListener(this::finish);
                xSaveSMSHelper.setOnSaveFailListener(() -> ToastUtil_m.show(ActivityNewSms.this, getString(R.string.sms_save_error)));
                xSaveSMSHelper.setOnSpaceFullListener(() -> ToastUtil_m.show(ActivityNewSms.this, getString(R.string.sms_error_message_full_storage)));
                xSaveSMSHelper.saveSms(xSaveSmsParam);
            } else {
                String msgRes = this.getString(R.string.sms_number_invalid);
                Toast.makeText(this, msgRes, Toast.LENGTH_SHORT).show();
                m_etNumber.requestFocus();
            }
        } else {
            this.finish();
        }
    }

    private void OnBtnSend() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if (checkNumbers()) {

            String num = m_etNumber.getText().toString();
            ArrayList<String> numList = new ArrayList<>();
            numList.add(num);

            SendSmsParam xSendSmsParam = new SendSmsParam();
            xSendSmsParam.setSMSId(-1);
            xSendSmsParam.setSMSContent(m_etContent.getText().toString());
            xSendSmsParam.setSMSTime(DataUtils.getCurrent());
            xSendSmsParam.setPhoneNumber(numList);

            SendSMSHelper xSendSMSHelper = new SendSMSHelper();
            xSendSMSHelper.setOnSendSmsSuccessListener(this::getSendSMSResult);
            xSendSMSHelper.setOnSendSmsFailListener(this::resetUI);
            xSendSMSHelper.setOnSendFailListener(() -> {
                ToastUtil_m.show(ActivityNewSms.this, getString(R.string.send_sms_failed));
            });
            xSendSMSHelper.setOnLastMessageListener(() -> {
                ToastUtil_m.show(ActivityNewSms.this, getString(R.string.fail_still_sending_last_message));
            });
            xSendSMSHelper.setOnSpaceFullListener(() -> {
                ToastUtil_m.show(ActivityNewSms.this, getString(R.string.fail_with_store_space_full));
            });
            xSendSMSHelper.sendSms(xSendSmsParam);

            m_progressWaiting.setVisibility(View.VISIBLE);
            m_btnSend.setEnabled(false);
            m_etNumber.setEnabled(false);
            m_etContent.setEnabled(false);
        } else {

            String msgRes = this.getString(R.string.sms_number_invalid);
            Toast.makeText(this, msgRes, Toast.LENGTH_SHORT).show();
            m_etNumber.requestFocus();
        }
    }

    /**
     * 刷新UI
     */
    private void resetUI() {
        m_progressWaiting.setVisibility(View.GONE);
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
            if (sendStatus == Cons.NONE) {
                m_progressWaiting.setVisibility(View.GONE);
                pd.dismiss();
                ToastUtil_m.show(ActivityNewSms.this, getString(R.string.none));
                finish();
            } else if (sendStatus == Cons.SENDING) {
                getSendSMSResult();
            } else if (sendStatus == Cons.SUCCESS) {
                pd.dismiss();
                m_progressWaiting.setVisibility(View.GONE);
                ToastUtil_m.show(ActivityNewSms.this, getString(R.string.succeed));
                finish();
            } else if (sendStatus == Cons.FAIL_STILL_SENDING_LAST_MSG) {
                m_progressWaiting.setVisibility(View.GONE);
                getSendSMSResult();
            } else if (sendStatus == Cons.FAIL_WITH_MEMORY_FULL) {
                pd.dismiss();
                m_progressWaiting.setVisibility(View.GONE);
                ToastUtil_m.show(ActivityNewSms.this, getString(R.string.fail_with_memory_full));
                finish();
            } else if (sendStatus == Cons.FAIL) {
                pd.dismiss();
                m_progressWaiting.setVisibility(View.GONE);
                ToastUtil_m.show(ActivityNewSms.this, getString(R.string.fail));
                finish();
            }
        });
        xGetSendSMSResultHelper.setOnGetSendSmsResultFailListener(() -> {
            resetUI();
            if (pd != null) {
                pd.dismiss();
            }
        });
        xGetSendSMSResultHelper.getSendSmsResult();
    }

    /**
     * 检查号码合法性
     *
     * @return T:合法
     */
    private boolean checkNumbers() {
        String strNumber = m_etNumber.getText().toString();
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
