package com.ok.zzh.okhttplibrary.builder;

import java.util.Map;

/**
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/8/14.
 * <p>
 * 该接口用来对 RequestBuilder 的参数进行处理,查看是否含有参数的情况
 */

public interface HasParams {
    RequestBuilder params(Map<String, String> params);

    RequestBuilder addParams(String key, String val);
}
