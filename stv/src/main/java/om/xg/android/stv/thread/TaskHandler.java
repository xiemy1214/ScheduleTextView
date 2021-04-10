package om.xg.android.stv.thread;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public class TaskHandler<T extends HandleMessageCallback> extends Handler {

    private final WeakReference<T> msgCallbacks;

    public TaskHandler(T msgCallback) {
        msgCallbacks = new WeakReference<>(msgCallback);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msgCallbacks.get() == null) {
            return;
        }
        msgCallbacks.get().handleMessage(msg);
    }
}
