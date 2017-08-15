package com.ok.zzh.okhttplibrary;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.ok.zzh.okhttplibrary.builder.builderextend.GetBuilder;
import com.ok.zzh.okhttplibrary.builder.builderextend.PostBuilder;
import com.ok.zzh.okhttplibrary.callback.CallBack;
import com.ok.zzh.okhttplibrary.cookie.CookieJarImpl;
import com.ok.zzh.okhttplibrary.cookie.store.MemoryCookieStore;
import com.ok.zzh.okhttplibrary.interceptor.LoggerInterceptor;
import com.ok.zzh.okhttplibrary.request.RequestCall;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/8/14.
 */

public class OkHttpHelper {
    public static String TAG = "OkHttpHelper";

    /**
     * 默认联网时候 链接 , 写出 , 读取 的超时时间.
     */
    public static final long DEFAULT_TIME_OUT_MILLISECONDS = 10_000L;

    private static OkHttpHelper mInstance;

    private Handler mDelivery;              //用于在主线程执行的调度器
    private OkHttpClient mOkHttpClient;      //ok请求的客户端

    /**
     * 过滤重复请求(正在飞的请求集合)，保存当前正在消息队列中执行的RequestCall，其中key为requestCode.
     */
    private static final HashMap<Integer, RequestCall> mInFlightRequests = new HashMap<>();


    private OkHttpHelper() {
        if (mOkHttpClient == null) {
            mOkHttpClient = initOkHttpClient();
        }
        if (mDelivery == null) {
            mDelivery = new Handler(Looper.getMainLooper());
        }
    }

    /**
     * 初始化一个okhttp的实例
     *
     * @return
     */
    private OkHttpClient initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));
        builder.readTimeout(DEFAULT_TIME_OUT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.writeTimeout(DEFAULT_TIME_OUT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.connectTimeout(DEFAULT_TIME_OUT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.addInterceptor(LoggerInterceptor.getLoggingIntercaptor());
        return builder.build();
    }

    /**
     * 获取当前的 okhttpclient
     *
     * @return
     */
    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 获取主线程的调用线程
     *
     * @return
     */
    public Handler getDelivery() {
        return mDelivery;
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static OkHttpHelper getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpHelper.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpHelper();
                }
            }
        }
        return mInstance;
    }

    /**
     * 一个get 方法
     */
    public static GetBuilder get() {
        return new GetBuilder();
    }

    /**
     * 普通的post方法,主要用于form标签的提交
     *
     * @return
     */
    public static PostBuilder post() {
        return new PostBuilder();
    }

    /**
     * post上传文件的方法
     *
     * @return
     */
    public static PostBuilder postFiles() {
        return new PostBuilder();
    }

    public void execute(final RequestCall requestCall, CallBack callback) {
        final int requestCode = requestCall.getOkHttpRequest().getRequestCode();

        RequestCall request = mInFlightRequests.get(requestCode);
        if (request != null) {
            Log.e(TAG, "Hi guy,the request (RequestCode is " + requestCode + ")  is already anim_fade_in-flight , So Ignore!");
            return;
        }

        mInFlightRequests.put(requestCode, requestCall);

        if (callback == null)
            callback = CallBack.DEFAULT_CALLBACK;
        final CallBack finalCallback = callback;
        requestCall.getCall().enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                setFilureCallback(call, e, finalCallback, requestCode);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (call.isCanceled()) {
                        setFilureCallback(call, new IOException("Canceled!"), finalCallback, requestCode);
                        return;
                    }
                    if (!finalCallback.validateReponse(response, requestCode)) {
                        setFilureCallback(call,
                                new IOException("request failed , reponse's code is : " +
                                        response.code()), finalCallback, requestCode);
                        return;
                    }
                    Object o = finalCallback.parseNetworkResponse(response, requestCode);
                    setSuccessCallback(o, finalCallback, requestCode);
                } catch (Exception e) {
                    setFilureCallback(call, e, finalCallback, requestCode);
                } finally {
                    if (response.body() != null)
                        response.body().close();
                }
            }
        });
    }

    private void setSuccessCallback(final Object o, final CallBack callback, final int requestCode) {
        if (callback == null) {
            return;
        }
        getDelivery().post(new Runnable() {
            @Override
            public void run() {
                mInFlightRequests.remove(requestCode);
                callback.onResponse(o, requestCode);
                callback.onAfter(requestCode);
            }
        });
    }

    private void setFilureCallback(final Call call, final Exception e, final CallBack callback, final int requestCode) {
        if (callback == null) {
            return;
        }
        getDelivery().post(new Runnable() {
            @Override
            public void run() {
                mInFlightRequests.remove(requestCode);
                callback.onError(call, e, requestCode);
                callback.onAfter(requestCode);
            }
        });
    }

    public void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }

        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }

        //同时在mInFlightRequests删除保存所有TAG匹配的Request
        Iterator<Map.Entry<Integer, RequestCall>> it = mInFlightRequests.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, RequestCall> entry = it.next();
            Object rTag = entry.getValue().getRequest().tag();
            if (rTag != null && rTag.equals(tag)) {
                it.remove();
            }
        }
    }
}
