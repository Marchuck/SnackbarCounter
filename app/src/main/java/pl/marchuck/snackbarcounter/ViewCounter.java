package pl.marchuck.snackbarcounter;

import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author Lukasz Marczak
 * @since 08.07.16.
 */
public class ViewCounter {
    public interface Updatable {
        void onUpdate(long tic);
    }

    @Nullable
    private Updatable updatable;
    private long initialDelay = 0;
    private rx.Subscription subscription;

    public ViewCounter() {
    }


    public ViewCounter withInitialDelay(long secondsDelay) {

        this.initialDelay = secondsDelay;
        return this;
    }

    public ViewCounter withUpdatable(Updatable updatable) {
        this.updatable = updatable;
        return this;
    }

    public void start() {
        if (updatable == null) {return;}
        this.subscription = rx.Observable.interval(initialDelay, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (updatable != null) updatable.onUpdate(0);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (updatable != null) updatable.onUpdate(aLong);
                    }
                });
    }

    public void stop() {
        if (updatable != null) updatable = null;
        if (subscription != null) subscription.unsubscribe();
        subscription = null;
    }
}
