package com.newsee.common.http.interceptor;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2020/9/23 9:28
 * 说明: 请求数据封装/响应数据解析
 * ====================================
 */
public abstract class ReqResWrapperInterceptor extends BaseInterceptor {

    private static final String TAG = ReqResWrapperInterceptor.class.getSimpleName();

    private Gson mGson;

    public ReqResWrapperInterceptor() {
        mGson = new Gson();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        String serviceType = request.header(SERVICE_TYPE);
        if (null == serviceType || serviceType.isEmpty()) {
            return chain.proceed(request);
        }

        request = wrapperRequest(request, serviceType);
        Response response = chain.proceed(request);

        return responseTakeOffWrapper(request, response, serviceType);
    }

    // region ------------------------------------- 解析响应数据 -------------------------------------

    /**
     * 响应数据解析，只返回Data部分的数据，褪去 Response 层
     *
     * @param request
     * @param response
     * @param serviceType
     * @return
     * @throws IOException
     */
    private Response responseTakeOffWrapper(Request request, Response response, String serviceType) throws IOException {
        if (response.isSuccessful()) {
            String responseJson = getResponseDetail(response);
            JsonObject wrapperObject = mGson.fromJson(responseJson, JsonObject.class);

            Response resultResponse = null;

            switch (serviceType) {
                case SERVER_TYPE_V8_V9:  // V8/V9 接口
                    resultResponse = doParseResponseJson(SERVER_TYPE_V8_V9, response, wrapperObject);
                    if (resultResponse != null) {
                        response = resultResponse;
                    }
                    String v8v9Login = request.header(API_TYPE_LOGIN);
                    if (!TextUtils.isEmpty(v8v9Login)) {
                        // 登录接口，缓存数据
                        wrapperObject = mGson.fromJson(getResponseDetail(response), JsonObject.class);
                        doResponseCache(SERVER_TYPE_V8_V9, response, wrapperObject);
                    }
                    break;
                case SERVER_TYPE_V10_CHECK_HOUSE:  // V10 验房接口
                    resultResponse = doParseResponseJson(SERVER_TYPE_V10_CHECK_HOUSE, response, wrapperObject);
                    if (resultResponse != null) {
                        response = resultResponse;
                    }
                    String ssoLogin = request.header(API_TYPE_SSO_LOGIN);
                    if (!TextUtils.isEmpty(ssoLogin)) {
                        // 登录接口，缓存数据
                        doResponseCache(SERVER_TYPE_V10_CHECK_HOUSE, response, wrapperObject);
                    }
                    break;
                case SERVER_TYPE_V10_WORK_ORDER:   // V10 工单接口
                    resultResponse = doParseResponseJson(SERVER_TYPE_V10_WORK_ORDER, response, wrapperObject);
                    if (resultResponse != null) {
                        response = resultResponse;
                    }
                    String woLogin = request.header(API_TYPE_LOGIN);
                    String woSsoLogin = request.header(API_TYPE_SSO_LOGIN);
                    if (!TextUtils.isEmpty(woLogin) || !TextUtils.isEmpty(woSsoLogin)) {
                        // 登录接口，缓存数据
                        doResponseCache(SERVER_TYPE_V10_WORK_ORDER, response, wrapperObject);
                    }
                    break;
                default:    // 其它接口
                    response = doParseResponseJson(SERVER_TYPE_V10_WORK_ORDER, response, wrapperObject);
                    break;
            }

        }
        return response;
    }

    /**
     * 封装请求
     *
     * @param serverType
     * @param request
     * @param requestJson
     * @return
     */
    public abstract Map<String, Object> doWrapperRequest(String serverType, Request request, String requestJson);

    /**
     * 自定义解析接口数据
     *
     * @param serverType
     * @param response
     * @param jsonObject
     * @return
     */
    public abstract Response doParseResponseJson(String serverType, Response response, JsonObject jsonObject);

    /**
     * V10 登陆接口缓存
     *
     * @param serverType
     * @param response
     * @param jsonObject
     */
    public abstract void doResponseCache(String serverType, Response response, JsonObject jsonObject);

    /**
     * 解析旧版接口的响应
     *
     * @param response
     * @param wrapperObject
     * @return
     */
    protected Response takeOffV8V9WrapperResponse(Response response, JsonObject wrapperObject) {
        try {
            JsonObject oldResponseObject = wrapperObject.get("Response").getAsJsonObject();
            JsonObject headObject = oldResponseObject.get("Head").getAsJsonObject();
            JsonObject dataObject = oldResponseObject.get("Data").getAsJsonObject();

            try {
                // v9列表返回数据包含的数量信息
                if (dataObject.has("PAGE_INFO")) {
                    JsonObject v9PageObject = dataObject.get("PAGE_INFO").getAsJsonObject();
                    JsonArray recordArray = dataObject.get("Record").getAsJsonArray();
                    if (recordArray != null) {
                        JsonArray resultArray = new JsonArray();
                        for (JsonElement element : recordArray) {
                            JsonObject itemObject = mGson.fromJson(element, JsonObject.class);
                            itemObject.add("PageInfo", v9PageObject);
                            resultArray.add(itemObject);
                        }
                        dataObject.add("Record", resultArray);
                    }
                }

            } catch (Exception e) {
                Log.d(TAG, "OkHttpError-->" + e.getMessage());
            }

            ResponseBody responseBody = ResponseBody.create(response.body().contentType(), mGson.toJson(dataObject));
            return response.newBuilder().body(responseBody).build();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    // endregion ------------------------------------------------------------------------------------

    // region ------------------------------------- 处理请求数据 -------------------------------------

    /**
     * 请求数据包装通用数据
     *
     * @param request
     * @param serviceType 服务器类型
     * @return
     * @throws IOException
     */
    private Request wrapperRequest(Request request, String serviceType) throws IOException {
        String requestJson = getRequestDetail(request);
        if (TextUtils.isEmpty(requestJson)) {
            return request;
        }

        Map<String, Object> map = doWrapperRequest(serviceType, request, requestJson);
        if (map != null) {
            MediaType mediaType = request.body().contentType();
            RequestBody body = MultipartBody.create(mediaType, mGson.toJson(map));
            return request.newBuilder()
                    .post(body)
                    .build();
        } else {
            return request;
        }
    }

    // endregion ------------------------------------------------------------------------------------

}
