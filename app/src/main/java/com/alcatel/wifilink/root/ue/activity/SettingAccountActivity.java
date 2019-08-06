package com.alcatel.wifilink.root.ue.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.ResetHelper;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.Lgg;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.alcatel.wifilink.root.widget.DialogOkWidget;
import com.alcatel.wifilink.root.widget.WaitWidget;
import com.p_xhelper_smart.p_xhelper_smart.helper.ChangePasswordHelper;
import com.p_xhelper_smart.p_xhelper_smart.utils.EncryptUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingAccountActivity extends BaseActivityWithBack implements OnClickListener {

    public static final String LOGOUT_FLAG = "LogoutFlag";
    public static final String TAG = "SettingAccountActivity";
    private EditText mCurrentPassword;
    private EditText mNewPassword;
    private EditText mConfirmPassword;
    private DialogOkWidget dgWidgetOk;
    private WaitWidget dgWidgetWait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account);
        setTitle(R.string.ergo_20181010_login_password);
        initUi();
    }

    private void initUi() {
        mCurrentPassword = (EditText) findViewById(R.id.current_password);
        mNewPassword = (EditText) findViewById(R.id.new_password);
        mConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        findViewById(R.id.password_notice).setOnClickListener(this);
        dgWidgetOk = (DialogOkWidget) findViewById(R.id.dg_widget_ok);
        dgWidgetWait = (WaitWidget) findViewById(R.id.dg_widget_wait);
    }

    private void doneChangePassword() {
        // 1.getInstant the psd info
        String currentPwd = mCurrentPassword.getText().toString();
        String newPwd = mNewPassword.getText().toString();
        String confirmPwd = mConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(currentPwd)) {
            Toast.makeText(this, getString(R.string.input_current_password), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPwd.equals(confirmPwd)) {
            Toast.makeText(this, getString(R.string.inconsistent_new_password), Toast.LENGTH_SHORT).show();
            return;
        }
        if (confirmPwd.length() < 4 || confirmPwd.length() > 16) {
            Toast.makeText(this, getString(R.string.change_passowrd_invalid_password), Toast.LENGTH_SHORT).show();
            return;
        }

        OtherUtils otherUtils = new OtherUtils();
        otherUtils.setOnCustomizedVersionListener(isCustomized -> {
            // isCustomized: true--> 则为定制版
            if (isCustomized) {
                // setting the direct condition psd
                String splChrs = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\\-\\+\\!\\^\\$\\@\\#\\&\\*])[A-Za-z0-9\\-\\+\\!\\^\\$\\@\\#\\&\\*]{4,16}$";
                Pattern pattern = Pattern.compile(splChrs);
                Matcher matcher = pattern.matcher(confirmPwd);
                if (matcher.find()) {
                    // 是否需要加密
                    otherUtils.setOnSwVersionListener(needToEncrypt -> changePsd(needToEncrypt, "admin", currentPwd, confirmPwd));
                    otherUtils.getDeviceSwVersion();
                } else {
                    Toast.makeText(SettingAccountActivity.this, getString(R.string.login_invalid_password), Toast.LENGTH_SHORT).show();
                }
            } else {
                // 是否需要加密
                otherUtils.setOnSwVersionListener(needToEncrypt -> changePsd(needToEncrypt, "admin", currentPwd, confirmPwd));
                otherUtils.getDeviceSwVersion();
            }
        });
        otherUtils.isCustomVersion();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mCurrentPassword.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mNewPassword.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mConfirmPassword.getWindowToken(), 0);
    }

    private void showDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(str);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, null);
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        int nID = v.getId();
        switch (nID) {
            case R.id.password_notice:
                showAlertDialog();
                break;
        }
    }

    /**
     * 显示对话框
     */
    private void showAlertDialog() {
        dgWidgetOk.setVisibility(View.VISIBLE);
        dgWidgetOk.setTitle(R.string.warning);
        dgWidgetOk.setDes(R.string.ergo_20181010_forgot_password);
        dgWidgetOk.setOnBgClickListener(() -> Lgg.t(TAG).ii("click not area"));
        dgWidgetOk.setOnCancelClickListener(() -> dgWidgetOk.setVisibility(View.GONE));
        dgWidgetOk.setOnOkClickListener(this::toReset);

    }

    /**
     * 启用重置接口
     */
    public void toReset() {
        dgWidgetWait.setVisibility(View.VISIBLE);
        ResetHelper resetHelper = new ResetHelper();
        resetHelper.setOnResultErrorListener(error -> {
            Lgg.t(TAG).ee("Method--> " + getClass().getSimpleName() + ":toReset()--> " + error.getMessage());
            dgWidgetWait.setVisibility(View.GONE);
        });
        resetHelper.setOnFailedListener(() -> {
            Lgg.t(TAG).ee("Method--> " + getClass().getSimpleName() + ":toReset() failed");
            dgWidgetWait.setVisibility(View.GONE);
        });
        resetHelper.setOnErrorListener(throwable -> {
            Lgg.t(TAG).ee("Method--> " + getClass().getSimpleName() + ":toReset() --> " + throwable.getMessage());
            dgWidgetWait.setVisibility(View.GONE);
        });
        resetHelper.reset();
    }

    @Override
    public void onBackPressed() {
        if (dgWidgetOk.getVisibility() == View.VISIBLE) {
            dgWidgetOk.setVisibility(View.GONE);
        } else if (dgWidgetWait.getVisibility() == View.VISIBLE) {
            // do nothing
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            doneChangePassword();
        }
        return super.onOptionsItemSelected(item);
    }

    public void changePsd(boolean needEncrypt, String UserName, String CurrentPassword, String NewPassword) {
        
        UserName = needEncrypt ? EncryptUtils.encryptAdmin(UserName) : UserName;
        CurrentPassword = needEncrypt ? EncryptUtils.encryptAdmin(CurrentPassword) : CurrentPassword;
        NewPassword = needEncrypt ? EncryptUtils.encryptAdmin(NewPassword) : NewPassword;
        // 发起请求
        ChangePasswordHelper xChangePasswordHelper = new ChangePasswordHelper();
        xChangePasswordHelper.setOnChangePasswordSuccessListener(() -> {// 成功
            Toast.makeText(SettingAccountActivity.this, R.string.succeed, Toast.LENGTH_SHORT).show();
            finish();
        });
        xChangePasswordHelper.setOnChangePasswordFailedListener(() -> toast(R.string.change_password_failed));// 失败
        xChangePasswordHelper.setOnCurrentPasswordWrongListener(() -> toast(R.string.the_current_password_is_wrong));// 当前密码错误
        xChangePasswordHelper.setOnSameDefaultPasswordListener(() -> toast(R.string.the_current_password_is_the_same_as_default_password));// 与默认密码相同
        xChangePasswordHelper.changePassword(CurrentPassword, NewPassword);
    }

    private void toast(int resId) {
        ToastUtil_m.show(this, resId);
    }

    private void toastLong(int resId) {
        ToastUtil_m.showLong(this, resId);
    }

    private void toast(String content) {
        ToastUtil_m.show(this, content);
    }

    private void to(Class ac, boolean isFinish) {
        CA.toActivity(this, ac, false, isFinish, false, 0);
    }
}
