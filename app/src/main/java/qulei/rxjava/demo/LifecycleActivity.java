package qulei.rxjava.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subjects.BehaviorSubject;

public class LifecycleActivity extends AppCompatActivity {

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext("onCreate");

        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.just(1)
                        .delay(10, TimeUnit.SECONDS)
                        .compose(LifecycleActivity.this.<Integer>bindToLifecycle())
                        .subscribe(Utils.getSubscriber());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext("onResume");
    }

    @Override
    protected void onPause() {
        lifecycleSubject.onNext("onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        lifecycleSubject.onNext("onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        lifecycleSubject.onNext("onDestroy");
        super.onDestroy();
    }

    private final BehaviorSubject<String> lifecycleSubject = BehaviorSubject.create();

    public final <T> Observable.Transformer<T, T> bindToLifecycle() {
        return bindFragment(lifecycleSubject);
    }

    public static <T> Observable.Transformer<T, T> bindFragment(@NonNull final Observable<String> lifecycle) {
        return bind(lifecycle, new Func1<String, String>() {
            @Override
            public String call(String s) {

                Log.d(Utils.TAG, "Func1 call s : " + s);

                switch (s) {
                    case "onCreate":
                        return "onDestroy";
                    case "onStart":
                        return "onStop";
                    case "onResume":
                        return "onPause";
                    case "onPause":
                        return "onStop";
                    case "onStop":
                        return "onDestroy";
                    case "onDestroy":
                        return "";
                }
                return "";
            }
        });
    }


    public static <T, R> Observable.Transformer<T, T> bind(Observable<R> lifecycle,
                                                           final Func1<R, R> correspondingEvents) {
        final Observable<R> sharedLifecycle = lifecycle.share();
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> source) {
                return source.takeUntil(
                        Observable.combineLatest(sharedLifecycle.take(1).map(correspondingEvents),
                                sharedLifecycle.skip(1), new Func2<R, R, Boolean>() {
                                    @Override
                                    public Boolean call(R bindUntilEvent, R lifecycleEvent) {
                                        Log.d(Utils.TAG, "bindUntilEvent : " + bindUntilEvent
                                                + " , lifecycleEvent : " + lifecycleEvent);
                                        return lifecycleEvent.equals(bindUntilEvent);
                                    }
                                })
                                .onErrorReturn(new Func1<Throwable, Boolean>() {
                                    @Override
                                    public Boolean call(Throwable throwable) {
                                        Log.d(Utils.TAG, "onErrorReturn call ");
                                        return true;
                                    }
                                })
                                .takeFirst(new Func1<Boolean, Boolean>() {
                                    @Override
                                    public Boolean call(Boolean aBoolean) {
                                        Log.d(Utils.TAG, "takeFirst call : " + aBoolean);
                                        return aBoolean;
                                    }
                                })
                );
            }
        };
    }


}