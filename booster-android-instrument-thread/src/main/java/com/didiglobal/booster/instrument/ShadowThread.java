package com.didiglobal.booster.instrument;

import android.util.Log;

/**
 * @author johnsonlee
 */
public class ShadowThread extends Thread {

    /**
     * {@code U+200B}: Zero-Width Space
     */
    static final String MARK = "\u200B";

    public static Thread newThread(final String prefix) {
        return new Thread(prefix);
    }

    public static Thread newThread(final Runnable target, final String prefix) {
        return new Thread(target, prefix);
    }

    public static Thread newThread(final ThreadGroup group, final Runnable target, final String prefix) {
        return new Thread(group, target, prefix);
    }

    public static Thread newThread(final String name, final String prefix) {
        return new Thread(makeThreadName(name, prefix));
    }

    public static Thread newThread(final ThreadGroup group, final String name, final String prefix) {
        return new Thread(group, makeThreadName(name, prefix));
    }

    public static Thread newThread(final Runnable target, final String name, final String prefix) {
        return new Thread(target, makeThreadName(name, prefix));
    }

    public static Thread newThread(final ThreadGroup group, final Runnable target, final String name, final String prefix) {
        return new Thread(group, target, makeThreadName(name, prefix));
    }

    public static Thread newThread(final ThreadGroup group, final Runnable target, final String name, final long stackSize, final String prefix) {
        return new Thread(group, target, makeThreadName(name, prefix), stackSize);
    }

    public static Thread setThreadName(final Thread t, final String prefix) {
        t.setName(makeThreadName(t.getName(), prefix));
        return t;
    }

    public static String makeThreadName(final String name) {
        //Log.d("ShadowThread.makeThreadName", name);
        return name == null ? "" : name.startsWith(MARK) ? name : (MARK + name);
    }

    public static String makeThreadName(final String name, final String prefix) {
        //Log.d("ShadowThread.makeThreadName", prefix + "#" + name);
        return name == null ? prefix : (name.startsWith(MARK) ? name : (prefix + "#" + name));
    }

    /**
     * Initialize {@code Thread} with new name, this constructor is used by {@code ThreadTransformer} for renaming
     *
     * @param prefix the new name
     */
    public ShadowThread(final String prefix) {
        super(null, null, makeThreadName(prefix), adjustStackSize(null, prefix));
    }

    /**
     * Initialize {@code Thread} with new name, this constructor is used by {@code ThreadTransformer} for renaming
     *
     * @param target the object whose {@code run} method is invoked when this thread is started.
     *               If {@code null}, this thread's run method is invoked.
     * @param prefix the new name
     */
    public ShadowThread(final Runnable target, final String prefix) {
        super(null, target, makeThreadName(prefix), adjustStackSize(null, prefix));
    }

    /**
     * Initialize {@code Thread} with new name, this constructor is used by {@code ThreadTransformer} for renaming
     *
     * @param group  the thread group
     * @param target the object whose {@code run} method is invoked when this thread is started.
     *               If {@code null}, this thread's run method is invoked.
     * @param prefix the new name
     */
    public ShadowThread(final ThreadGroup group, final Runnable target, final String prefix) {
        super(group, target, makeThreadName(prefix), adjustStackSize(null, prefix));
    }

    /**
     * Initialize {@code Thread} with new name, this constructor is used by {@code ThreadTransformer} for renaming
     *
     * @param name   the original name
     * @param prefix the prefix of new name
     */
    public ShadowThread(final String name, final String prefix) {
        super(null, null, makeThreadName(name, prefix), adjustStackSize(name, prefix));
    }

    /**
     * Initialize {@code Thread} with new name, this constructor is used by {@code ThreadTransformer} for renaming
     *
     * @param group  the thread group
     * @param name   the original name
     * @param prefix the prefix of new name
     */
    public ShadowThread(final ThreadGroup group, final String name, final String prefix) {
        super(group, null, makeThreadName(name, prefix), adjustStackSize(name, prefix));
    }

    /**
     * Initialize {@code Thread} with new name, this constructor is used by {@code ThreadTransformer} for renaming
     *
     * @param target the object whose {@code run} method is invoked when this thread is started.
     *               If {@code null}, this thread's run method is invoked.
     * @param name   the original name
     * @param prefix the prefix of new name
     */
    public ShadowThread(final Runnable target, final String name, final String prefix) {
        super(null, target, makeThreadName(name, prefix), adjustStackSize(name, prefix));
    }

    /**
     * Initialize {@code Thread} with new name, this constructor is used by {@code ThreadTransformer} for renaming
     *
     * @param group  the thread group
     * @param target the object whose {@code run} method is invoked when this thread is started.
     *               If {@code null}, this thread's run method is invoked.
     * @param name   the original name
     * @param prefix the prefix of new name
     */
    public ShadowThread(final ThreadGroup group, final Runnable target, final String name, final String prefix) {
        super(group, target, makeThreadName(name, prefix), adjustStackSize(name, prefix));
    }

    /**
     * Initialize {@code Thread} with new name, this constructor is used by {@code ThreadTransformer} for renaming
     *
     * @param group     the thread group
     * @param target    the object whose {@code run} method is invoked when this thread is started.
     *                  If {@code null}, this thread's run method is invoked.
     * @param name      the original name
     * @param stackSize the desired stack size for the new thread, or zero to indicate that this parameter is to be ignored.
     * @param prefix    the prefix of new name
     */
    public ShadowThread(final ThreadGroup group, final Runnable target, final String name, final long stackSize, final String prefix) {
        super(group, target, makeThreadName(name, prefix), stackSize);
    }

    private static long adjustStackSize(String name, String prefix) {
        if (prefix != null && !prefix.contains("instabridge")) {
            return 256 * 1024;
        }
        if (name != null && !name.startsWith("ib-pool")) {
            return 256 * 1024;
        }
        // Use default stack size
        return 0;
    }

}
