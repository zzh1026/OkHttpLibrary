package com.ok.zzh.okhttplibrary.cookie.store;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public interface CookieStore {

    /**
     * 保存url对应所有cookie
     */
    void saveCookie(HttpUrl uri, List<Cookie> cookie);

    /**
     * 加载url所有的cookie
     */
    List<Cookie> loadCookie(HttpUrl uri);

    /**
     * 获取当前所有保存的cookie
     */
    List<Cookie> getAllCookies();

    /**
     * 根据url和cookie移除对应的cookie
     */
    boolean removeCookie(HttpUrl uri, Cookie cookie);

    /**
     * 移除所有的cookie
     */
    boolean removeAllCookies();

}
