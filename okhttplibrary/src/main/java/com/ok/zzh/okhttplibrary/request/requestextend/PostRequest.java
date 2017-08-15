package com.ok.zzh.okhttplibrary.request.requestextend;

import android.support.annotation.NonNull;

import com.ok.zzh.okhttplibrary.OkHttpHelper;
import com.ok.zzh.okhttplibrary.builder.builderextend.PostBuilder;
import com.ok.zzh.okhttplibrary.callback.CallBack;
import com.ok.zzh.okhttplibrary.request.CountingRequestBody;
import com.ok.zzh.okhttplibrary.request.OkHttpRequest;

import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/8/15.
 */

public class PostRequest extends OkHttpRequest {
    private List<PostBuilder.FileInput> files;

    public PostRequest(@NonNull String url, Object tag, Map<String, String> params, Map<String, String> headers, int requestCode, List<PostBuilder.FileInput> files) {
        super(url, tag, params, headers, requestCode);
        this.files = files;
    }

    @Override
    protected RequestBody buildRequestBody() {
        if (files == null || files.isEmpty()) {
            FormBody.Builder builder = new FormBody.Builder();
            addParams(builder);
            FormBody formBody = builder.build();
            return formBody;
        } else {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            addParams(builder);
            for (PostBuilder.FileInput fileInput : files) {
                builder.addFormDataPart(fileInput.key, fileInput.filename,
                        RequestBody.create(MediaType.parse(guessMimeType(fileInput.filename)), fileInput.file));
            }
            return builder.build();
        }
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }

    @Override
    protected RequestBody wrapRequestBody(RequestBody requestBody, final CallBack callBack) {
        if (callBack == null) return requestBody;
        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength) {
                OkHttpHelper.getInstance().getDelivery().post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.inProgress(bytesWritten * 1.0f / contentLength, contentLength, requestCode);
                    }
                });
            }
        });
        return countingRequestBody;
    }

    private String guessMimeType(String filename) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = null;
        try {
            contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(filename, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private void addParams(MultipartBody.Builder builder) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key));
            }
        }
    }

    private void addParams(FormBody.Builder builder) {
        if (params != null) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
    }
}
