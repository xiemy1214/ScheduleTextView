package om.xg.android.stv.thread;

import android.os.Message;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CountDownTask implements HandleMessageCallback {

    /*** 倒计时任务**/
    private static CountDownTask downTask;
    /*** ScheduledExecutorService 方式实现
     * 此方式中handler功能与timer方式一致**/
    private ScheduledExecutorService scheduled;
    /*** 任务Handler**/
    private TaskHandler<HandleMessageCallback> taskHandler;
    /*** 倒计时回调**/
    private CountDownCallback mDownCallback;
    /*** 倒计时时长**/
    private int mScheduleTime;
    /*** 开始倒计时(每一秒一次回调)**/
    private static final int TIMER_START = 1;
    /*** 倒计时结束**/
    private static final int TIMER_STOP = 2;
    /***线程池大小,初始化 ScheduledExecutorService**/
    private static final int corePoolSize = 3;
    private ScheduledThreadPoolExecutor poolExecutor;

    /**
     * 单例
     *
     * @return 单例倒计时任务
     */
    public static CountDownTask newInstance() {
        if (downTask == null) {
            synchronized (CountDownTask.class) {
                downTask = new CountDownTask();
            }
        }
        return downTask;
    }

    /**
     * 倒计时,以秒为单位
     *
     * @param scheduleTime 倒计时时长(秒)
     */
    public void countDownSchedule(int scheduleTime, CountDownCallback downCallback) {
        int period = 1000;
        this.mScheduleTime = (scheduleTime + 1);
        this.mDownCallback = downCallback;
        taskHandler = getTaskHandler();
        scheduled = new ScheduledThreadPoolExecutor(corePoolSize, new TaskThreadFactory("CountDown"));
        scheduled.scheduleAtFixedRate(() -> {
            Message msg = Message.obtain();
            msg.what = TIMER_START;
            msg.obj = mScheduleTime--;

            if (mScheduleTime < 0) {
                taskHandler.removeMessages(TIMER_START);
                msg.what = TIMER_STOP;
            }
            taskHandler.sendMessage(msg);
        }, 0, period, TimeUnit.MILLISECONDS);
    }

    public void schedule(long delay, final ScheduleCallback callback) {
        if (poolExecutor != null) {
            poolExecutor.shutdown();
        }
        poolExecutor = new ScheduledThreadPoolExecutor(corePoolSize, new TaskThreadFactory("schedule"));
        poolExecutor.schedule(() -> {
            if (callback != null) {
                callback.call();
            }
            return null;
        }, delay, TimeUnit.SECONDS);
    }

    private TaskHandler<HandleMessageCallback> getTaskHandler() {
        if (taskHandler == null) {
            taskHandler = new TaskHandler<>(this);
        }
        taskHandler.removeMessages(TIMER_START);
        taskHandler.removeMessages(TIMER_STOP);
        return taskHandler;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            //倒计时(每一秒回调一次)
            case TIMER_START:
                if (mDownCallback != null) {
                    mDownCallback.timerRun(mScheduleTime);
                }
                break;

            //计时结束
            case TIMER_STOP:
                if (mDownCallback != null) {
                    mDownCallback.timerStop();
                }
                closeCountDown();
                break;

            default:
                break;
        }
    }

    /**
     * 关闭(倒计时)线程池
     */
    public void closeCountDown() {
        if (scheduled != null) {
            scheduled.shutdown();
            scheduled = null;
            mScheduleTime = 0;
        }
        if (taskHandler != null) {
            taskHandler.removeMessages(TIMER_START);
            taskHandler.removeMessages(TIMER_STOP);
            taskHandler = null;
        }
    }

    /**
     * 关闭(定时器)线程池
     */
    public void closePoolExecutorCountDown() {
        if (poolExecutor != null) {
            poolExecutor.shutdown();
            poolExecutor = null;
        }
    }

    /**
     * 倒计时回调
     */
    public interface CountDownCallback {

        /**
         * 开始倒计时
         *
         * @param surplusTime 剩余时长(秒)
         */
        void timerRun(int surplusTime);

        /**
         * 倒计时结束
         */
        void timerStop();
    }

    /**
     * 定时器回调
     */
    public interface ScheduleCallback extends Callable<Object> {

        /**
         * 计时结束
         */
        @Override
        Object call();
    }
}
