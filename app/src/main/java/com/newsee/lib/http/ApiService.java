package com.newsee.lib.http;

import com.alibaba.fastjson.JSONObject;
import com.newsee.common.http.interceptor.BaseInterceptor;
import com.newsee.common.http.result.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2020/9/23 10:45
 * 说明:
 * ====================================
 */
public interface ApiService {

    /**
     * 服务器地址类别标注请求头
     */
    String SERVER_TYPE = BaseInterceptor.SERVICE_TYPE + ":" + BaseInterceptor.SERVER_TYPE_V8_V9;

    /**
     * 登录接口标注请求头
     */
    String API_LOGIN = BaseInterceptor.API_TYPE_LOGIN + ":1";

    /**
     * V8/v9 请求地址固定
     */
    String API_PATH = "apiCode";

    @Headers({SERVER_TYPE, API_LOGIN})
    @POST(API_PATH)
    Observable<HttpResult<List<JSONObject>>> login(@Body RequestBody body);

    /**
     * 获取菜单列表
     *
     * @param body
     * @return
     */
    @Headers(SERVER_TYPE)
    @POST(API_PATH)
    HttpResult<List<JSONObject>> loadMenu(@Body RequestBody body);

}
