package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/8/1 0001.
 */
@SuppressWarnings("unchecked")
public class SetCheckNewVersionHelper extends BaseHelper {

    /**
     * 触发新版本检测
     */
    public void setCheckNewVersion() {
        prepareHelperNext();
        XSmart xSetCheckNewVersion = new XSmart();
        xSetCheckNewVersion.xMethod(XCons.METHOD_SET_CHECK_NEW_VERSION);
        xSetCheckNewVersion.xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                SetCheckNewVersionSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                SetCheckNewVersionFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                SetCheckNewVersionFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnSetCheckNewVersionSuccessListener onSetCheckNewVersionSuccessListener;

    // Inteerface--> 接口OnSetCheckNewVersionSuccessListener
    public interface OnSetCheckNewVersionSuccessListener {
        void SetCheckNewVersionSuccess();
    }

    // 对外方式setOnSetCheckNewVersionSuccessListener
    public void setOnSetCheckNewVersionSuccessListener(OnSetCheckNewVersionSuccessListener onSetCheckNewVersionSuccessListener) {
        this.onSetCheckNewVersionSuccessListener = onSetCheckNewVersionSuccessListener;
    }

    // 封装方法SetCheckNewVersionSuccsNext
    private void SetCheckNewVersionSuccessNext() {
        if (onSetCheckNewVersionSuccessListener != null) {
            onSetCheckNewVersionSuccessListener.SetCheckNewVersionSuccess();
        }
    }

    private OnSetCheckNewVersionFailedListener onSetCheckNewVersionFailedListener;

    // Inteerface--> 接口OnSetCheckNewVersionFailedListener
    public interface OnSetCheckNewVersionFailedListener {
        void SetCheckNewVersionFailed();
    }

    // 对外方式setOnSetCheckNewVersionFailedListener
    public void setOnSetCheckNewVersionFailedListener(OnSetCheckNewVersionFailedListener onSetCheckNewVersionFailedListener) {
        this.onSetCheckNewVersionFailedListener = onSetCheckNewVersionFailedListener;
    }

    // 封装方法SetCheckNewVersionFailedNext
    private void SetCheckNewVersionFailedNext() {
        if (onSetCheckNewVersionFailedListener != null) {
            onSetCheckNewVersionFailedListener.SetCheckNewVersionFailed();
        }
    }
}
