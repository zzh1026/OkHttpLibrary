package com.ok.zzh.okhttplibrary.callback.callbackextend;

import com.google.gson.Gson;
import com.ok.zzh.okhttplibrary.callback.CallBack;

import okhttp3.Response;

/**
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/8/15.
 */

public abstract class ObjectCallBack extends CallBack {

    @Override
    public Object parseNetworkResponse(Response response, int requestCode) throws Exception {
        Class clazz = getResponseClass();
        String string = response.body().string();
        return new Gson().fromJson(string, clazz);
    }

    protected abstract Class getResponseClass();

}
