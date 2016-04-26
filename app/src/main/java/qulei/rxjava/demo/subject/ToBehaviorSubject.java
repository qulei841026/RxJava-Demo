package qulei.rxjava.demo.subject;

import android.util.Log;

import qulei.rxjava.demo.Utils;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

public class ToBehaviorSubject {

    void demo() {
        BehaviorSubject<Integer> subject = BehaviorSubject.create();
        subject.onNext(1);
        subject.onNext(2);
        subject.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(Utils.TAG, "integer : " + integer);
            }
        });
        subject.onNext(3);
    }

    void demo2() {
        BehaviorSubject<Integer> s = BehaviorSubject.create();
        s.onNext(0);
        s.onNext(1);
        s.onNext(2);
        s.onCompleted();
        s.subscribe(Utils.getSubscriber());
    }
}
