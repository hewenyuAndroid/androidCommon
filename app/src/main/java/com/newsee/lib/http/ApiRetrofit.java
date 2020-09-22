package com.newsee.lib.http;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.newsee.common.http.RetrofitManager;
import com.newsee.common.http.result.HttpResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2020/9/23 10:51
 * 说明:
 * ====================================
 */
public class ApiRetrofit {

    private static final String NWCode = "NWCode";

    private static volatile ApiRetrofit sInstance;

    private ApiService mApiService;

    public static final ApiRetrofit getInstance() {
        if (sInstance == null) {
            synchronized (ApiRetrofit.class) {
                if (sInstance == null) {
                    sInstance = new ApiRetrofit();
                }
            }
        }
        return sInstance;
    }

    private ApiRetrofit() {
        mApiService = RetrofitManager.getInstance().getRetrofit().create(ApiService.class);
    }

    /**
     * 请求体实例对象获取类
     *
     * @param map
     * @return
     */
    private RequestBody getBody(Map<String, Object> map) {
        if (null == map) {
            map = new HashMap<>();
        }
        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(map));
    }

    public Observable<HttpResult<List<JSONObject>>> login(String account, String password) {
        Map<String, Object> map = new HashMap<>();
        map.put(NWCode, 1 + "");
        map.put("Account", account);
        map.put("PassWord", password);
        return mApiService.login(getBody(map));
    }


}
