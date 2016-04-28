package qulei.rxjava.demo;

import android.util.Log;

public class Tools {
    public static int count;

    public static String getMessage() throws Exception {
        count++;
        Log.d(Utils.TAG, "execute getMessage count : " + count);
        if (count == 15) {
            return "getMessage";
        } else {
            throw new Exception("exception getMessage");
        }
    }

    public static String getWorkThread() {
        Log.d(Utils.TAG, Thread.currentThread().getName());
        return "workThread";
    }
}
