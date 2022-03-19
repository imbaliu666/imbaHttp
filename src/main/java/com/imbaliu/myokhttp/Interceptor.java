package com.imbaliu.myokhttp;



public interface Interceptor{

    Response intercept(Chain chain);


    interface Chain{
      Response proceed(Request request);
    }
}
