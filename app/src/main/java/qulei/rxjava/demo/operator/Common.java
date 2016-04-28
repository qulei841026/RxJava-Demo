package qulei.rxjava.demo.operator;

import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import qulei.rxjava.demo.Tools;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;

public class Common {

    public static Observable ToTakeUntil() {
        return Observable.just("1").takeUntil(Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                subscriber.onNext("abc"); //如果不发射"abc"，Log信息回接收到onNext=1;
            }
        }));
    }

    public static Observable ToTakeFirst() {
        return Observable.just(1).takeFirst(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return true;
            }
        });
    }

    public static Observable ToCombineLatest() {
        return Observable.combineLatest(Observable.just("abc"),
                Observable.just("xyz"), new Func2<String, String, Boolean>() {
                    @Override
                    public Boolean call(String s, String s2) {
                        return s.equals(s2);
                    }
                });
    }

    public static Observable ToFlatMap3() {
        return Observable.just("create call onNext").flatMap(new Func1<String, Observable<?>>() {
            @Override
            public Observable<?> call(String s) {
                return Observable.just("ABC");
            }
        }, new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                return null;
            }
        }, new Func0<Observable<?>>() {
            @Override
            public Observable<?> call() {
                return Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("EFG");
                        subscriber.onCompleted();
                    }
                });
            }
        });
    }

    public static Observable getObservable(final String s) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return s;
            }
        });
    }

    public static Observable retryWhen() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.d("RxJava", "call");
                try {
                    subscriber.onNext(Tools.getMessage());
                    Log.d("RxJava", "onNext");
                    subscriber.onCompleted();
                } catch (Exception e) {
                    Log.d("RxJava", "onError");
                    subscriber.onError(e);
                }
            }
        }).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
            @Override
            public Observable<?> call(final Observable<? extends Throwable> observable) {
                Log.d("RxJava", "retryWhen call");
                return retryDelay(observable);
            }
        });
    }

    public static Observable<Object> retryDelay(Observable<? extends Throwable> o) {
        return o.zipWith(Observable.range(1, 10), new Func2<Throwable, Integer, Object>() {
            @Override
            public Object call(Throwable throwable, Integer integer) {
                return integer == 10 ? throwable : integer;
            }
        }).flatMap(new Func1<Object, Observable<Object>>() {
            @Override
            public Observable<Object> call(final Object object) {
                return Observable.create(new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        if (object instanceof Throwable) {
                            subscriber.onError((Throwable) object);
                        } else {
                            subscriber.onNext(object);
                            subscriber.onCompleted();
                        }
                    }
                }).delay(1, TimeUnit.SECONDS);
            }
        });
    }


}
