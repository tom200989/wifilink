package com.alcatel.wifilink.root.ue.frag;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.ue.activity.SplashActivity;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.alcatel.wifilink.root.widget.HH70_LoadWidget;
import com.alcatel.wifilink.root.widget.HH70_NormalWidget;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSystemInfoBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.ChangePasswordHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetDeviceResetHelper;
import com.p_xhelper_smart.p_xhelper_smart.utils.EncryptUtils;
import com.p_xhelper_smart.p_xhelper_smart.utils.SmartUtils;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/20 0020.
 */
public class SettingAccountFrag extends BaseFrag {

    @BindView(R.id.iv_setting_account_back)
    ImageView ivBack;
    @BindView(R.id.tv_setting_account_done)
    TextView tvDone;

    @BindView(R.id.current_password)
    EditText mCurrentPassword;
    @BindView(R.id.new_password)
    EditText mNewPassword;
    @BindView(R.id.confirm_password)
    EditText mConfirmPassword;
    @BindView(R.id.password_notice)
    TextView tvNotice;

    @BindView(R.id.dg_widget_ok)
    HH70_NormalWidget wdOK;
    @BindView(R.id.wd_setting_account_load)
    HH70_LoadWidget wdLoad;

    private String account = XCons.ACCOUNT;
    private String password;
    private String currentPassword;
    private GetSystemInfoBean getSystemInfoBean;
    private int devType;// 设备类型

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_setting_account;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o, view, s);
        initClick();
    }

    private void initClick() {
        // 回退
        ivBack.setOnClickListener(v -> onBackPresss());
        // 点击了DONE
        tvDone.setOnClickListener(v -> clickDone());
        // 点击Forgot
        tvNotice.setOnClickListener(v -> showAlertPanel());
    }

    /**
     * 点击DONE
     */
    private void clickDone() {
        String currentPassword = RootUtils.getEDText(mCurrentPassword);
        String newPassword = RootUtils.getEDText(mNewPassword);
        String confirmPassword = RootUtils.getEDText(mConfirmPassword);

        if (TextUtils.isEmpty(currentPassword)) {
            toast(R.string.hh70_enter_psd, 3000);
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            toast(R.string.hh70_new_psd_not_match, 3000);
            return;
        }
        if (confirmPassword.length() < 4 || confirmPassword.length() > 16) {
            toast(R.string.hh70_the_psd_should_4, 3000);
            return;
        }

        // 检验是否匹配正则
        if (RootUtils.isMatchRule(confirmPassword)) {
            // 交付本地变量
            password = confirmPassword;
            this.currentPassword = currentPassword;
            // 去加密
            toEncrypts();
        } else {
            toast(R.string.hh70_invalid_psd, 3000);
        }

        // 隐藏键盘
        RootUtils.hideKeyBoard(activity);
    }

    /**
     * 去加密
     */
    private void toEncrypts() {
        GetSystemInfoHelper xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(getSystemInfobean -> {
            // 0.提交本地变量
            this.getSystemInfoBean = getSystemInfobean;
            devType = SmartUtils.getDEVType(getSystemInfobean.getDeviceName());
            // 1.判断是否为E1版本
            if (devType == XCons.ENCRYPT_DEV_TARGET) {
                // 1.1.E1版本必须加密
                encryptAccAndPsd(true);
                // 1.2.发起修改
                changePassword();
            } else {
                // 1.2.非E1版本, 判断是否有加密字段PwEncrypt
                isPwEncrypt();
            }
        });
        xGetSystemInfoHelper.setOnFwErrorListener(() -> encryptAccAndPsd(true));// 1.3.如果获取出错 -- 则一定是需要加密的版本
        xGetSystemInfoHelper.setOnAppErrorListener(() -> toast(R.string.hh70_cant_connect, 3000));
        xGetSystemInfoHelper.getSystemInfo();
    }

    /**
     * 提交修改
     */
    private void changePassword() {
        // 发起请求
        ChangePasswordHelper xChangePasswordHelper = new ChangePasswordHelper();
        xChangePasswordHelper.setOnChangePasswordSuccessListener(() -> {
            toast(R.string.hh70_succeed, 3000);
            toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true, true, 0);
        });
        xChangePasswordHelper.setOnChangePasswordFailedListener(() -> toast(R.string.hh70_change_psd_fail, 3000));// 失败
        xChangePasswordHelper.setOnCurrentPasswordWrongListener(() -> toast(R.string.hh70_current_psd_wrong, 3000));// 当前密码错误
        xChangePasswordHelper.setOnSameDefaultPasswordListener(() -> toast(R.string.hh70_current_psd_same, 3000));// 与默认密码相同
        xChangePasswordHelper.changePassword(currentPassword, password);
    }

    /**
     * 加密用户名和密码
     */
    private void encryptAccAndPsd(boolean isEncrypt) {
        // 账户名加密 (均为普通算法加密)
        account = isEncrypt ? EncryptUtils.encryptAdmin(account) : account;
        // 密码加密 (分为算法加密或者MD5加密)
        if (isEncrypt) {
            if (getSystemInfoBean != null) {
                //这里统一用普通加密，不需要区分md5加密或者普通加密方式了
                password = EncryptUtils.encryptAdmin(password);
                currentPassword = EncryptUtils.encryptAdmin(currentPassword);
            }
        }
    }

    /**
     * 非E1版本, 判断是否有加密字段PwEncrypt
     */
    private void isPwEncrypt() {
        GetLoginStateHelper loginStateHelper = new GetLoginStateHelper();
        loginStateHelper.setOnGetLoginStateSuccessListener(loginStateBean -> {
            // 获取PW-ENCRYPT字段: 1 -- 需要加密; 0 -- 不需要加密
            boolean isEncrypt = loginStateBean.getPwEncrypt() == GetLoginStateBean.CONS_PWENCRYPT_ON;
            // 进行加密操作
            encryptAccAndPsd(isEncrypt);
            // 1.2.发起修改
            changePassword();
        });
        loginStateHelper.setOnGetLoginStateFailedListener(() -> toast(R.string.hh70_cant_connect, 3000));
        loginStateHelper.getLoginState();
    }

    /**
     * 显示警告框
     */
    private void showAlertPanel() {
        wdOK.setVisibility(View.VISIBLE);
        wdOK.setTitle(R.string.hh70_warning);
        wdOK.setDes(R.string.hh70_forgotten_reset_router);
        wdOK.setOnCancelClickListener(() -> wdOK.setVisibility(View.GONE));
        wdOK.setOnOkClickListener(this::toReset);
    }

    /**
     * 启用重置接口
     */
    public void toReset() {
        wdLoad.setVisibles();
        SetDeviceResetHelper xSetDeviceResetHelper = new SetDeviceResetHelper();
        xSetDeviceResetHelper.setOnSetDeviceResetFailedListener(() -> wdLoad.setGone());
        xSetDeviceResetHelper.SetDeviceReset();
    }

    @Override
    public boolean onBackPresss() {
        if (wdOK.getVisibility() == View.VISIBLE) {
            wdOK.setVisibility(View.GONE);
            return true;
        } else if (wdLoad.getVisibility() == View.VISIBLE) {
            toast(R.string.hh70_data_be_submit, 3000);
            return true;
        }
        toFrag(getClass(), SettingFrag.class, null, false);
        return true;
    }
}
