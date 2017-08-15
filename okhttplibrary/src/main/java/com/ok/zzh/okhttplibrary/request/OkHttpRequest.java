package com.ok.zzh.okhttplibrary.request;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ok.zzh.okhttplibrary.callback.CallBack;

import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/8/14.
 * <p>
 * 该类是 所有关于 okhttp 请求 request 的基类主要包含request 请求的一些基本信息.
 */

public abstract class OkHttpRequest {
    protected String url;
    protected Object tag;
    protected Map<String, String> params;
    protected Map<String, String> headers;
    protected int requestCode;

    protected Request.Builder builder = new Request.Builder();

    protected OkHttpRequest(@NonNull String url, Object tag,
                            Map<String, String> params, Map<String, String> headers, int requestCode) {
        this.url = url;
        this.tag = tag;
        this.params = params;
        this.headers = headers;
        this.requestCode = requestCode;

        if (TextUtils.isEmpty(url)) throw new IllegalArgumentException("url 不能为空");

        initBuilder();
    }

    /**
     * 初始化request的基本参数
     */
    private void initBuilder() {
        builder.url(url).tag(tag);
        appendHeaders();
    }

    /**
     * 添加头信息
     */
    private void appendHeaders() {
        if (headers == null || headers.isEmpty()) {
            return;
        }
        Headers.Builder headerBuilder = new Headers.Builder();
        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    protected RequestBody wrapRequestBody(RequestBody requestBody, CallBack callBack) {
        return requestBody;
    }

    public Request generateRequest(CallBack callBack) {
        RequestBody requestBody = buildRequestBody();
        RequestBody wrappedRequestBody = wrapRequestBody(requestBody, callBack);
        Request request = buildRequest(wrappedRequestBody);
        return request;
    }

    public int getRequestCode() {
        return requestCode;
    }

    /**
     * 创建请求体 用于post请求的时候
     * 子类需要实现该方法
     *
     * @return
     */
    protected abstract RequestBody buildRequestBody();

    /**
     * 根据 请求体创建请求
     *
     * @param requestBody
     * @return
     */
    protected abstract Request buildRequest(RequestBody requestBody);

    /**
     * 创建requestCall
     *
     * @return
     */
    public RequestCall build() {
        return new RequestCall(this);
    }
}
