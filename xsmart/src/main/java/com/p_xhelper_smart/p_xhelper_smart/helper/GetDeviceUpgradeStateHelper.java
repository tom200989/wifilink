package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetDeviceUpgradeStateBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/8/1 0001.
 */
public class GetDeviceUpgradeStateHelper extends BaseHelper {

    /**
     * 获取升级状态和进度
     */
    public void getDeviceUpgradeState() {
        prepareHelperNext();
        XSmart<GetDeviceUpgradeStateBean> xUpgradeStart = new XSmart<>();
        xUpgradeStart.xMethod(XCons.METHOD_GET_DEVICE_UPGRADE_STATE);
        xUpgradeStart.xPost(new XNormalCallback<GetDeviceUpgradeStateBean>() {
            @Override
            public void success(GetDeviceUpgradeStateBean result) {
                GetDeviceUpgradeStateSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                GetDeviceUpgradeStateFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                GetDeviceUpgradeStateFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnGetDeviceUpgradeStateSuccessListener onGetDeviceUpgradeStateSuccessListener;

    // Inteerface--> 接口OnGetDeviceUpgradeStateSuccessListener
    public interface OnGetDeviceUpgradeStateSuccessListener {
        void GetDeviceUpgradeStateSuccess(GetDeviceUpgradeStateBean attr);
    }

    // 对外方式setOnGetDeviceUpgradeStateSuccessListener
    public void setOnGetDeviceUpgradeStateSuccessListener(OnGetDeviceUpgradeStateSuccessListener onGetDeviceUpgradeStateSuccessListener) {
        this.onGetDeviceUpgradeStateSuccessListener = onGetDeviceUpgradeStateSuccessListener;
    }

    // 封装方法GetDeviceUpgradeStateSuccessNext
    private void GetDeviceUpgradeStateSuccessNext(GetDeviceUpgradeStateBean attr) {
        if (onGetDeviceUpgradeStateSuccessListener != null) {
            onGetDeviceUpgradeStateSuccessListener.GetDeviceUpgradeStateSuccess(attr);
        }
    }

    private OnGetDeviceUpgradeStateFailedListener onGetDeviceUpgradeStateFailedListener;

    // Inteerface--> 接口OnGetDeviceUpgradeStateFailedListener
    public interface OnGetDeviceUpgradeStateFailedListener {
        void GetDeviceUpgradeStateFailed();
    }

    // 对外方式setOnGetDeviceUpgradeStateFailedListener
    public void setOnGetDeviceUpgradeStateFailedListener(OnGetDeviceUpgradeStateFailedListener onGetDeviceUpgradeStateFailedListener) {
        this.onGetDeviceUpgradeStateFailedListener = onGetDeviceUpgradeStateFailedListener;
    }

    // 封装方法GetDeviceUpgradeStateFailedNext
    private void GetDeviceUpgradeStateFailedNext() {
        if (onGetDeviceUpgradeStateFailedListener != null) {
            onGetDeviceUpgradeStateFailedListener.GetDeviceUpgradeStateFailed();
        }
    }
}
