package pl.marchuck.snackbarcounter;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class ViewCounter {

    @NonNull
    private final Updatable updatable;
    private final long initialDelay;

    private Disposable disposable;

    private ViewCounter(Builder builder) {
        updatable = builder.updatable;
        initialDelay = builder.initialDelay;
    }

    void start() {

        stop();

        disposable = Observable.interval(initialDelay, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updatable::onUpdate,
                        throwable -> updatable.onUpdate(0)
                );

    }

    void stop() {
        if (disposable != null) {
            disposable.dispose();
        }
        disposable = null;
    }


    public static final class Builder {
        private Updatable updatable;
        private long initialDelay;

        public Builder() {
        }

        public Builder updatable(Updatable val) {
            updatable = val;
            return this;
        }

        public Builder initialDelay(long val) {
            initialDelay = val;
            return this;
        }

        public ViewCounter build() {
            return new ViewCounter(this);
        }
    }
}
