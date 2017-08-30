package com.go2going.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Created by BlueT on 2017/8/26.
 */
public class WestCoastScheduledExecutor extends ScheduledThreadPoolExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WestCoastScheduledExecutor.class);

    public WestCoastScheduledExecutor(int corePoolSize) {
        super(corePoolSize);
    }

    public WestCoastScheduledExecutor(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }

    public WestCoastScheduledExecutor(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }

    public WestCoastScheduledExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return super.scheduleWithFixedDelay(wrapRunnable(command), initialDelay, delay, unit);
    }

    private Runnable wrapRunnable(Runnable command) {
        return new LogOnExceptionRunnable(command);
    }

    private class LogOnExceptionRunnable implements Runnable {
        private Runnable theRunnable;

        public LogOnExceptionRunnable(Runnable theRunnable) {
            super();
            this.theRunnable = theRunnable;
        }

        @Override
        public void run() {
            try {
                theRunnable.run();
            } catch (Exception e) {
                // LOG IT HERE!!!
                LOGGER.error("error in executing: " + theRunnable + ". It will no longer be run!");
                // and re throw it so that the Executor also gets this error so that it can do what it would
                // usually do
                throw new RuntimeException(e);
            }
        }
    }

}
