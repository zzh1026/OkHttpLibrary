package com.ok.zzh.okhttplibrary.request.requestextend;

import android.support.annotation.NonNull;

import com.ok.zzh.okhttplibrary.OkHttpHelper;
import com.ok.zzh.okhttplibrary.callback.CallBack;
import com.ok.zzh.okhttplibrary.request.CountingRequestBody;
import com.ok.zzh.okhttplibrary.request.OkHttpRequest;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/8/15.
 * <p>
 * 上传单个文件
 */

public class PostFileRequest extends OkHttpRequest {
    private static MediaType DEFAULT_MEDIATYPE = MediaType.parse("application/octet-stream");

    private File file;
    private MediaType mediaType;

    public PostFileRequest(@NonNull String url, Object tag, Map<String, String> params,
                           Map<String, String> headers, int requestCode, @NonNull File file, MediaType mediaType) {
        super(url, tag, params, headers, requestCode);
        this.file = file;
        if (mediaType == null) {
            this.mediaType = DEFAULT_MEDIATYPE;
        } else {
            this.mediaType = mediaType;
        }
    }

    @Override
    protected RequestBody buildRequestBody() {
        return RequestBody.create(mediaType, file);
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
}
