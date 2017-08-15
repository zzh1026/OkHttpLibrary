package com.ok.zzh.okhttplibrary.builder;

import com.ok.zzh.okhttplibrary.request.RequestCall;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/8/14.
 * <p>
 * 该类的作用是
 * 1,封装了 okhttp 中 request.builder 中的一些基本参数
 * 2,对外暴露,设置参数.
 */

public abstract class RequestBuilder<T extends RequestBuilder> {
    //请求地址
    protected String url;
    //设置的标签
    protected Object tag;
    //请求头的参数
    protected Map<String, String> headers;
    //请求的具体参数
    protected Map<String, String> params;
    //请求码
    protected int requestCode;

    public T requestCode(int requestCode) {
        this.requestCode = requestCode;
        return (T) this;
    }

    public T url(String url) {
        this.url = url;
        return (T) this;
    }

    public T tag(Object tag) {
        this.tag = tag;
        return (T) this;
    }

    public T headers(Map<String, String> headers) {
        this.headers = headers;
        return (T) this;
    }

    public T addHeader(String key, String val) {
        if (this.headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return (T) this;
    }

    public abstract RequestCall build();

}
