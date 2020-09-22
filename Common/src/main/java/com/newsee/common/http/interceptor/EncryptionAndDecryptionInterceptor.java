package com.newsee.common.http.interceptor;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * ====================================
 * 作者: hewenyu
 * 日期: 2020/9/23 9:16
 * 说明: 网络请求加密/解密拦截器
 * ====================================
 */
public class EncryptionAndDecryptionInterceptor extends BaseInterceptor {

    /**
     * 密钥
     */
    private String mSecretKey = "";

    public EncryptionAndDecryptionInterceptor(String secretKey) {
        this.mSecretKey = secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.mSecretKey = secretKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String serviceType = request.header(SERVICE_TYPE);
        switch (serviceType) {
            case SERVER_TYPE_V8_V9:  // V8/V9 接口加密报文
                // 加密报文
                request = encryption(request);
                break;
        }

        Response response = chain.proceed(request);

        switch (serviceType) {
            case SERVER_TYPE_V8_V9:  // V8/V9 接口解密报文
                try {
                    // 解密报文
                    return decryptResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        return response;
    }

    /**
     * 加密请求报文
     *
     * @param request
     * @return
     */
    private Request encryption(Request request) throws IOException {
        String requestJson = getRequestDetail(request);
        if (TextUtils.isEmpty(requestJson)) {
            return request;
        }

        String encryptStr = encrypt(requestJson, mSecretKey);

        MediaType contentType = request.body().contentType();
        RequestBody body = MultipartBody.create(contentType, encryptStr);
        request = request.newBuilder()
                .post(body)
                .build();

        return request;
    }

    /**
     * 解密响应报文
     *
     * @param response
     * @return
     */
    private Response decryptResponse(Response response) throws Exception {
        if (response.isSuccessful()) {
            MediaType contentType = response.body().contentType();
            String decryptResult = decrypt(getResponseDetail(response), mSecretKey);
            ResponseBody responseBody = ResponseBody.create(contentType, decryptResult);
            return response.newBuilder().body(responseBody).build();
        }
        return response;
    }

    /**
     * 加密算法
     *
     * @param input
     * @param key
     * @return
     */
    public static String encrypt(String input, String key) {
        byte[] crypted = null;
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception e) {
            Log.d("encrypt", e.toString());
        }
        String s = new String(Base64.encodeToString(crypted, Base64.DEFAULT));
        return s;
    }

    /**
     * 解密算法
     *
     * @param input
     * @param key
     * @return
     */
    public static String decrypt(String input, String key) throws Exception {
        byte[] output = null;
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] buffer = Base64.decode(input, Base64.DEFAULT);
        output = cipher.doFinal(buffer);
        return new String(output);
    }

}
