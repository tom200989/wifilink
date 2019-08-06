package com.p_xhelper_smart.p_xhelper_smart.core;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XBackupCallback;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.impl.XRequstBody;
import com.p_xhelper_smart.p_xhelper_smart.impl.XResponceBody;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;
import com.p_xhelper_smart.p_xhelper_smart.utils.HostnameUtils;
import com.p_xhelper_smart.p_xhelper_smart.utils.Logg;
import com.p_xhelper_smart.p_xhelper_smart.utils.SmartUtils;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.http.request.UriRequest;
import org.xutils.x;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by qianli.ma on 2019/7/25 0025.
 */
public class XSmart<T> {

    // key
    public static final String AUTHORIZATION = "KSDHSDFOGQ5WERYTUIQWERTYUISDFG1HJZXCVCXBN2GDSMNDHKVKFsVBNf";
    public static final String KEY = "e5dl12XYVggihggafXWf0f2YSf2Xngd1";
    // url
    private static final String HTTP = "http://";
    private static final String JRD = "/jrd/webapi";
    private static final String BACKUP = "/cfgbak/configure.bin";
    // token
    public static String token = "0";

    // 请求方式
    private static final int GET = 0;
    private static final int POST = 1;

    /**
     * context : 必须初始化一次
     */
    private static Context context = null;

    /**
     * 请求的方法名
     */
    private String method = "";

    /**
     * 可变参数(请求提交所需)
     */
    @Nullable
    private Object object;

    /**
     * 日志打印
     */
    private Logg lgg;

    /**
     * 超时时间 (30s)
     */
    private final int TIMEOUT = 30000;

    /**
     * 是否打印框架日志 (按需求设置, T:打印)
     */
    public static boolean PRINT_TAG = true;

    /**
     * 是否打印头部信息 (T: 打印)
     */
    public static boolean PRINT_HEAD = false;

    /**
     * 是否打印进度 (T: 打印)
     */
    public static boolean PRINT_PROGRESS = false;

    /**
     * 用于存储网络请求对象
     */
    private static final List<Callback.Cancelable> cancelList = new ArrayList<>();

    /**
     * 普通请求体
     */
    private Callback.Cancelable requestCancelable;

    /**
     * 备份下载请求体
     */
    private Callback.Cancelable backupCancelable;

    private int count = 0;// 辅助递归计数器
    private int MAX_COUNT = 9;// 最大请求次数

    public XSmart() {
        lgg = Logg.t(XCons.TAG).openClose(PRINT_TAG);
    }

    /**
     * 使用前必须初始化一次
     *
     * @param cts context
     */
    public static void init(Context cts) {
        context = cts;
        HostnameUtils.setVerifyHostName(context);
    }

    /**
     * 设置请求接口方法
     *
     * @param method 接口方法
     */
    public XSmart<T> xMethod(String method) {
        this.method = method;
        return this;
    }

    public XSmart<T> xParam(@Nullable Object object) {
        this.object = object;
        return this;
    }

    /**
     * 发起请求
     */
    public void xPost(XNormalCallback<T> listener) {
        request(POST, listener);
    }

    /**
     * 备份
     *
     * @param savePath 备份路径
     * @param listener 回调
     */
    public void xBackup(String savePath, XBackupCallback listener) {
        downBackup(savePath, listener);
    }

    /**
     * 正式请求
     *
     * @param type     类型
     * @param callback 回调
     */
    private void request(int type, final XNormalCallback<T> callback) {
        // 第一步确认WIFI连接上
        if (SmartUtils.isWifiOn(context)) {
            if (!TextUtils.isEmpty(method)) {
                // 初始化context
                if (context == null) {
                    Toast.makeText(context, "请先调用 XSmart.init(context) 初始context", Toast.LENGTH_LONG).show();
                    printNormal("请先调用 XSmart.init(context) 初始context");
                    return;
                }
                // 封装请求参数
                RequestParams requstParams = getRequstParams();
                // 封装请求方法
                HttpMethod httpMethod = HttpMethod.POST;
                httpMethod = getHttpMethod(type, httpMethod);
                // 发起请求
                // 打印日志
                // 交付给接口处理返回
                requestCancelable = x.http().request(httpMethod, requstParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void responseBody(UriRequest uriRequest) {
                        printUriRequest(uriRequest);
                        callback.getUriRequest(uriRequest);
                    }

                    @Override
                    public void onSuccess(String result) {
                        // 打印日志
                        XResponceBody xResponceBody = toBean(result, callback);
                        FwError fwError = xResponceBody.getError();
                        if (fwError != null & count >= 0 & count < MAX_COUNT) {
                            request(type, callback);
                            count++;
                        } else {
                            count = 0;
                            if (fwError != null) {
                                printFwError(fwError.getCode(), fwError.getMessage());
                            } else {
                                printResponseSuccess(result);
                            }
                            // 交付给接口处理返回
                            callback.onNext(xResponceBody);
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean b) {
                        count = 0;
                        printAppError(ex);
                        callback.appError(ex);
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        count = 0;
                        printCancel(cex);
                        callback.cancel(cex);
                    }

                    @Override
                    public void onFinished() {
                        // 移除请求连接体
                        cancelList.remove(requestCancelable);
                        if (count == 0 | count == MAX_COUNT) {
                            count = 0;
                            printFinish();
                            callback.finish();
                        }
                    }
                });
                // 添加到请求体集合 -- 统一管理
                cancelList.add(requestCancelable);

            } else {
                Toast.makeText(context, "请调用 XSmart.xMethod(method) 传入需要访问的方法method", Toast.LENGTH_LONG).show();
                printNormal("请调用 XSmart.xMethod(method) 传入需要访问的方法method");
            }
        } else {
            // WIFI断线 -- 切断所有请求
            xCancelAllRequest();
            callback.wifiOff();
            printWifiState(false);
        }
    }

