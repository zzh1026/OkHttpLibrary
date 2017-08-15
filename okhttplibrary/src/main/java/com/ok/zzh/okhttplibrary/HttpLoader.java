package com.ok.zzh.okhttplibrary;

import android.text.TextUtils;

import com.ok.zzh.okhttplibrary.builder.RequestBuilder;
import com.ok.zzh.okhttplibrary.builder.builderextend.GetBuilder;
import com.ok.zzh.okhttplibrary.builder.builderextend.PostBuilder;
import com.ok.zzh.okhttplibrary.callback.CallBack;
import com.ok.zzh.okhttplibrary.callback.callbackextend.ObjectCallBack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/8/14.
 */

public class HttpLoader {

    private HttpLoader() {
    }

    /**
     * 以get方式发送请求
     *
     * @param url
     * @param params
     * @param clazz
     * @param requestCode
     * @param tag
     * @param listener
     */
    public static void get(String url, Map<String, String> params, final Class clazz,
                           final int requestCode, Object tag, final ResponseListener listener) {
        GetBuilder getBuilder = OkHttpHelper.get()
                .params(params);
        initRequestBuilder(getBuilder, url, requestCode, tag, new ObjectCallBack() {
            @Override
            protected Class getResponseClass() {
                return clazz;
            }

            @Override
            public void onError(Call call, Exception e, int reqeustCode) {
                listener.onResponseError(reqeustCode, e, call);
            }

            @Override
            public void onResponse(Object response, int reqeustCode) {
                listener.onResponseSuccess(requestCode, response);
            }
        });
    }

    /**
     * 发送post请求
     *
     * @param url
     * @param params
     * @param clazz
     * @param requestCode
     * @param tag
     * @param listener
     */
    public static void post(String url, Map<String, String> params, final Class clazz,
                            final int requestCode, Object tag, final ResponseListener listener) {
        postFiles(url, params, clazz, requestCode, tag, null, null, listener);
    }

    /**
     * 上传文件
     *
     * @param url
     * @param params
     * @param clazz
     * @param requestCode
     * @param tag
     * @param fileKey
     * @param files
     * @param listener
     */
    public static void postFiles(String url, Map<String, String> params, final Class clazz,
                                 final int requestCode, Object tag, String fileKey, Map<String, File> files, final ResponseListener listener) {
        PostBuilder postBuilder = OkHttpHelper.post()
                .params(params);
        if (!TextUtils.isEmpty(fileKey)) {
            postBuilder.files(fileKey, files);
        }
        initRequestBuilder(postBuilder, url, requestCode, tag, new ObjectCallBack() {
            @Override
            protected Class getResponseClass() {
                return clazz;
            }

            @Override
            public void onError(Call call, Exception e, int reqeustCode) {
                listener.onResponseError(reqeustCode, e, call);
            }

            @Override
            public void onResponse(Object response, int reqeustCode) {
                listener.onResponseSuccess(requestCode, response);
            }
        });
    }

    /**
     * 初始化builder ,用来设置公共的参数
     *
     * @param requestBuilder
     * @param url
     * @param requestCode
     * @param tag
     * @param callBack
     */
    private static void initRequestBuilder(RequestBuilder requestBuilder, String url,
                                           int requestCode, Object tag, CallBack callBack) {
        requestBuilder
                .url(url)
                .requestCode(requestCode)
                .headers(getDefaultHeaders())
                .tag(tag)
                .build()
                .execute(callBack);
    }

    /**
     * 生成公共Header头信息
     *
     * @return
     */
    private static Map<String, String> getDefaultHeaders() {

        Map<String, String> headers = new HashMap<String, String>();
        //        appkey        软件身份key
        //        udid          手机客户端的唯一标识
        //        os            操作系统名称
        //        osversion     操作系统版本
        //        appversion    APP版本
        //        sourceid      推广ID
        //        ver           通讯协议版本
        //        userid        用户ID
        //        usersession   登陆后得到的用户唯一性标识
        //        unique        激活后得到的设备唯一性标识
        return headers;

    }


    /**
     * 成功获取到服务器响应结果的监听，供UI层调用
     */
    public interface ResponseListener<T> {

        /**
         * 成功获取到服务器响应数据的时候调用
         *
         * @param requestCode response对应的requestCode
         * @param response    返回的response
         */
        void onResponseSuccess(int requestCode, T response);

        /**
         * 网络获取失败，(做一些释放性的操作，比如关闭对话框)
         *
         * @param call        ,请求失败后返回请求时候的call
         * @param e
         * @param requestCode 请求码
         */
        void onResponseError(int requestCode, Exception e, Call call);
    }
}
