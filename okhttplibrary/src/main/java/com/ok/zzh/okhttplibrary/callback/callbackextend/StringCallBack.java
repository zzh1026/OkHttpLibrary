package com.ok.zzh.okhttplibrary.callback.callbackextend;

import com.ok.zzh.okhttplibrary.callback.CallBack;

import okhttp3.Response;


/**
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/8/15.
 */

public abstract class StringCallBack extends CallBack<String> {
    @Override
    public String parseNetworkResponse(Response response, int requestCode) throws Exception {
        return response.body().string();
    }
}
