package com.imbaliu.myokhttp;


/**
 * call的工厂类
 */
public class MyOkHttpClient {

    private Dispatcher dispatcher;

    public MyOkHttpClient() {
        this(new Builder());
    }

     MyOkHttpClient(Builder builder) {
        this.dispatcher = builder.dispatcher;
    }


    public Dispatcher dispatcher(){
        return dispatcher;
    }
    public Call newCall(Request request) {
        return RealCall.newRealCall(this,request);
    }


    public Builder newBuilder() {
        return new Builder(this);
    }

    public static final class Builder {
        private Dispatcher dispatcher;

        public Builder() {
            dispatcher = new Dispatcher();
        }

        public Builder(MyOkHttpClient okHttpClient) {
            this.dispatcher = okHttpClient.dispatcher;
        }

        public MyOkHttpClient build(){
            return new MyOkHttpClient(this);
        }
    }


}
