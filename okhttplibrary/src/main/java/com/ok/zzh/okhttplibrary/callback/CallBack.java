package com.ok.zzh.okhttplibrary.callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/8/14.
 * <p>
 * 这个类是 所有关于 okhttp获取到数据后的返回数据的封装基类
 */

public abstract class CallBack<T> {

    public static final CallBack DEFAULT_CALLBACK = new CallBack() {
        @Override
        public Object parseNetworkResponse(Response response, int requestCode) throws Exception {
            return null;
        }

        @Override
        public void onError(Call call, Exception e, int reqeustCode) {

        }

        @Override
        public void onResponse(Object response, int reqeustCode) {

        }
    };

    /**
     * 在 main thread 中,可以修改界面,返回 请求对象 和 请求码
     * <p>
     * 在网络请求之前调用
     *
     * @param request     请求对象
     * @param reqeustCode 请求码
     */
    public void onBefore(Request request, int reqeustCode) {
    }

    /**
     * 在 main thread 中,可以修改界面,返回 请求码
     * <p>
     * 在网络请求之后调用
     *
     * @param reqeustCode 请求码
     */
    public void onAfter(int reqeustCode) {
    }

    /**
     * 在 main thread 中,可以修改界面,返回 当前已经下载的 大小 , 总大小 和 请求码
     * <p>
     * 在网络下载之间调用
     *
     * @param progress    文件已经下载的大小 , 百分比 大小为  0 --> 1 变化
     * @param total       文件总大小
     * @param reqeustCode 请求码
     */
    public void inProgress(float progress, long total, int reqeustCode) {
    }

    /**
     * 判断数据是否请求成功
     *
     * @param response
     * @return
     */
    public boolean validateReponse(Response response, int requestCode) {
        return response.isSuccessful();
    }

    /**
     * 用来解析 response 返回的数据 ,在工作线程,可以执行耗时操作
     * <p>
     * 在网络请求到数据的其他线程调用
     *
     * @param response
     * @return
     * @throws Exception
     */
    public abstract T parseNetworkResponse(Response response, int requestCode) throws Exception;

    /**
     * 对数据获取失败的回调, 在 main 线程中执行
     *
     * @param call
     * @param e
     * @param reqeustCode
     */
    public abstract void onError(Call call, Exception e, int reqeustCode);

    /**
     * 对数据获取成功的回调 ,在 main 线程中执行
     *
     * @param response
     * @param reqeustCode
     */
    public abstract void onResponse(T response, int reqeustCode);
}
