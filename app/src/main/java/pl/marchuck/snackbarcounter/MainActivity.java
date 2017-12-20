package pl.marchuck.snackbarcounter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SnackbarTimer proxy = new SnackbarTimer();
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);

        final Retryable event = this::retryEvent;

        proxy.start(textView, event);

        final TextView textView1 = findViewById(R.id.fab_text);

        findViewById(R.id.fab).setOnClickListener(v -> this.onClick(textView1, event));
    }

    private void onClick(TextView v, Retryable event) {
        v.setText(!proxy.isWorking() ? "STOP" : "START");
        if (proxy.isWorking()) proxy.stop();
        else proxy.start(textView, event);
    }

    private void retryEvent() {
        Random random = new Random();
        int a = random.nextInt(255);
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        textView.setBackgroundColor(Color.argb(a, r, g, b));
    }
}
