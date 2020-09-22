package com.newsee.common.http.interceptor;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * ================================================
 * 作者: hewenyu
 * 日期: 2019/6/17 15:19
 * 说明: 拦截器基础类
 * ================================================
 */
public abstract class BaseInterceptor implements Interceptor {

    protected final Charset UTF8 = Charset.forName("UTF-8");

    protected final Charset GBK = Charset.forName("GBK");


    // region ------------------------------------ 接口请求头信息 ------------------------------------

    /**
     * 服务器类型
     */
    public static final String SERVICE_TYPE = "serviceType";

    /**
     * V8/V9接口
     */
    public static final String SERVER_TYPE_V8_V9 = "server_type_v8_v9";

    /**
     * V10 验房接口(java)
     */
    public static final String SERVER_TYPE_V10_CHECK_HOUSE = "server_type_v10_check_house";

    /**
     * V10 工单接口(java)
     */
    public static final String SERVER_TYPE_V10_WORK_ORDER = "server_type_v10_work_order";

    /**
     * SSO 登陆接口
     */
    public static final String API_TYPE_SSO_LOGIN = "api_type_sso_login";

    /**
     * 登陆接口
     */
    public static final String API_TYPE_LOGIN = "api_type_login";

    // endregion -----------------------------------------------------------------------------------

    /**
     * SSO 登陆接口地址
     */
    public static final String SSO_PATH = "api/oauth/login";

    /**
     * 获取请求的数据
     *
     * @param request
     * @return
     */
    protected String getRequestDetail(Request request) throws IOException {
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }

            return buffer.readString(charset);
        }
        return "";
    }

    /**
     * 获取返回的数据
     *
     * @param response
     * @return
     */
    protected String getResponseDetail(Response response) {
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();

        BufferedSource source = responseBody.source();
        try {
            source.request(9223372036854775807L);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Buffer buffer = source.buffer();

        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }

        if (contentLength != 0L) {
            return buffer.clone().readString(charset);
        }
        return "";
    }

}
