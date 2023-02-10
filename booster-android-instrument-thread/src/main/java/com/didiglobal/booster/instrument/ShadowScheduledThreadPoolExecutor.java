package com.didiglobal.booster.instrument;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author johnsonlee
 */
public class ShadowScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

    private static ThreadPoolExecutor EXECUTOR;

    /**
     * Initialize {@code ScheduledThreadPoolExecutor} with new thread name, this constructor is used by {@code ThreadTransformer} for thread renaming
     *
     * @param corePoolSize    the number of threads to keep in the pool, even if they are idle,
     *                        unless {@code allowCoreThreadTimeOut} is set
     * @param prefix          the prefix of new thread
     * @throws IllegalArgumentException if {@code corePoolSize < 0}
     */
    public ShadowScheduledThreadPoolExecutor(
            final int corePoolSize,
            final String prefix
    ) {
        this(corePoolSize, prefix, false);
    }

    /**
     * Initialize {@code ScheduledThreadPoolExecutor} with new thread name, this constructor is used by {@code ThreadTransformer} for thread renaming
     *
     * @param corePoolSize    the number of threads to keep in the pool, even if they are idle,
     *                        unless {@code allowCoreThreadTimeOut} is set
     * @param prefix          the prefix of new thread
     * @param optimize        the value indicates that the thread pool optimization should be applied
     * @throws IllegalArgumentException if {@code corePoolSize < 0}
     */
    public ShadowScheduledThreadPoolExecutor(
            final int corePoolSize,
            final String prefix,
            final boolean optimize
    ) {
        //super(corePoolSize, new NamedThreadFactory(prefix));
        super(
                EXECUTOR != null ? 1: corePoolSize,
                EXECUTOR != null ? EXECUTOR.getThreadFactory() : new NamedThreadFactory(prefix)
        );
        if (optimize) {
            if(getKeepAliveTime(TimeUnit.NANOSECONDS) <= 0L) {
                setKeepAliveTime(10L, TimeUnit.MILLISECONDS);
            }
            allowCoreThreadTimeOut(true);
        }
    }

    /**
     * Initialize {@code ScheduledThreadPoolExecutor} with new thread name, this constructor is used by {@code ThreadTransformer} for thread renaming
     *
     * @param corePoolSize    the number of threads to keep in the pool, even if they are idle,
     *                        unless {@code allowCoreThreadTimeOut} is set
     * @param threadFactory   the factory to use when the executor creates a new thread
     * @param prefix          the prefix of new thread
     * @throws IllegalArgumentException if {@code corePoolSize < 0}
     */
    public ShadowScheduledThreadPoolExecutor(
            final int corePoolSize,
            final ThreadFactory threadFactory,
            final String prefix
    ) {
        this(corePoolSize, threadFactory, prefix, false);
    }

    /**
     * Initialize {@code ScheduledThreadPoolExecutor} with new thread name, this constructor is used by {@code ThreadTransformer} for thread renaming
     *
     * @param corePoolSize    the number of threads to keep in the pool, even if they are idle,
     *                        unless {@code allowCoreThreadTimeOut} is set
     * @param threadFactory   the factory to use when the executor creates a new thread
     * @param prefix          the prefix of new thread
     * @param optimize        the value indicates that the thread pool optimization should be applied
     * @throws IllegalArgumentException if {@code corePoolSize < 0}
     */
    public ShadowScheduledThreadPoolExecutor(
            final int corePoolSize,
            final ThreadFactory threadFactory,
            final String prefix,
            final boolean optimize
    ) {
        //super(corePoolSize, new NamedThreadFactory(threadFactory, prefix));
        super(
                EXECUTOR != null ? 1: corePoolSize,
                EXECUTOR != null ? EXECUTOR.getThreadFactory() : new NamedThreadFactory(threadFactory, prefix)
        );
        if (optimize) {
            if(getKeepAliveTime(TimeUnit.NANOSECONDS) <= 0L) {
                setKeepAliveTime(10L, TimeUnit.MILLISECONDS);
            }
            allowCoreThreadTimeOut(true);
        }
    }

    /**
     * Initialize {@code ScheduledThreadPoolExecutor} with new thread name, this constructor is used by {@code ThreadTransformer} for thread renaming
     *
     * @param corePoolSize    the number of threads to keep in the pool, even if they are idle,
     *                        unless {@code allowCoreThreadTimeOut} is set
     * @param handler         the handler to use when execution is blocked because the thread bounds and queue capacities are reached
     * @param prefix          the prefix of new thread
     * @throws IllegalArgumentException if {@code corePoolSize < 0}
     */
    public ShadowScheduledThreadPoolExecutor(
            final int corePoolSize,
            final RejectedExecutionHandler handler,
            final String prefix
    ) {
        this(corePoolSize, handler, prefix, false);
    }

    /**
     * Initialize {@code ScheduledThreadPoolExecutor} with new thread name, this constructor is used by {@code ThreadTransformer} for thread renaming
     *
     * @param corePoolSize    the number of threads to keep in the pool, even if they are idle,
     *                        unless {@code allowCoreThreadTimeOut} is set
     * @param handler         the handler to use when execution is blocked because the thread bounds and queue capacities are reached
     * @param prefix          the prefix of new thread
     * @param optimize        the value indicates that the thread pool optimization should be applied
     * @throws IllegalArgumentException if {@code corePoolSize < 0}
     */
    public ShadowScheduledThreadPoolExecutor(
            final int corePoolSize,
            final RejectedExecutionHandler handler,
            final String prefix,
            final boolean optimize
    ) {
        //super(corePoolSize, new NamedThreadFactory(prefix), handler);
        super(
                EXECUTOR != null ? 1: corePoolSize,
                EXECUTOR != null ? EXECUTOR.getThreadFactory() : new NamedThreadFactory(prefix),
                handler
        );
        if (optimize) {
            if(getKeepAliveTime(TimeUnit.NANOSECONDS) <= 0L) {
                setKeepAliveTime(10L, TimeUnit.MILLISECONDS);
            }
            allowCoreThreadTimeOut(true);
        }
    }

    /**
     * Initialize {@code ScheduledThreadPoolExecutor} with new thread name, this constructor is used by {@code ThreadTransformer} for thread renaming
     *
     * @param corePoolSize    the number of threads to keep in the pool, even if they are idle,
     *                        unless {@code allowCoreThreadTimeOut} is set
     * @param threadFactory   the factory to use when the executor creates a new thread
     * @param handler         the handler to use when execution is blocked because the thread bounds and queue capacities are reached
     * @param prefix          the prefix of new thread
     * @throws IllegalArgumentException if {@code corePoolSize < 0}
     */
    public ShadowScheduledThreadPoolExecutor(
            final int corePoolSize,
            final ThreadFactory threadFactory,
            final RejectedExecutionHandler handler,
            final String prefix
    ) {
        this(corePoolSize, threadFactory, handler, prefix, false);
    }

    /**
     * Initialize {@code ScheduledThreadPoolExecutor} with new thread name, this constructor is used by {@code ThreadTransformer} for thread renaming
     *
     * @param corePoolSize    the number of threads to keep in the pool, even if they are idle,
     *                        unless {@code allowCoreThreadTimeOut} is set
     * @param threadFactory   the factory to use when the executor creates a new thread
     * @param handler         the handler to use when execution is blocked because the thread bounds and queue capacities are reached
     * @param prefix          the prefix of new thread
     * @param optimize        the value indicates that the thread pool optimization should be applied
     * @throws IllegalArgumentException if {@code corePoolSize < 0}
     */
    public ShadowScheduledThreadPoolExecutor(
            final int corePoolSize,
            final ThreadFactory threadFactory,
            final RejectedExecutionHandler handler,
            final String prefix,
            final boolean optimize
    ) {
        //super(corePoolSize, new NamedThreadFactory(threadFactory, prefix), handler);
        super(
                EXECUTOR != null ? 1: corePoolSize,
                EXECUTOR != null ? EXECUTOR.getThreadFactory() : new NamedThreadFactory(threadFactory, prefix),
                handler
        );
        if (optimize) {
            if(getKeepAliveTime(TimeUnit.NANOSECONDS) <= 0L) {
                setKeepAliveTime(10L, TimeUnit.MILLISECONDS);
            }
            allowCoreThreadTimeOut(true);
        }
    }

    @Override
    public void execute(Runnable command) {
        if (EXECUTOR == null) {
            super.execute(command);
        } else {
            EXECUTOR.execute(command);
        }
    }

    @Override
    public Future<?> submit(Runnable command) {
        if (EXECUTOR == null) {
            return super.submit(command);
        } else {
            return EXECUTOR.submit(command);
        }
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        if (EXECUTOR == null) {
            return super.submit(task);
        } else {
            return EXECUTOR.submit(task);
        }
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        if (EXECUTOR == null) {
            return super.submit(task, result);
        } else {
            return EXECUTOR.submit(task, result);
        }
    }

}
