package com.imbaliu.myokhttp.http;

import com.imbaliu.myokhttp.Interceptor;
import com.imbaliu.myokhttp.RealInterceptorChain;
import com.imbaliu.myokhttp.Response;


/**
 * 连接拦截器
 */
public class ConnectInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) {
        Response response =chain.proceed(((RealInterceptorChain)chain).getRequest());
        return response;
    }
}
