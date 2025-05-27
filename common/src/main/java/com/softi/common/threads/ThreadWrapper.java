package com.softi.common.threads;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadWrapper {

    private final Executor executor;

    public ThreadWrapper(Executor executor) {
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
