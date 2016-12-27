package com.fastbuildlibrary.http.nohttp;

import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Response;

/**
 * 默认实现，不做任何处理，子类重写想要的方法
 * Created by Aaron on 2016/7/30.
 */
public class DefaultResponseListener implements OnResponseListener {
    @Override
    public void onStart(int i) {

    }

    @Override
    public void onSucceed(int i, Response response) {

    }

    @Override
    public void onFailed(int i, String s, Object o, Exception e, int i1, long l) {

    }

    @Override
    public void onFinish(int i) {

    }
}
