package pl.marchuck.snackbarcounter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SnackbarProxy proxy = new SnackbarProxy();
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        final SnackbarProxy.RetryEvent event = new SnackbarProxy.RetryEvent() {
            @Override
            public void onRetry() {
                Random random = new Random();
                int a = random.nextInt(255);
                int r = random.nextInt(255);
                int g = random.nextInt(255);
                int b = random.nextInt(255);
                textView.setBackgroundColor(Color.argb(a, r, g, b));
            }
        };
        proxy.start(textView, event);
        final TextView textView1 = (TextView) findViewById(R.id.fab_text);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView1.setText(!proxy.isWorking() ? "STOP" : "START");
                if (proxy.isWorking()) proxy.stop();
                else proxy.start(textView, event);
            }
        });

    }

    @Override
    protected void onDestroy() {
        proxy.stop();
        super.onDestroy();
    }
}
