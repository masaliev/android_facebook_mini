package com.github.masaliev.facebookmini.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by mbt on 10/17/17.
 */

public class Helper {
    public static Observable getRetryWhenObservable(Observable<? extends Throwable> observable) {
        return observable.zipWith(Observable.range(1, 3), (throwable, attempts) -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("attempts", attempts);
            hashMap.put("throwable", throwable);
            return hashMap;
        })
                .flatMap((Function<HashMap<String, Object>, ObservableSource<?>>) hashMap -> {
                    Throwable throwable = (Throwable) hashMap.get("throwable");
                    Integer attempts = (Integer) hashMap.get("attempts");
                    if (throwable instanceof IOException && attempts < 3) {
                        return Observable.timer(1, TimeUnit.SECONDS);
                    }
                    return Observable.error(throwable);
                });
    }
}
