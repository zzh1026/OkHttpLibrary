package com.ok.zzh.okhttplibrary.request.requestextend;

import android.support.annotation.NonNull;

import com.ok.zzh.okhttplibrary.request.OkHttpRequest;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/8/14.
 */

public class GetRequest extends OkHttpRequest {

    public GetRequest(@NonNull String url, Object tag, Map<String, String> params, Map<String, String> headers, int requestCode) {
        super(url, tag, params, headers, requestCode);
    }

    @Override
    protected RequestBody buildRequestBody() {
        return null;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.get().build();
    }
}
