package com.ok.zzh.okhttplibrary.builder.builderextend;

import android.net.Uri;
import android.text.TextUtils;

import com.ok.zzh.okhttplibrary.builder.HasParams;
import com.ok.zzh.okhttplibrary.builder.RequestBuilder;
import com.ok.zzh.okhttplibrary.request.RequestCall;
import com.ok.zzh.okhttplibrary.request.requestextend.GetRequest;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/8/14.
 * <p>
 * 这个类是一个 get 请求的builder 类
 */

public class GetBuilder extends RequestBuilder<GetBuilder> implements HasParams {

    @Override
    public GetBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public GetBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }

    @Override
    public RequestCall build() {
        if (params == null) {
            url = appendParams(url, params);
        }
        return new GetRequest(url, tag, params, headers, requestCode).build();
    }

    private String appendParams(String url, Map<String, String> params) {
        if (TextUtils.isEmpty(url) || params == null || params.isEmpty()) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }
}
