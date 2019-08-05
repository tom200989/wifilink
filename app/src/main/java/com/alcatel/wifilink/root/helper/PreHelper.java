package com.alcatel.wifilink.root.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.System_SystemStates;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianli.ma on 2018/2/26 0026.
 */

public class PreHelper {
    private Context context;
    private List<Drawable> signals;// 信号强度集合

    public PreHelper(Context context) {
        this.context = context;
        initRes();
    }

    private void initRes() {
        signals = new ArrayList<>();
        signals.add(context.getResources().getDrawable(R.drawable.mw_signal_0));
        signals.add(context.getResources().getDrawable(R.drawable.mw_signal_1));
        signals.add(context.getResources().getDrawable(R.drawable.mw_signal_2));
        signals.add(context.getResources().getDrawable(R.drawable.mw_signal_3));
        signals.add(context.getResources().getDrawable(R.drawable.mw_signal_4));
        signals.add(context.getResources().getDrawable(R.drawable.mw_signal_4));
    }

    /**
     * 获取网络类型
     *
     * @param level
     * @return
     */
    public String getMobileType(int level) {
        String text = context.getString(R.string.home_no_service);
        if (level <= 0) {
            return text;
        } else if (level >= 1 & level <= 2) {
            return context.getString(R.string.home_network_type_2g);
        } else if (level >= 3 & level <= 7) {
            return context.getString(R.string.home_network_type_3g_plus);
        } else if (level >= 8 & level <= 9) {
            return context.getString(R.string.home_network_type_4g);
        } else if (level >= 10 & level <= 12) {
            return context.getString(R.string.home_network_type_2g);
        }
        return text;
    }

    /**
     * 获取强度图片
     *
     * @param level
     * @return
     */
    public Drawable getStrenght(int level) {
        return signals.get(level <= 0 ? 0 : level);
    }

    public void get() {
        RX.getInstant().getSystemStates(new ResponseObject<System_SystemStates>() {
            @Override
            protected void onSuccess(System_SystemStates result) {
                successNext(result);
            }

            @Override
            public void onError(Throwable e) {
                errorNextNext(e);
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                resultErrorNextNext(error);
            }
        });
    }

    public void getConnecteds( ) {
        
    }

    private OnSuccessListener onSuccessListener;

    // 接口OnSuccessListener
    public interface OnSuccessListener {
        void success(System_SystemStates attr);
    }

    // 对外方式setOnSuccessListener
    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    // 封装方法successNext
    private void successNext(System_SystemStates attr) {
        if (onSuccessListener != null) {
            onSuccessListener.success(attr);
        }
    }

    private OnErrorListener onErrorListener;

    // 接口OnErrorListener
    public interface OnErrorListener {
        void errorNext(Throwable attr);
    }

    // 对外方式setOnErrorListener
    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    // 封装方法errorNextNext
    private void errorNextNext(Throwable attr) {
        if (onErrorListener != null) {
            onErrorListener.errorNext(attr);
        }
    }

    private OnResultErrorListener onResultErrorListener;

    // 接口OnResultErrorListener
    public interface OnResultErrorListener {
        void resultErrorNext(ResponseBody.Error attr);
    }

    // 对外方式setOnResultErrorListener
    public void setOnResultErrorListener(OnResultErrorListener onResultErrorListener) {
        this.onResultErrorListener = onResultErrorListener;
    }

    // 封装方法resultErrorNextNext
    private void resultErrorNextNext(ResponseBody.Error attr) {
        if (onResultErrorListener != null) {
            onResultErrorListener.resultErrorNext(attr);
        }
    }
}
