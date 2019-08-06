package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetSMSStorageStateBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
public class GetSMSStorageStateHelper extends BaseHelper {

    /**
     * 获取短信存储状态
     */
    public void getSMSStorageState() {
        prepareHelperNext();
        XSmart<GetSMSStorageStateBean> xSmsStore = new XSmart<>();
        xSmsStore.xMethod(XCons.METHOD_GET_SMS_STORAGE_STATE);
        xSmsStore.xPost(new XNormalCallback<GetSMSStorageStateBean>() {
            @Override
            public void success(GetSMSStorageStateBean result) {
                getSMSStoreStateSuccessNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                getSMSStoreStateFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                getSMSStoreStateFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    /* -------------------------------------------- impl -------------------------------------------- */

    private OnGetSMSStoreStateSuccessListener onGetSMSStoreStateSuccessListener;

    // Inteerface--> 接口OnGetSMSStoreStateSuccessListener
    public interface OnGetSMSStoreStateSuccessListener {
        void getSMSStoreStateSuccess(GetSMSStorageStateBean stateBean);
    }

    // 对外方式setOnGetSMSStoreStateSuccessListener
    public void setOnGetSMSStoreStateSuccessListener(OnGetSMSStoreStateSuccessListener onGetSMSStoreStateSuccessListener) {
        this.onGetSMSStoreStateSuccessListener = onGetSMSStoreStateSuccessListener;
    }

    // 封装方法getSMSStoreStateSuccessNext
    private void getSMSStoreStateSuccessNext(GetSMSStorageStateBean stateBean) {
        if (onGetSMSStoreStateSuccessListener != null) {
            onGetSMSStoreStateSuccessListener.getSMSStoreStateSuccess(stateBean);
        }
    }

    private OnGetSMSStoreStateFailedListener onGetSMSStoreStateFailedListener;

    // Inteerface--> 接口OnGetSMSStoreStateFailedListener
    public interface OnGetSMSStoreStateFailedListener {
        void getSMSStoreStateFailed();
    }

    // 对外方式setOnGetSMSStoreStateFailedListener
    public void setOnGetSMSStoreStateFailedListener(OnGetSMSStoreStateFailedListener onGetSMSStoreStateFailedListener) {
        this.onGetSMSStoreStateFailedListener = onGetSMSStoreStateFailedListener;
    }

    // 封装方法getSMSStoreStateFailedNext
    private void getSMSStoreStateFailedNext() {
        if (onGetSMSStoreStateFailedListener != null) {
            onGetSMSStoreStateFailedListener.getSMSStoreStateFailed();
        }
    }
}
