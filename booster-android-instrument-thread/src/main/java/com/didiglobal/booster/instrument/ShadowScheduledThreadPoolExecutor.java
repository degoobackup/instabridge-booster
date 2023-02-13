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

    private static ScheduledThreadPoolExecutor EXECUTOR;
    private boolean isIBThreadPoolExecutor = false;

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
        if (prefix.contains("instabridge")) {
            isIBThreadPoolExecutor = true;
        }
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
        if (prefix.contains("instabridge")) {
            isIBThreadPoolExecutor = true;
        }
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
        if (prefix.contains("instabridge")) {
            isIBThreadPoolExecutor = true;
        }
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
        if (prefix.contains("instabridge")) {
            isIBThreadPoolExecutor = true;
        }
        if (optimize) {
            if(getKeepAliveTime(TimeUnit.NANOSECONDS) <= 0L) {
                setKeepAliveTime(10L, TimeUnit.MILLISECONDS);
            }
            allowCoreThreadTimeOut(true);
        }
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        if (EXECUTOR == null || isIBThreadPoolExecutor) {
            return super.schedule(command, delay, unit);
        } else {
            return EXECUTOR.schedule(command, delay, unit);
        }
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        if (EXECUTOR == null || isIBThreadPoolExecutor) {
            return super.schedule(callable, delay, unit);
        } else {
            return EXECUTOR.schedule(callable, delay, unit);
        }
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        if (EXECUTOR == null || isIBThreadPoolExecutor) {
            return super.scheduleAtFixedRate(command, initialDelay, period, unit);
        } else {
            return EXECUTOR.scheduleAtFixedRate(command, initialDelay, period, unit);
        }
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        if (EXECUTOR == null || isIBThreadPoolExecutor) {
            return super.scheduleWithFixedDelay(command, initialDelay, delay, unit);
        } else {
            return EXECUTOR.scheduleWithFixedDelay(command, initialDelay, delay, unit);
        }
    }

}