    /**
     * 备份操作
     *
     * @param savePath 备份路径
     * @param callback 回调
     */
    private void downBackup(String savePath, XBackupCallback callback) {
        // 第一步确保WIFI连接上
        if (SmartUtils.isWifiOn(context)) {
            // 初始化context
            if (context == null) {
                Toast.makeText(context, "请先调用 XSmart.init(context) 初始context", Toast.LENGTH_LONG).show();
                printNormal("请先调用 XSmart.init(context) 初始context");
                return;
            }
            // 封装请求参数
            RequestParams requstParams = getDownBackupParam(savePath);
            // 请求下载
            backupCancelable = x.http().get(requstParams, new Callback.ProgressCallback<File>() {

                @Override
                public void responseBody(UriRequest uriRequest) {
                    callback.getUriRequest(uriRequest);
                    printUriRequest(uriRequest);
                }

                @Override
                public void onWaiting() {
                    callback.waiting();
                    printNormal("--> wait to upload");
                }

                @Override
                public void onStarted() {
                    callback.start();
                    printNormal("--> start to upload");
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    callback.loading(total, current, isDownloading);
                    if (PRINT_PROGRESS) {
                        printNormal(" progress--> total: " + total + ";current: " + current + ";isDownloading: " + isDownloading);
                    }
                }

                @Override
                public void onSuccess(File file) {
                    callback.success(file);
                    printNormal("upload successfule, the [PATH] is : " + file.getAbsolutePath());
                }

                @Override
                public void onError(Throwable ex, boolean b) {
                    callback.appError(ex);
                    printNormal("--> down failed: " + ex.getMessage());
                }

                @Override
                public void onCancelled(CancelledException e) {
                    callback.cancel(e);
                    printNormal("--> down cancel: " + e.getMessage());
                }

                @Override
                public void onFinished() {
                    // 移除请求体
                    cancelList.remove(backupCancelable);
                    callback.finish();
                    printNormal("--> down finish");
                }
            });

            // 添加到请求体集合 -- 统一管理
            cancelList.add(backupCancelable);

        } else {
            // WIFI断线 -- 切断所有请求
            xCancelAllRequest();
            callback.wifiOff();
            printWifiState(false);
        }
    }

    /**
     * 获取下载备份的参数
     *
     * @param savePath 保存路径
     * @return 备份请求参数
     */
    private RequestParams getDownBackupParam(String savePath) {
        printNormal("prepare to backup params");
        String gateWay = SmartUtils.getWIFIGateWay(context);// 获取网关
        RequestParams params = new RequestParams(HTTP + gateWay + JRD + BACKUP);
        params.setHostnameVerifier(HostnameUtils.getVerify(context));
        params.setConnectTimeout(TIMEOUT);
        params.setReadTimeout(TIMEOUT);
        params.addHeader("_TclRequestVerificationKey", AUTHORIZATION);
        params.addHeader("_TclRequestVerificationToken", token);
        params.addHeader("Referer", HTTP + gateWay + "/");
        // 保存路径
        params.setSaveFilePath(savePath);
        printHead();
        return params;
    }

