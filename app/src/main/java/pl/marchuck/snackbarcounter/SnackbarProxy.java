package pl.marchuck.snackbarcounter;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * @author Lukasz Marczak
 * @since 08.07.16.
 */
public class SnackbarProxy {

    public interface RetryEvent {
        void onRetry();
    }

    private boolean isWorking = false;
    private long cycle = 15;
    private Snackbar snackBar;
    private long initialDelay;
    private ViewCounter viewCounter;
    private ViewCounter.Updatable updatable;

    public SnackbarProxy() {
    }

    public SnackbarProxy withCycle(long cycle) {
        this.cycle = cycle;
        return this;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public SnackbarProxy withInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
        return this;
    }

    public void start(View view, final @Nullable RetryEvent event) {

        snackBar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE);
        if (event != null)
            snackBar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    event.onRetry();
                }
            });

        updatable = new ViewCounter.Updatable() {
            @Override
            public void onUpdate(long tic) {
                tic = tic % cycle;
                snackBar.setText("No internet connection retry in " + (cycle - tic) + " seconds").show();
                if (tic + 1 == cycle && event != null) {
                    event.onRetry();
                }
            }
        };
        if (viewCounter == null) viewCounter = new ViewCounter();
        isWorking = true;
        viewCounter.withUpdatable(updatable).withInitialDelay(initialDelay).start();
    }

    public void stop() {
        if (snackBar != null) snackBar.dismiss();
        if (updatable != null) updatable = null;
        viewCounter.stop();
        isWorking = false;
    }
}
