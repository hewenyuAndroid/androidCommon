package com.newsee.lib;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.newsee.common.global.CommonApplication;
import com.newsee.common.http.RetrofitManager;
import com.newsee.common.http.interceptor.BaseInterceptor;
import com.newsee.common.http.interceptor.EncryptionAndDecryptionInterceptor;
import com.newsee.common.http.interceptor.HttpLoggingInterceptor;
import com.newsee.common.http.interceptor.ReqResWrapperInterceptor;
import com.newsee.common.http.interceptor.UpdateUrlInterceptor;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2020/9/23 10:22
 * 说明:
 * ====================================
 */
public class LibApplication extends Application {

    private Gson mGson;

    @Override
    public void onCreate() {
        super.onCreate();

        mGson = new Gson();

        CommonApplication.getInstance()
                .init(this);
        initRetrofit();

    }

    private void initRetrofit() {
        RetrofitManager.getInstance()
                .addInterceptor(new UpdateUrlInterceptor() {
                    @Override
                    protected Request doUpdateUrl(String serverType, Request request) {
                        switch (serverType) {
                            case BaseInterceptor.SERVER_TYPE_V8_V9:
                                request = updateUrl(request, "http://192.168.4.173:8081/newseeserver.aspx");
                                break;
                        }
                        return request;
                    }
                })
                .addInterceptor(new ReqResWrapperInterceptor() {
                    @Override
                    public Map<String, Object> doWrapperRequest(String serverType, Request request, String requestJson) {
                        switch (serverType) {
                            case BaseInterceptor.SERVER_TYPE_V8_V9:
                                //{"Request":{"Data":{"Account":"susan","PushClientID":"4e22deaaf15fcd189ac49aa4fd5b3727","PushClientToken":"/oIlU3mPCRno4svdyWusapsBXjUzHNL9BYT8QMGc09GTGgvZCJ1FsD6Wv4BP81ul","AppId":"4ce19ca8fcd150a4","PackageName":"com.newsee.wygl","AppClientType":"mb","ClientFlag":"4","DeviceInfo":"System:Android 10 Device:xiaomi Redmi Note 7 Version:3.3.20200921","ClientVersionID":"3.3.20200921","PassWord":"susan123","Mac":"B4:C4:FC:F1:35:62"},"Head":{"NWCode":"1","NWExID":"jTABp3eghVrAeKFDDWhmtkvSjlUHpFc6D13xPLngnRqntd4cCXsC1iqQQQYj97xyAPuzdxFJLtg=","NWGUID":"201109090001","NWRandom":"3c143ed53b9eb0d2","NWVersion":"1","SubDBConfigID":"0"}}}
                                return wrapperV8V9Request(request, requestJson);
                        }
                        return null;
                    }

                    @Override
                    public Response doParseResponseJson(String serverType, Response response, JsonObject jsonObject) {
                        switch (serverType) {
                            case BaseInterceptor.SERVER_TYPE_V8_V9:
                                return takeOffV8V9WrapperResponse(response, jsonObject);
                        }
                        return null;
                    }

                    @Override
                    public void doResponseCache(String serverType, Response response, JsonObject jsonObject) {
                        switch (serverType) {
                            case BaseInterceptor.SERVER_TYPE_V8_V9:
                                Log.d("TAG", "-->" + jsonObject.toString());
                                break;
                        }
                    }
                })
                .addInterceptor(HttpLoggingInterceptor.getDefault())
                .addInterceptor(new EncryptionAndDecryptionInterceptor("01234567890123456789012345678901"))
                .build();
    }

    private Map<String, Object> wrapperV8V9Request(Request request, String requestJson) {

        Map<String, Object> mapData = mGson.fromJson(requestJson, Map.class);

        mapData.put("Account", "susan");
        mapData.put("PassWord", "susan123");
        mapData.put("PushClientToken", "/oIlU3mPCRno4svdyWusapsBXjUzHNL9BYT8QMGc09GTGgvZCJ1FsD6Wv4BP81ul");
        mapData.put("PushClientID", "4e22deaaf15fcd189ac49aa4fd5b3727");
        mapData.put("AppId", "4ce19ca8fcd150a4");
        mapData.put("PackageName", "com.newsee.wygl");
        mapData.put("Mac", "B4:C4:FC:F1:35:62");
        mapData.put("OS", "Android");
//        mapData.put("UserID", "2");
//        mapData.put("PrecinctID", "820");

        // Head 模块数据包装
        Map<String, Object> mapHead = new HashMap<>();
        mapHead.put("NWGUID", "201109090001");
//        mapHead.put("NWExID", NWExID);
        mapHead.put("NWCode", mapData.remove("NWCode"));
        mapHead.put("NWVersion", "1");
        mapHead.put("NWRandom", "3c143ed53b9eb0d2");
        mapData.put("NWExID", "jTABp3eghVrAeKFDDWhmtkvSjlUHpFc6D13xPLngnRqntd4cCXsC1iqQQQYj97xyAPuzdxFJLtg=");

        // 封装 Head Data 为同一个对象
        Map<String, Object> mapCombine = new HashMap<>();
        mapCombine.put("Head", mapHead);
        mapCombine.put("Data", mapData);

        // 统一封装成 Json 对象
        Map<String, Object> mapRequest = new HashMap<>();
        mapRequest.put("Request", mapCombine);

        return mapRequest;

    }

    private String NWExID = "";

}
