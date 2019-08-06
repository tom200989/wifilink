package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.ChangePasswordParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
@SuppressWarnings("unchecked")
public class ChangePasswordHelper extends BaseHelper {

    /**
     * 修改密码
     *
     * @param CurrPassword 当前密码
     * @param newPassword  新密码
     */
    public void changePassword(String CurrPassword, String newPassword) {
        prepareHelperNext();
        XSmart xChange = new XSmart();
        xChange.xMethod(XCons.METHOD_CHANGE_PASSWORD);
        xChange.xParam(new ChangePasswordParam(XCons.ACCOUNT, CurrPassword, newPassword));
        xChange.xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                changePasswordSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                changePasswordFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                String code = fwError.getCode();
                if (code.contains("010401")) {// 修改失败
                    changePasswordFailedNext();
                } else if (code.contains("010402")) {// 当前密码错误
                    currentPasswordWrongNext();
                } else if (code.contains("010403")) {// 与默认密码相同
                    sameDefaultPasswordNext();
                }
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    /* -------------------------------------------- impl -------------------------------------------- */

    private OnChangePasswordSuccessListener onChangePasswordSuccessListener;

    // Inteerface--> 接口OnChangePasswordSuccessListener
    public interface OnChangePasswordSuccessListener {
        void changePasswordSuccess();
    }

    // 对外方式setOnChangePasswordSuccessListener
    public void setOnChangePasswordSuccessListener(OnChangePasswordSuccessListener onChangePasswordSuccessListener) {
        this.onChangePasswordSuccessListener = onChangePasswordSuccessListener;
    }

    // 封装方法changePasswordSuccessNext
    private void changePasswordSuccessNext() {
        if (onChangePasswordSuccessListener != null) {
            onChangePasswordSuccessListener.changePasswordSuccess();
        }
    }

    private OnChangePasswordFailedListener onChangePasswordFailedListener;

    // Inteerface--> 接口OnChangePasswordFailedListener
    public interface OnChangePasswordFailedListener {
        void changePasswordFailed();
    }

    // 对外方式setOnChangePasswordFailedListener
    public void setOnChangePasswordFailedListener(OnChangePasswordFailedListener onChangePasswordFailedListener) {
        this.onChangePasswordFailedListener = onChangePasswordFailedListener;
    }

    // 封装方法changePasswordFailedNext
    private void changePasswordFailedNext() {
        if (onChangePasswordFailedListener != null) {
            onChangePasswordFailedListener.changePasswordFailed();
        }
    }

    private OnCurrentPasswordWrongListener onCurrentPasswordWrongListener;

    // Inteerface--> 接口OnCurrentPasswordWrongListener
    public interface OnCurrentPasswordWrongListener {
        void currentPasswordWrong();
    }

    // 对外方式setOnCurrentPasswordWrongListener
    public void setOnCurrentPasswordWrongListener(OnCurrentPasswordWrongListener onCurrentPasswordWrongListener) {
        this.onCurrentPasswordWrongListener = onCurrentPasswordWrongListener;
    }

    // 封装方法currentPasswordWrongNext
    private void currentPasswordWrongNext() {
        if (onCurrentPasswordWrongListener != null) {
            onCurrentPasswordWrongListener.currentPasswordWrong();
        }
    }

    private OnSameDefaultPasswordListener onSameDefaultPasswordListener;

    // Inteerface--> 接口OnSameDefaultPasswordListener
    public interface OnSameDefaultPasswordListener {
        void sameDefaultPassword();
    }

    // 对外方式setOnSameDefaultPasswordListener
    public void setOnSameDefaultPasswordListener(OnSameDefaultPasswordListener onSameDefaultPasswordListener) {
        this.onSameDefaultPasswordListener = onSameDefaultPasswordListener;
    }

    // 封装方法sameDefaultPasswordNext
    private void sameDefaultPasswordNext() {
        if (onSameDefaultPasswordListener != null) {
            onSameDefaultPasswordListener.sameDefaultPassword();
        }
    }
}
