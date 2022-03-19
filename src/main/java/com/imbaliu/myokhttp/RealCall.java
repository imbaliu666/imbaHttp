package com.imbaliu.myokhttp;

import com.imbaliu.myokhttp.http.BridgeInterceptor;
import com.imbaliu.myokhttp.http.CallServerInterceptor;
import com.imbaliu.myokhttp.http.ConnectInterceptor;
import com.imbaliu.myokhttp.http.RetryAndFollowUpInterceptor;

import java.util.ArrayList;
import java.util.List;

public class RealCall implements Call {
    private final MyOkHttpClient client;

    private final Request originalRequest;

    public RealCall(MyOkHttpClient okHttpClient, Request originalRequest) {
        this.client = okHttpClient;
        this.originalRequest = originalRequest;
    }

    static RealCall newRealCall(MyOkHttpClient okHttpClient, Request originalRequest) {
        RealCall call = new RealCall(okHttpClient, originalRequest);
        return call;
    }

    //同步请求
    @Override
    public Response execute() {
        //丢进队列中
        client.dispatcher().executed(this);
        //责任链模式的具体体现
        Response result = getResponseWithInterceptorChain();
        return result;
    }

    @Override
    public void enqueue(Callback callback) {
        //todo 异步处理
       client.dispatcher().enqueue(new AsyncCall(callback));
    }


    Response getResponseWithInterceptorChain()  {
        // Build a full stack of interceptors.
        List<Interceptor> interceptors = new ArrayList<>();
         //添加拦截器
        interceptors.add(new RetryAndFollowUpInterceptor());
        interceptors.add(new BridgeInterceptor());
        interceptors.add(new BridgeInterceptor());
        interceptors.add(new ConnectInterceptor());
        interceptors.add(new CallServerInterceptor());
        //初始化责任链
        Interceptor.Chain chain = new RealInterceptorChain(interceptors, 0, originalRequest, this);
        //启动责任链
        return chain.proceed(originalRequest);
    }


     class AsyncCall implements  Runnable{

        private final Callback responseCallback;

         public AsyncCall(Callback responseCallback) {
             this.responseCallback = responseCallback;
         }

         @Override
        public void run() {
           Response response = getResponseWithInterceptorChain();
           responseCallback.onResponse(RealCall.this,response);
        }
    }
}
