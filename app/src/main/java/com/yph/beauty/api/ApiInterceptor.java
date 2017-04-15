package com.yph.beauty.api;

import com.yph.beauty.util.LogUtils;
import com.yph.beauty.util.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 2017/4/14.
 */

public class ApiInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        boolean isConnected = NetworkUtils.isConnected();
        LogUtils.e("---->isConnected = " + isConnected);
        if (!isConnected) {
            //无网络下强制使用缓存，无论缓存是否过期,此时该请求实际上不会被发送出去。
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response response = chain.proceed(request);
        if (isConnected) {
            String cacheControl = request.cacheControl().toString();
//            String cacheControl = "Cache-Control:public,max-age=0";
            return response.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        } else {
            //无网络
            return response.newBuilder()
                    .header("Cache-Control", "public,only-if-cached,max-stale=30*24*60*60")
                    .removeHeader("Pragma")
                    .build();
        }

    }
}
