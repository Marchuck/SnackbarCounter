package pl.marchuck.snackbarcounter;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import static android.arch.lifecycle.Lifecycle.Event.ON_STOP;

/**
 * @author Lukasz Marczak
 * @since 08.07.16.
 */
public class SnackbarTimer implements LifecycleObserver {

    public static final int TIMEOUT = 15;

    private boolean isWorking = false;
    private long cycle = TIMEOUT;

    private Snackbar snackBar;

    private long initialDelay;
    private ViewCounter viewCounter;
    private Updatable updatable;

    public SnackbarTimer() {
    }

    public SnackbarTimer withCycle(long cycle) {
        this.cycle = cycle;
        return this;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public SnackbarTimer withInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
        return this;
    }

    public void start(View view, final @Nullable Retryable event) {

        snackBar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE);
        if (event != null)
            snackBar.setAction("RETRY", v -> event.onRetry());

        updatable = tic -> {
            tic = tic % cycle;
            snackBar.setText("No internet connection retry in " + (cycle - tic) + " seconds").show();
            if (tic + 1 == cycle && event != null) {
                event.onRetry();
            }
        };
        if (viewCounter == null) {
            viewCounter = createViewCounter(initialDelay, updatable);
        }
        viewCounter.start();
        isWorking = true;
    }

    private ViewCounter createViewCounter(long initialDelay, Updatable updatable) {
        return new ViewCounter.Builder()
                .initialDelay(initialDelay)
                .updatable(updatable)
                .build();
    }


    @OnLifecycleEvent(ON_STOP)
    public final void stop() {
        viewCounter.stop();
        if (snackBar != null) {
            snackBar.dismiss();
            snackBar = null;
        }
        isWorking = false;
    }
}
