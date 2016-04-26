package qulei.rxjava.demo.compose;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public class MyTransformer<T> implements Observable.Transformer<T, T> {
    @Override
    public Observable<T> call(Observable<T> source) {

        source.takeUntil(Observable.combineLatest(null, null,
                new Func2<Object, Object, Boolean>() {
                    @Override
                    public Boolean call(Object o, Object o2) {
                        return null;
                    }
                }).onErrorReturn(new Func1<Throwable, Boolean>() {
                    @Override
                    public Boolean call(Throwable throwable) {
                        return null;
                    }
                }).takeFirst(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        return null;
                    }
                })
        );

        return null;
    }
}
