package net.branium.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import net.branium.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(
                () -> {
                    Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                    startActivity(intent);
                    finish();
                }, 2000
        );
    }
}