package com.ok.zzh.okhttplibrary.builder.builderextend;

import android.net.Uri;
import android.text.TextUtils;

import com.ok.zzh.okhttplibrary.builder.HasParams;
import com.ok.zzh.okhttplibrary.builder.RequestBuilder;
import com.ok.zzh.okhttplibrary.request.RequestCall;
import com.ok.zzh.okhttplibrary.request.requestextend.PostFileRequest;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;

/**
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/8/15.
 */

public class PostFileBuilder extends RequestBuilder<PostFileBuilder> implements HasParams {

    private File file;
    private MediaType mediaType;
    private Map<String, String> urlParams;

    @Override
    public RequestCall build() {
        if (urlParams == null) {
            url = appendParams(url, params);
        }
        return new PostFileRequest(url, tag, params, headers, requestCode, file, mediaType).build();
    }


    @Override
    public PostFileBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public PostFileBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }

    public PostFileBuilder urlParams(Map<String, String> setUrlParams) {
        this.urlParams = setUrlParams;
        return this;
    }

    public PostFileBuilder file(File file) {
        this.file = file;
        return this;
    }

    public PostFileBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
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
