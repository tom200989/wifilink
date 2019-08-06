package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.ChangePinCodeParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
@SuppressWarnings("unchecked")
public class ChangePinCodeHelper extends BaseHelper {

    public void changePinCode(String newPin, String currentPin) {
        prepareHelperNext();
        XSmart xChangePin = new XSmart();
        xChangePin.xMethod(XCons.METHOD_CHANGE_PIN_CODE);
        xChangePin.xParam(new ChangePinCodeParam(newPin, currentPin));
        xChangePin.xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                ChangePinCodeSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                ChangePinCodeFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                ChangePinCodeFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnChangePinCodeSuccessListener onChangePinCodeSuccessListener;

    // Inteerface--> 接口OnChangePinCodeSuccessListener
    public interface OnChangePinCodeSuccessListener {
        void ChangePinCodeSuccess();
    }

    // 对外方式setOnChangePinCodeSuccessListener
    public void setOnChangePinCodeSuccessListener(OnChangePinCodeSuccessListener onChangePinCodeSuccessListener) {
        this.onChangePinCodeSuccessListener = onChangePinCodeSuccessListener;
    }

    // 封装方法ChangePinCodeSuccessNext
    private void ChangePinCodeSuccessNext() {
        if (onChangePinCodeSuccessListener != null) {
            onChangePinCodeSuccessListener.ChangePinCodeSuccess();
        }
    }

    private OnChangePinCodeFailedListener onChangePinCodeFailedListener;

    // Inteerface--> 接口OnChangePinCodeFailedListener
    public interface OnChangePinCodeFailedListener {
        void ChangePinCodeFailed();
    }

    // 对外方式setOnChangePinCodeFailedListener
    public void setOnChangePinCodeFailedListener(OnChangePinCodeFailedListener onChangePinCodeFailedListener) {
        this.onChangePinCodeFailedListener = onChangePinCodeFailedListener;
    }

    // 封装方法ChangePinCodeFailedNext
    private void ChangePinCodeFailedNext() {
        if (onChangePinCodeFailedListener != null) {
            onChangePinCodeFailedListener.ChangePinCodeFailed();
        }
    }
}
