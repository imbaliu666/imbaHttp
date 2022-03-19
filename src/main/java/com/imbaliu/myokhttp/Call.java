package com.imbaliu.myokhttp;


/**
 * 请求接口
 */
public interface Call {

    Response execute();

    void enqueue(Callback callback);
}
