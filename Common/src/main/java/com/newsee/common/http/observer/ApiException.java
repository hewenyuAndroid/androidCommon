package com.newsee.common.http.observer;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/7/3 14:05
 * 说明:
 * ====================================
 */
public class ApiException extends Throwable {

    private String mErrorCode;

    public ApiException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.mErrorCode = errorCode;
    }

    public String getErrorCode() {
        return mErrorCode;
    }

}
