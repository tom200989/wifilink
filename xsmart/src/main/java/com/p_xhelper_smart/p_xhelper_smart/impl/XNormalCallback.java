package com.p_xhelper_smart.p_xhelper_smart.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/*
 * Created by qianli.ma on 2019/7/26 0026.
 */
public abstract class XNormalCallback<T> extends XNormalListener<XResponceBody<T>> {

    /**
     * 成功后返回
     * 注意: 不能把该方法写在XNormalListener接口里, 因为XNormalListener接受的是XResponceBody:T
     *
     * @param result 结果:注意, 这里的T是开发传递进来的bean
     */
    public abstract void success(T result);

    @Override
    public void getUriRequest(UriRequest uriRequest) {
        // 由开发自由实现
    }

    @Override
    public void cancel(Callback.CancelledException cex) {
        // 由开发自由实现
    }

    @Override
    public void wifiOff() {
        // 发送WIFI掉线信号 -- WifiShutDownBean
        EventBus.getDefault().postSticky(new WifiShutDownBean());
    }

    @Override
    public void onNext(XResponceBody<T> xResponceBody) {
        FwError fwError = xResponceBody.getError();
        if (fwError != null) {
            fwError(fwError);
        } else {
            // 1.此时T == JSONOObject
            JSONObject object = (JSONObject) xResponceBody.getResult();
            // 2.需要将JSONObject转换成字符
            String result = JSON.toJSONString(object);
            // 3.区分空响应
            if (result.equals("{}")) {
                // 3.回调null
                success(null);
            } else {
                // 3.然后把callback需要外传的泛型进行类型指定
                T t = toBean(result, this);
                // 4.最后回调
                success(t);
            }
        }
    }

    /**
     * 转换数据为bean
     *
     * @param result 返回字符
     */
    private T toBean(String result, XNormalCallback callback) {
        /* 注意: XNormalCallback是抽象类, 实现了XNormalListener接口, 所以要使用getGenericSuperclass的方式 */
        // 注意此处: 如果数据已经是最后一层, 则使用getclass, 如果仍有多层包裹, 则还是要用getclass().getsuperClass()
        Type genericInterfaces = callback.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genericInterfaces).getActualTypeArguments();
        return JSONObject.parseObject(result, params[0]);
    }

}
