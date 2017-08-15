package com.ok.zzh.okhttplibrary.builder.builderextend;

import android.net.Uri;
import android.text.TextUtils;

import com.ok.zzh.okhttplibrary.builder.HasParams;
import com.ok.zzh.okhttplibrary.builder.RequestBuilder;
import com.ok.zzh.okhttplibrary.request.RequestCall;
import com.ok.zzh.okhttplibrary.request.requestextend.PostRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/8/15.
 */

public final class PostBuilder extends RequestBuilder<PostBuilder> implements HasParams {

    /**
     * post方法通过 key 上传多文件的本质上是一个form标签上传的形式, 所以在 普通 form提交的创建中添加了files的提交
     */
    private List<FileInput> files = new ArrayList<>();

    //这个是强制在url后面拼接的key和value ,一般的post请求的参数是放在请求体中的,但是不排除url后面需要拼接的header
    private Map<String, String> urlParams;

    @Override
    public PostBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public PostBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }

    public PostBuilder files(String key, Map<String, File> files) {
        for (String filename : files.keySet()) {
            this.files.add(new FileInput(key, filename, files.get(filename)));
        }
        return this;
    }

    /**
     * 设置url后面需要拼接的参数
     *
     * @param setUrlParams
     * @return
     */
    public PostBuilder urlParams(Map<String, String> setUrlParams) {
        this.urlParams = setUrlParams;
        return this;
    }

    @Override
    public RequestCall build() {
        if (urlParams == null) {
            url = appendParams(url, params);
        }
        return new PostRequest(url, tag, params, headers, requestCode, files).build();
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


    /**
     * 对每个file的封装,因为上传的时候需要三个参数 : key,filename,file
     */
    public static class FileInput {
        public String key;
        public String filename;
        public File file;

        public FileInput(String name, String filename, File file) {
            this.key = name;
            this.filename = filename;
            this.file = file;
        }
    }
}
