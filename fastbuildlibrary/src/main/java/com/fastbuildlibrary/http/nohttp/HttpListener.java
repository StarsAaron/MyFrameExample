package com.fastbuildlibrary.http.nohttp;
import com.yolanda.nohttp.rest.Response;

/**
 * <p>接受回调结果.</p>
 */
public interface HttpListener<T> {

    void onSucceed(int what, Response<T> response);

    void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis);

}
