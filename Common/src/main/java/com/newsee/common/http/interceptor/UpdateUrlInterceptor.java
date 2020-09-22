package com.newsee.common.http.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2020/9/23 10:16
 * 说明: 更新请求地址拦截器
 * ====================================
 */
public abstract class UpdateUrlInterceptor extends BaseInterceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String serviceType = request.header(SERVICE_TYPE);
        if (null == serviceType || serviceType.isEmpty()) {
            Log.e("TAG", "服务器类型未标注");
            return chain.proceed(request);
        }

        return chain.proceed(doUpdateUrl(serviceType, request));
    }

    /**
     * 解析请求地址
     *
     * @param serverType
     * @param request
     * @return
     */
    protected abstract Request doUpdateUrl(String serverType, Request request);

    /**
     * 修改请求地址
     *
     * @param request
     * @param url
     * @return
     */
    protected Request updateUrl(Request request, String url) {
        try {
            return request.newBuilder()
                    .url(HttpUrl.parse(url))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }

}