    /**
     * 转换数据为bean
     *
     * @param result 返回字符
     */
    private XResponceBody toBean(String result, XNormalCallback callback) {
        /* 注意: XNormalCallback是抽象类, 实现了XNormalListener接口, 所以要使用getGenericSuperclass的方式 */
        // 注意此处: 如果数据已经是最后一层, 则使用getclass, 如果仍有多层包裹, 则还是要用getclass().getsuperClass()
        Type genericInterfaces = callback.getClass().getSuperclass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genericInterfaces).getActualTypeArguments();
        return JSONObject.parseObject(result, params[0]);
    }

    /**
     * @return 请求参数封装
     */
    private RequestParams getRequstParams() {
        printNormal("prepare to request params");
        String gateWay = SmartUtils.getWIFIGateWay(context);// 获取网关
        RequestParams params = new RequestParams(HTTP + gateWay + JRD);
        params.setHostnameVerifier(HostnameUtils.getVerify(context));
        params.setConnectTimeout(TIMEOUT);
        params.setReadTimeout(TIMEOUT);
        params.addHeader("Content-Type", "application/json");
        params.addHeader("Accept", "application/json");
        params.addHeader("_TclRequestVerificationKey", AUTHORIZATION);
        params.addHeader("_TclRequestVerificationToken", token);
        params.addHeader("Referer", HTTP + gateWay + "/");
        params.setAsJsonContent(true);
        String json = getRequestBody();
        params.setBodyContent(json);
        printHead();
        printRequestJson(json);
        return params;
    }

    /**
     * @return 封装具体请求体
     */
    private String getRequestBody() {
        XRequstBody requstBody = new XRequstBody();
        requstBody.setMethod(method);
        requstBody.setParams(object != null ? object : new Object());
        TypeUtils.compatibleWithJavaBean = true;// 保证fastjson传递数据时保持原大小写的设置(解决全大写)
        TypeUtils.compatibleWithFieldName = true;// 保证fastjson传递数据时保持原大小写的设置(解决首字母大小)
        return JSON.toJSONString(requstBody);
    }

    /**
     * 切换请求方式类型
     *
     * @param type       类型代号
     * @param httpMethod 请求方式
     * @return 请求方式对象
     */
    private HttpMethod getHttpMethod(int type, HttpMethod httpMethod) {
        switch (type) {
            case GET:
                httpMethod = HttpMethod.GET;
                break;
            case POST:
                httpMethod = HttpMethod.POST;
                break;
        }
        return httpMethod;
    }

    /**
     * 取消全部的网络请求
     */
    public static void xCancelAllRequest() {
        for (Callback.Cancelable cancelable : cancelList) {
            if (cancelable != null) {
                cancelable.cancel();
            }
        }
    }

    /**
     * 普通打印
     *
     * @param content 备注
     */
    private void printNormal(String content) {
        lgg.ii("\n" + "-------- " + "以下是打印的内容" + " --------" + "\n" + content);
    }

    /**
     * 打印Urirequest的hashcode
     *
     * @param uriRequest Urirequest
     */
    private void printUriRequest(UriRequest uriRequest) {
        lgg.ii(uriRequest.toString());
    }

    /**
     * 打印头部请求参数
     */
    private void printHead() {
        if (PRINT_HEAD) {
            lgg.ii("Request body: \n"// 请求头信息
                           + "{\n\turl: " + HTTP + SmartUtils.getWIFIGateWay(context) + "/" + method // url
                           + "\n\tkey: " + KEY // key
                           + "\n\ttimeout: " + TIMEOUT // timeout
                           + "\n\t_TclRequestVerificationToken: " + token // access token
                           + "\n\t_TclRequestVerificationKey: " + AUTHORIZATION // authorization
                           + "\n\tReferer: " + HTTP + SmartUtils.getWIFIGateWay(context) + "/" // authorization
            );
        }
    }

    /**
     * 服务器返回成功(未进行转换)
     *
     * @param json 原文
     */
    private void printResponseSuccess(String json) {
        lgg.ii("--> url: [" + HTTP + SmartUtils.getWIFIGateWay(context) + "/" + method + "] request Successful");
        StringBuilder builder = new StringBuilder();
        builder.append("\n" + "------ response json ------" + "\n");
        json = json.replace(",", "," + "\n");
        builder.append(json);
        lgg.ii(builder.toString());
    }

    /**
     * 打印请求json
     *
     * @param json 原文
     */
    private void printRequestJson(String json) {
        if (PRINT_HEAD) {
            StringBuilder builder = new StringBuilder();
            builder.append("\n" + "------ request json ------" + "\n");
            json = json.replace(",", "," + "\n");
            builder.append(json);
            lgg.ii(builder.toString());
        }
    }

    /**
     * APP请求出错
     *
     * @param ex 错误对象
     */
    private void printAppError(Throwable ex) {
        lgg.ee("<-- url: [" + HTTP + SmartUtils.getWIFIGateWay(context) + "/" + method + "] request app Error");
        lgg.ee("<-- { error_message = " + ex.getMessage() + " }");
    }

    /**
     * 服务器返回失败
     */
    private void printFwError(String code, String message) {
        lgg.ee("<-- url: [" + HTTP + SmartUtils.getWIFIGateWay(context) + "/" + method + "] request firmware Error");
        lgg.ee("<-- { code = " + code + "; message = " + message + "; }");
    }

    /**
     * APP请求取消
     *
     * @param cex 错误对象
     */
    private void printCancel(Callback.CancelledException cex) {
        lgg.ww("<-- url: [" + HTTP + SmartUtils.getWIFIGateWay(context) + "/" + method + "] request app cancel");
        lgg.ww("<-- { cancel_message = " + cex.getMessage() + " }");
    }

    /**
     * 请求结束
     */
    private void printFinish() {
        lgg.vv("<-- url: [" + HTTP + SmartUtils.getWIFIGateWay(context) + "/" + method + "] request Finish");
    }

    /**
     * WIFI连接与否
     *
     * @param isOn T:连接
     */
    private void printWifiState(boolean isOn) {
        if (isOn) {
            lgg.ii("wifi is on");
        } else {
            lgg.ee("wifi is off");
        }
    }
}
