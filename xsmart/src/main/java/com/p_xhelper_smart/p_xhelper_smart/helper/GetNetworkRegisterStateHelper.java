package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetNetworkRegisterStateBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
public class GetNetworkRegisterStateHelper extends BaseHelper {

    /**
     * 获取网络注册状态
     */
    public void getNetworkRegisterState() {
        prepareHelperNext();
        XSmart<GetNetworkRegisterStateBean> xRegister = new XSmart<>();
        xRegister.xMethod(XCons.METHOD_GET_NETWORK_REGISTER_STATE);
        xRegister.xPost(new XNormalCallback<GetNetworkRegisterStateBean>() {
            @Override
            public void success(GetNetworkRegisterStateBean result) {
                int state = result.getRegist_state();
                if (state == GetNetworkRegisterStateBean.CONS_NOT_REGISETER) {
                    notRegisterNext();
                } else if (state == GetNetworkRegisterStateBean.CONS_REGISTTING) {
                    registtingNext();
                } else if (state == GetNetworkRegisterStateBean.CONS_REGISTER_SUCCESSFUL) {
                    registerSuccessNext();
                } else if (state == GetNetworkRegisterStateBean.CONS_REGISTRATION_FAILED) {
                    getNetworkRegisterStateFailedNext();
                }
            }

            @Override
            public void appError(Throwable ex) {
                getNetworkRegisterStateFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                getNetworkRegisterStateFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnNotRegisterListener onNotRegisterListener;

    // Inteerface--> 接口OnNotRegisterListener
    public interface OnNotRegisterListener {
        void notRegister();
    }

    // 对外方式setOnNotRegisterListener
    public void setOnNotRegisterListener(OnNotRegisterListener onNotRegisterListener) {
        this.onNotRegisterListener = onNotRegisterListener;
    }

    // 封装方法notRegisterNext
    private void notRegisterNext() {
        if (onNotRegisterListener != null) {
            onNotRegisterListener.notRegister();
        }
    }

    private OnRegisttingListener onRegisttingListener;

    // Inteerface--> 接口OnRegisttingListener
    public interface OnRegisttingListener {
        void registting();
    }

    // 对外方式setOnRegisttingListener
    public void setOnRegisttingListener(OnRegisttingListener onRegisttingListener) {
        this.onRegisttingListener = onRegisttingListener;
    }

    // 封装方法registtingNext
    private void registtingNext() {
        if (onRegisttingListener != null) {
            onRegisttingListener.registting();
        }
    }

    private OnRegisterSuccessListener onRegisterSuccessListener;

    // Inteerface--> 接口OnRegisterSuccessListener
    public interface OnRegisterSuccessListener {
        void registerSuccess();
    }

    // 对外方式setOnRegisterSuccessListener
    public void setOnRegisterSuccessListener(OnRegisterSuccessListener onRegisterSuccessListener) {
        this.onRegisterSuccessListener = onRegisterSuccessListener;
    }

    // 封装方法registerSuccessNext
    private void registerSuccessNext() {
        if (onRegisterSuccessListener != null) {
            onRegisterSuccessListener.registerSuccess();
        }
    }

    private OnGetNetworkRegisterStateFailedListener onGetNetworkRegisterStateFailedListener;

    // Inteerface--> 接口OnGetNetworkRegisterStateFailedListener
    public interface OnGetNetworkRegisterStateFailedListener {
        void getNetworkRegisterStateFailed();
    }

    // 对外方式setOnGetNetworkRegisterStateFailedListener
    public void setOnGetNetworkRegisterStateFailedListener(OnGetNetworkRegisterStateFailedListener onGetNetworkRegisterStateFailedListener) {
        this.onGetNetworkRegisterStateFailedListener = onGetNetworkRegisterStateFailedListener;
    }

    // 封装方法getNetworkRegisterStateFailedNext
    private void getNetworkRegisterStateFailedNext() {
        if (onGetNetworkRegisterStateFailedListener != null) {
            onGetNetworkRegisterStateFailedListener.getNetworkRegisterStateFailed();
        }
    }
}
