package qulei.rxjava.demo;

import android.util.Log;

import rx.Subscriber;

public class Utils {

    public static final String TAG = "RxJava";

    public static Subscriber<Object> getSubscriber() {
        return new Subscriber<Object>() {
            @Override
            public void onCompleted() {
                Log.d(Utils.TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(Utils.TAG, "onError : " + e.toString());
            }

            @Override
            public void onNext(Object o) {
                Log.d(Utils.TAG, "onNext : " + o);
            }
        };
    }

}
