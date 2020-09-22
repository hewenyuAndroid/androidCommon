package com.newsee.common.http.result;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2019/7/3 11:55
 * 说明: RxJava 返回数据的封装
 * ====================================
 */
public class HttpResult<T> implements IHttpResult<T> {

    // region -------------------- 旧版接口号解析 --------------------

    public String NWRespCode;

    public String NWErrMsg;

    public int RecordCount;

    public T Record;

    // endregion -----------------------------------------------------

    // region -------------------- Java验房接口解析 --------------------

    public String resultCode;

    public String resultMsg;

    public T resultData;

    // endregion --------------------------------------------------------

    // region -------------------- Java 荟生活接口解析 --------------------

    public Boolean success;

    public String errorCode;

    public String errorMsg;

    public T data;

    // endregion ----------------------------------------------------------

    @Override
    public boolean isSuccess() {
        return (null != NWRespCode && (NWRespCode.equals("0000") || NWRespCode.equals("00000") || NWRespCode.equals("000")))    // 旧版接口号成功
                || (null != resultCode && resultCode.equals("200")  // Java验房接口成功
                || (null != success && success) // 荟生活接口成功
        );
    }

    @Override
    public T getData() {
        if (null != NWRespCode) {
            return Record;
        } else if (null != resultCode) {
            return resultData;
        } else if (null != success) {
            return data;
        }
        return null;
    }

}
