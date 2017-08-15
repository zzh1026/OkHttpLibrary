package com.ok.zzh.okhttplibrary.request;

import com.ok.zzh.okhttplibrary.OkHttpHelper;
import com.ok.zzh.okhttplibrary.callback.CallBack;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/8/14.
 * <p>
 * 该类是对 HttpRequest 的封装,主要是对一个请求newCall 后产生的 call 对象进行引用,用于对外提供更多的接口：cancel(),readTimeOut()...
 */

public class RequestCall {
    private OkHttpRequest okHttpRequest;
    private Request request;

    private Call call;

    private long readTimeOut;
    private long writeTimeOut;
    private long connTimeOut;

    private OkHttpClient newClient;

    public RequestCall(OkHttpRequest request) {
        this.okHttpRequest = request;
    }

    public RequestCall readTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public RequestCall writeTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return this;
    }

    public RequestCall connTimeOut(long connTimeOut) {
        this.connTimeOut = connTimeOut;
        return this;
    }

    public Call getCall() {
        return call;
    }

    public Request getRequest() {
        return request;
    }

    public OkHttpRequest getOkHttpRequest() {
        return okHttpRequest;
    }

    private Request generateRequest(CallBack callBack) {
        return getOkHttpRequest().generateRequest(callBack);
    }


    public Call buildCall(CallBack callBack) {
        //这里是处理联网逻辑的最重要的调用
        //先获取到request请求对象
        request = generateRequest(callBack);

        if (readTimeOut > 0 || writeTimeOut > 0 || connTimeOut > 0) {
            readTimeOut = readTimeOut > 0 ? readTimeOut : OkHttpHelper.DEFAULT_TIME_OUT_MILLISECONDS;
            writeTimeOut = writeTimeOut > 0 ? writeTimeOut : OkHttpHelper.DEFAULT_TIME_OUT_MILLISECONDS;
            connTimeOut = connTimeOut > 0 ? connTimeOut : OkHttpHelper.DEFAULT_TIME_OUT_MILLISECONDS;

            //本次请求的自定义参数
            newClient = OkHttpHelper.getInstance().getOkHttpClient().newBuilder()
                    .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                    .connectTimeout(connTimeOut, TimeUnit.MILLISECONDS)
                    .build();

            call = newClient.newCall(request);
        } else {
            call = OkHttpHelper.getInstance().getOkHttpClient().newCall(request);
        }

        //获取到的 call 为该次网络请求中的call
        return call;
    }

    public void execute(CallBack callBack) {
        buildCall(callBack);

        if (callBack != null) {
            callBack.onBefore(request, getOkHttpRequest().getRequestCode());
        }

        OkHttpHelper.getInstance().execute(this, callBack);
    }

    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }

}
