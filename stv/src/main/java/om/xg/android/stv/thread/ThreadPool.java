package om.xg.android.stv.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {

    private static ExecutorService executorService;

    public synchronized static void execute(Runnable runnable) {
        if (executorService == null) {
            //线程数量
            int size = 5;
            executorService = new ThreadPoolExecutor(size, size, 0L,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(),
                    new TaskThreadFactory());
        }
        executorService.execute(runnable);
    }
}