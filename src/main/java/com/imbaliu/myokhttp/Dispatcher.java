package com.imbaliu.myokhttp;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Dispatcher {

    private int maxRequests = 64;
    private int maxRequestsPerHost = 5;
    private ExecutorService executorService;

    //同步请求队列
    private final Deque<RealCall> runningSyncCalls = new ArrayDeque<>();
    //异步请求准备队列
    private final Deque<RealCall.AsyncCall> readyAsyncCalls = new ArrayDeque<>();
    //异步请求运行队列
    private final Deque<RealCall.AsyncCall> runningAsyncCalls = new ArrayDeque<>();

    public Dispatcher(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public Dispatcher() {
    }

    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), new ThreadFactory() {
                @Override
                public Thread newThread(Runnable runnable) {
                    Thread thread = new Thread("MyOkhttp");
                    return thread;
                }
            });
        }
        return executorService;
    }


    public void executed(RealCall call) {
        runningSyncCalls.add(call);
    }

    public void enqueue(RealCall.AsyncCall call){
        readyAsyncCalls.add(call);
        promoteAndExecute();
    }

    private void promoteAndExecute() {
        List<RealCall.AsyncCall> executableCalls = new ArrayList<>();
        //并发控制
        Iterator<RealCall.AsyncCall> iterator = readyAsyncCalls.iterator();
        while(iterator.hasNext()){
            RealCall.AsyncCall call = iterator.next();
            //简化条件
            if (runningAsyncCalls.size()>=this.maxRequests){
                break;
            }
            iterator.remove();
            executableCalls.add(call);
            runningAsyncCalls.add(call);
            this.executorService().execute(call);
        }


    }

}
