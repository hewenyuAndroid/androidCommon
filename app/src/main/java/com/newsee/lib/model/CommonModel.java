package com.newsee.lib.model;

import com.alibaba.fastjson.JSONObject;
import com.newsee.common.http.observer.RxHelper;
import com.newsee.lib.http.ApiRetrofit;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Observer;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2020/9/23 11:06
 * 说明:
 * ====================================
 */
public class CommonModel {

    public void login(String account, String password, Observer observer) {
        ApiRetrofit.getInstance()
                .login(account, password)
                .compose(RxHelper.<List<JSONObject>>transformer())
                .subscribe(observer);
    }

    public void login(@NotNull String s, @NotNull String s1, @NotNull Object any) {

    }
}
