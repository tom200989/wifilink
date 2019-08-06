package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_encrypt.p_encrypt.core.md5.Md5Code;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.LoginBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.LoginParam;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSystemInfoBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;
import com.p_xhelper_smart.p_xhelper_smart.utils.EncryptUtils;
import com.p_xhelper_smart.p_xhelper_smart.utils.Logg;
import com.p_xhelper_smart.p_xhelper_smart.utils.SmartUtils;

/*
 * Created by qianli.ma on 2019/7/29 0029.
 */
@SuppressWarnings("unchecked")
public class LoginHelper extends BaseHelper {

    private String account = XCons.ACCOUNT;
    private String password;
    private int count = 0;
    private int devType;// 设备类型
    private GetSystemInfoBean getSystemInfoBean;

    /**
     * 登陆
     *
     * @param account  用户名
     * @param password 密码
     */
    public void login(String account, String password) {

        this.account = account;
        this.password = password;
        count = 0;
        prepareHelperNext();
        // 判断是否为E1版本
        is_DEV_E1();
    }

    /**
     * 判断是否为E1版本
     */
    private void is_DEV_E1() {
        GetSystemInfoHelper systemInfoHelper = new GetSystemInfoHelper();
        systemInfoHelper.setOnGetSystemInfoSuccessListener(getSystemInfobean -> {
            // 0.提交本地变量
            this.getSystemInfoBean = getSystemInfobean;
            devType = SmartUtils.getDEVType(getSystemInfobean.getDeviceName());
            // 1.判断是否为E1版本
            if (devType == XCons.ENCRYPT_DEV_TARGET) {
                // 1.1.E1版本必须加密
                encryptAccAndPsd(true);
            } else {
                // 1.2.非E1版本, 判断是否有加密字段PwEncrypt
                isPwEncrypt();
            }
        });

        systemInfoHelper.setOnFwErrorListener(() -> {
            // 1.3.如果获取出错 -- 则一定是需要加密的版本
            encryptAccAndPsd(true);
        });

        systemInfoHelper.setOnAppErrorListener(() -> {
            Logg.t(XCons.TAG).ee("systemInfoHelper app error");
            loginFailedNext();
            doneHelperNext();
        });

        systemInfoHelper.getSystemInfo();
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
            // 正式发起请求
            reqLogin();
        });

        loginStateHelper.setOnGetLoginStateFailedListener(() -> {
            Logg.t(XCons.TAG).ee("loginStateHelper app error");
            loginFailedNext();
            doneHelperNext();
        });

        loginStateHelper.getLoginState();
    }

    /**
     * 正式发起请求
     */
    private void reqLogin() {
        XSmart<LoginBean> xLogin = new XSmart<>();
        xLogin.xParam(new LoginParam(account, password));
        xLogin.xMethod(XCons.METHOD_LOGIN).xPost(new XNormalCallback<LoginBean>() {
            @Override
            public void success(LoginBean loginBean) {
                isStateLogin(loginBean);// 查询登陆状态是否已经更改为［已登录］
            }

            @Override
            public void appError(Throwable ex) {
                Logg.t(XCons.TAG).ee("reqLogin app error");
                loginFailed();
            }

            @Override
            public void fwError(FwError fwError) {
                Logg.t(XCons.TAG).ee("reqLogin fwError :" + fwError.getCode());
                String code = fwError.getCode();
                if (code.equals("010101")) {// 密码不正确
                    psdNotCorrectNext();
                } else if (code.equals("010102")) {// 其他人已登录
                    otherUserLoginNext();
                } else if (code.equals("010103")) {// 设备重启
                    deviceRebootNext();
                } else if (code.equals("010104")) {// WEBUI登录
                    guestWebuiNext();
                } else {// 其他异常
                    loginFailed();
                }
            }

            @Override
            public void finish() {

            }
        });
    }

    /**
     * 查询登陆状态是否已经更改为［已登录］
     */
    private void isStateLogin(LoginBean loginBean) {
        GetLoginStateHelper loginStateHelper = new GetLoginStateHelper();
        loginStateHelper.setOnGetLoginStateSuccessListener(loginStateBean -> {
            int state = loginStateBean.getState();
            if (state == GetLoginStateBean.CONS_LOGIN) {/* 登陆成功 */
                // 查询辅助标记置零
                count = 0;
                // 更新token
                updateToken(loginBean);
                // 回调
                loginSuccessNext();
                doneHelperNext();

            } else if (state == GetLoginStateBean.CONS_LOGOUT) {/* 依然是登出状态 */
                if (count < 5) {// 小于5次 -- 则重复确认
                    try {
                        Thread.sleep(1000);
                        isStateLogin(loginBean);// 重复请求
                        count++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Logg.t(XCons.TAG).ee("isStateLogin count > 5 error");
                    loginFailed();
                }

            } else if (state == GetLoginStateBean.CONS_LOGIN_TIME_USER_OUT) {/* 登陆次数超限 */
                count = 0;
                loginOutTimeNext();
                doneHelperNext();

            }
        });

        loginStateHelper.setOnGetLoginStateFailedListener(this::loginFailed);
        loginStateHelper.getLoginState();
    }

    /**
     * 登陆失败后的处理方式
     */
    private void loginFailed() {
        count = 0;
        loginFailedNext();
        doneHelperNext();
    }

    /**
     * 更新token
     *
     * @param loginBean 登陆后返回的token
     */
    private void updateToken(LoginBean loginBean) {
        String token = loginBean.getToken();
        String key = loginBean.getParam0();
        String iv = loginBean.getParam1();
        String deviceName = getSystemInfoBean.getDeviceName();
        XSmart.token = EncryptUtils.encryptToken(token, key, iv, deviceName);
    }

    /**
     * 对帐号和用户名进行加密
     *
     * @param isEncrypt T:需要加密
     */
    private void encryptAccAndPsd(boolean isEncrypt) {
        // 账户名加密 (均为普通算法加密)
        account = isEncrypt ? EncryptUtils.encryptAdmin(account) : account;
        // 密码加密 (分为算法加密或者MD5加密)
        if (isEncrypt) {
            if (getSystemInfoBean != null) {
                // 如果不是［新设备］-- 采用普通算法加密
                if (devType != XCons.ENCRYPT_DEV_2019) {
                    password = EncryptUtils.encryptAdmin(password);
                } else {// 如果是［新设备］-- 采用MD5加密
                    password = Md5Code.encryption(password).toLowerCase();
                }
            }
        }
    }

    /* -------------------------------------------- impl -------------------------------------------- */

    private OnLoginSuccesListener onLoginSuccesListener;

    // Inteerface--> 接口OnLoginSuccesListener
    public interface OnLoginSuccesListener {
        void loginSuccess();
    }

    // 对外方式setOnLoginSuccesListener
    public void setOnLoginSuccesListener(OnLoginSuccesListener onLoginSuccesListener) {
        this.onLoginSuccesListener = onLoginSuccesListener;
    }

    // 封装方法loginSuccessNext
    private void loginSuccessNext() {
        if (onLoginSuccesListener != null) {
            onLoginSuccesListener.loginSuccess();
        }
    }

    private OnLoginFailedListener onLoginFailedListener;

    // Inteerface--> 接口OnLoginFailedListener
    public interface OnLoginFailedListener {
        void loginFailed();
    }

    // 对外方式setOnLoginFailedListener
    public void setOnLoginFailedListener(OnLoginFailedListener onLoginFailedListener) {
        this.onLoginFailedListener = onLoginFailedListener;
    }

    // 封装方法loginFailedNext
    private void loginFailedNext() {
        if (onLoginFailedListener != null) {
            onLoginFailedListener.loginFailed();
        }
    }

    private OnLoginOutTimeListener onLoginOutTimeListener;

    // Inteerface--> 接口OnLoginOutTimeListener
    public interface OnLoginOutTimeListener {
        void loginOutTime();
    }

    // 对外方式setOnLoginOutTimeListener
    public void setOnLoginOutTimeListener(OnLoginOutTimeListener onLoginOutTimeListener) {
        this.onLoginOutTimeListener = onLoginOutTimeListener;
    }

    // 封装方法loginOutTimeNext
    private void loginOutTimeNext() {
        if (onLoginOutTimeListener != null) {
            onLoginOutTimeListener.loginOutTime();
        }
    }

    private OnPsdNotCorrectListener onPsdNotCorrectListener;

    // Inteerface--> 接口OnPsdNotCorrectListener
    public interface OnPsdNotCorrectListener {
        void psdNotCorrect();
    }

    // 对外方式setOnPsdNotCorrectListener
    public void setOnPsdNotCorrectListener(OnPsdNotCorrectListener onPsdNotCorrectListener) {
        this.onPsdNotCorrectListener = onPsdNotCorrectListener;
    }

    // 封装方法psdNotCorrectNext
    private void psdNotCorrectNext() {
        if (onPsdNotCorrectListener != null) {
            onPsdNotCorrectListener.psdNotCorrect();
        }
    }

    private OnOtherUserLoginListener onOtherUserLoginListener;

    // Inteerface--> 接口OnOtherUserLoginListener
    public interface OnOtherUserLoginListener {
        void otherUserLogin();
    }

    // 对外方式setOnOtherUserLoginListener
    public void setOnOtherUserLoginListener(OnOtherUserLoginListener onOtherUserLoginListener) {
        this.onOtherUserLoginListener = onOtherUserLoginListener;
    }

    // 封装方法otherUserLoginNext
    private void otherUserLoginNext() {
        if (onOtherUserLoginListener != null) {
            onOtherUserLoginListener.otherUserLogin();
        }
    }

    private OnDeviceRebootListener onDeviceRebootListener;

    // Inteerface--> 接口OnDeviceRebootListener
    public interface OnDeviceRebootListener {
        void deviceReboot();
    }

    // 对外方式setOnDeviceRebootListener
    public void setOnDeviceRebootListener(OnDeviceRebootListener onDeviceRebootListener) {
        this.onDeviceRebootListener = onDeviceRebootListener;
    }

    // 封装方法deviceRebootNext
    private void deviceRebootNext() {
        if (onDeviceRebootListener != null) {
            onDeviceRebootListener.deviceReboot();
        }
    }

    private OnGuestWebUiListener onGuestWebUiListener;

    // Inteerface--> 接口OnGuestWebUiListener
    public interface OnGuestWebUiListener {
        void guestWebui();
    }

    // 对外方式setOnGuestWebUiListener
    public void setOnGuestWebUiListener(OnGuestWebUiListener onGuestWebUiListener) {
        this.onGuestWebUiListener = onGuestWebUiListener;
    }

    // 封装方法guestWebuiNext
    private void guestWebuiNext() {
        if (onGuestWebUiListener != null) {
            onGuestWebUiListener.guestWebui();
        }
    }
}
