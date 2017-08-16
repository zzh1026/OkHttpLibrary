package com.ok.zzh.okhttplibrary.interceptor;

import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.ok.zzh.okhttplibrary.BuildConfig;

import okhttp3.Interceptor;
import okhttp3.internal.platform.Platform;

/**
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/8/14.
 */

public class LoggerInterceptor {

    /**
     * 得到一个 Interceptor 类型的 logger 拦截器,用来进行 打印拦截
     *
     * @return
     */
    public static Interceptor getLoggingIntercaptor() {
        if (true)
            return null;
        LoggingInterceptor.Builder loggingInterceptor = new LoggingInterceptor.Builder();
        loggingInterceptor
                .loggable(true)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("Request")
                .response("Response")
                .addHeader("version", BuildConfig.VERSION_NAME);
        return loggingInterceptor.build();
    }
}
