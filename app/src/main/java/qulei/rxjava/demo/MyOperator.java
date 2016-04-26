package qulei.rxjava.demo;

import rx.Observable.Operator;
import rx.Subscriber;

public class MyOperator<T> implements Operator<T, T> {

    @Override
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) {
            @Override
            public void onCompleted() {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(e);
                }
            }

            @Override
            public void onNext(T t) {
                if (subscriber.isUnsubscribed()) {

                }
            }
        };
    }
}
