package qulei.rxjava.demo.compose;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MyTransformer<T> implements Observable.Transformer<T, T> {
    @Override
    public Observable<T> call(Observable<T> source) {
        return source.map(new Func1<T, T>() {
            @Override
            public T call(T t) {
                return t;
            }
        }).subscribeOn(Schedulers.io());
    }
}
