package com.imbaliu.myokhttp;


/**
 *  请求对象
 */
public class Request {

    private String url;
    private String body;
    private String header;
    private String method;

    public Request(String url, String body, String header,String method) {
        this.url = url;
        this.body = body;
        this.header = header;
        this.method=method;
    }

    public static class Builder{

        private String url;
        private String body;
        private String header;
        private String method;



        public Builder url(String url){
            this.url =url;
            return this;
        }

        public Builder get(){
            this.method ="GET";
            return this;

        }

        public Builder post(String body){
            this.body =body;
            this.method ="POST";
            return this;

        }
        public Builder addHeader(String header){
            this.header =header;
            return this;
        }

        public Request build() {
            return new Request(url,body,header,method);
        }
    }
}
