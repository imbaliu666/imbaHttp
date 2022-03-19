package com.imbaliu.myokhttp;


import java.util.List;

/**
 * 责任链的核心实现类
 */
public class RealInterceptorChain implements Interceptor.Chain {
    private List<Interceptor> interceptors;
    private int index;
    private Request request;
    private RealCall call;

    public Request getRequest() {
        return request;
    }

    public RealInterceptorChain(List<Interceptor> interceptors, int index, Request request, RealCall call) {
        this.interceptors = interceptors;
        this.index = index;
        this.request = request;
        this.call = call;
    }

    public RealInterceptorChain copy() {
        return new RealInterceptorChain(interceptors, index, request, call);
    }

    @Override
    public Response proceed(Request request) {
        index++;
        Response response = interceptors.get(index).intercept(copy());
        return response;
    }


}
