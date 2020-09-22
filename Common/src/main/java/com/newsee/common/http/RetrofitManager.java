package com.newsee.common.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.newsee.common.http.interceptor.EncryptionAndDecryptionInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/7/2 14:00
 * 说明: 获取 Retrofit 单例对象工具类
 * ====================================
 */
public class RetrofitManager {

    /**
     * url占位符，具体访问url由拦截器中修改
     */
    public static final String URL_PLACE_HOLDER = "http://192.168.1.20/";

    private static final int TIME_OUT = 20;

    private volatile static RetrofitManager sInstance;

    private List<Interceptor> mInterceptors;

    private Retrofit mRetrofit;

    private RetrofitManager() {
        mInterceptors = new ArrayList<>();

    }

    public static RetrofitManager getInstance() {
        if (sInstance == null) {
            synchronized (RetrofitManager.class) {
                if (sInstance == null) {
                    sInstance = new RetrofitManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 拦截器参考移动物管
     *
     * @param interceptor
     */
    public RetrofitManager addInterceptor(Interceptor interceptor) {
        if (interceptor != null) {
            mInterceptors.add(interceptor);
        }
        return this;
    }

    /**
     * 更新密钥
     *
     * @param secretKey
     */
    public void updateSecretKey(String secretKey) {
        for (Interceptor interceptor : mInterceptors) {
            if (interceptor instanceof EncryptionAndDecryptionInterceptor) {
                ((EncryptionAndDecryptionInterceptor) interceptor).setSecretKey(secretKey);
            }
        }
    }

    public void build() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 添加拦截器
        for (Interceptor interceptor : mInterceptors) {
            builder.addInterceptor(interceptor);
        }
        builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.followRedirects(true);

        OkHttpClient okHttpClient = builder.build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(URL_PLACE_HOLDER)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }

    public Retrofit getRetrofit() {
        if (mRetrofit == null) {
            throw new RuntimeException("please call build()");
        }
        return mRetrofit;
    }

}
