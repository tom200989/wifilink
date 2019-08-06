package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.ChangePinStateParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
@SuppressWarnings("unchecked")
public class ChangePinStateHelper extends BaseHelper {

    /**
     * 修改PIN码状态
     *
     * @param pin   PIN码
     * @param state 待修改状态
     */
    public void changePinState(String pin, int state) {
        prepareHelperNext();
        XSmart xChangePinState = new XSmart();
        xChangePinState.xMethod(XCons.METHOD_CHANGE_PIN_STATE);
        xChangePinState.xParam(new ChangePinStateParam(pin, state));
        xChangePinState.xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                ChangePinStateSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                ChangePinStateFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {

            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnChangePinStateSuccessListener onChangePinStateSuccessListener;

    // Inteerface--> 接口OnChangePinStateSuccessListener
    public interface OnChangePinStateSuccessListener {
        void ChangePinStateSuccess();
    }

    // 对外方式setOnChangePinStateSuccessListener
    public void setOnChangePinStateSuccessListener(OnChangePinStateSuccessListener onChangePinStateSuccessListener) {
        this.onChangePinStateSuccessListener = onChangePinStateSuccessListener;
    }

    // 封装方法ChangePinStateSuccessNext
    private void ChangePinStateSuccessNext() {
        if (onChangePinStateSuccessListener != null) {
            onChangePinStateSuccessListener.ChangePinStateSuccess();
        }
    }

    private OnChangePinStateFailedListener onChangePinStateFailedListener;

    // Inteerface--> 接口OnChangePinStateFailedListener
    public interface OnChangePinStateFailedListener {
        void ChangePinStateFailed();
    }

    // 对外方式setOnChangePinStateFailedListener
    public void setOnChangePinStateFailedListener(OnChangePinStateFailedListener onChangePinStateFailedListener) {
        this.onChangePinStateFailedListener = onChangePinStateFailedListener;
    }

    // 封装方法ChangePinStateFailedNext
    private void ChangePinStateFailedNext() {
        if (onChangePinStateFailedListener != null) {
            onChangePinStateFailedListener.ChangePinStateFailed();
        }
    }

}
