package qulei.rxjava.demo.subject;

import android.util.Log;

import qulei.rxjava.demo.Utils;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

class ToPublishSubject {

    void demo() {
        PublishSubject<Integer> subject = PublishSubject.create();
        subject.onNext(1);
        subject.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(Utils.TAG, "integer : " + integer);
            }
        });
        subject.onNext(2);
        subject.onNext(3);
        subject.onNext(4);
    }

}
