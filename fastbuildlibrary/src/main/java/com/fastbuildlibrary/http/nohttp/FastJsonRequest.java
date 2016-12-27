package com.fastbuildlibrary.http.nohttp;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.RestRequest;
import com.yolanda.nohttp.rest.StringRequest;

/**
 * 自定义 FastJson 解析请求数据
 * Created by Aaron on 2016/7/30.
 */
public class FastJsonRequest extends RestRequest<JSONObject> {

    public FastJsonRequest(String url) {
        super(url);
    }

    public FastJsonRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    public JSONObject parseResponse(Headers responseHeaders, byte[] responseBody) throws Throwable {
        String result = StringRequest.parseResponseString(responseHeaders, responseBody);//获取数据字符串
        JSONObject jsonObject = null;
        if (!TextUtils.isEmpty(result)) {
            jsonObject = JSON.parseObject(result);
        } else {
            // 这里默认的错误可以定义为你们自己的数据格式
            jsonObject = JSON.parseObject("{-1}");
        }
        return jsonObject;
    }

    @Override
    public String getAccept() {
        // 告诉服务器你接受什么类型的数据, 会添加到请求头的Accept中
        return "application/json";
    }
}
