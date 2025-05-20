package com.softi.common.threads;

import java.util.concurrent.ThreadPoolExecutor;

public class ThreadWrapper {

    private final ThreadPoolExecutor executor;

    public ThreadWrapper(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    @FunctionalInterface
    public interface ThreadSupplier<T> {
        void invoke();
    }

    public void doInNewThread(ThreadSupplier threadSupplier) {
        executor.execute(threadSupplier::invoke);
    }

}
