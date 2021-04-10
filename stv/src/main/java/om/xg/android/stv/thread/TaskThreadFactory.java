package om.xg.android.stv.thread;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskThreadFactory implements ThreadFactory {

    /**
     * 原子操作保证每个线程都有唯一的
     */
    private static final AtomicInteger threadNumber = new AtomicInteger(1);

    private final AtomicInteger mThreadNum = new AtomicInteger(1);

    private final String prefix;

    private final boolean daemonThread;

    private final ThreadGroup threadGroup;

    public TaskThreadFactory() {
        this("-thread-pool-" + threadNumber.getAndIncrement());
    }

    public TaskThreadFactory(String prefix) {
        this(prefix, false);
    }

    public TaskThreadFactory(String prefix, boolean daemon) {
        this.prefix = !TextUtils.isEmpty(prefix) ? prefix + "-thread-" : "";
        daemonThread = daemon;
        SecurityManager s = System.getSecurityManager();
        threadGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    @Override
    public Thread newThread(@NonNull Runnable runnable) {
        String name = prefix + mThreadNum.getAndIncrement();
        Thread ret = new Thread(threadGroup, runnable, name, 0);
        ret.setDaemon(daemonThread);
        return ret;
    }
}

